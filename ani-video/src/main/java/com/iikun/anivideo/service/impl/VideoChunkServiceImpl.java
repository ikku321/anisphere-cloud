package com.iikun.anivideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iikun.anivideo.config.UploadConfig;
import com.iikun.anivideo.entity.VideoChunkEntity;
import com.iikun.anivideo.mapper.VideoChunkMapper;
import com.iikun.anivideo.service.VideoChunkService;
import com.iikun.common.common.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

/**
 * 视频分片上传服务实现
 * <p>
 * 处理大文件分片上传相关业务逻辑
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VideoChunkServiceImpl implements VideoChunkService {

    private final VideoChunkMapper videoChunkMapper;
    private final UploadConfig uploadConfig;

    private static final String MP4_SUFFIX = ".mp4";
    private static final String CHUNK_DIR_NAME = "chunks";
    private static final int MAX_TOTAL_CHUNKS = 10000;

    @Override
    public Map<String, Object> init(String fileName, Long fileSize, Integer chunkSize) {
        validateMp4FileName(fileName);
        if (fileSize == null || fileSize <= 0) {
            throw new ServiceException("文件大小必须大于0");
        }
        if (chunkSize == null || chunkSize <= 0) {
            throw new ServiceException("分片大小必须大于0");
        }

        int totalChunks = (int) Math.ceil(fileSize / (double) chunkSize);
        if (totalChunks <= 0 || totalChunks > MAX_TOTAL_CHUNKS) {
            throw new ServiceException("分片数量不合法");
        }

        String uploadId = UUID.randomUUID().toString().replace("-", "");
        try {
            Files.createDirectories(taskDir(uploadId));
        } catch (IOException e) {
            log.error("创建分片目录失败 uploadId={}", uploadId, e);
            throw new ServiceException("创建分片上传任务失败");
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("uploadId", uploadId);
        result.put("fileName", safeFileName(fileName));
        result.put("fileSize", fileSize);
        result.put("chunkSize", chunkSize);
        result.put("totalChunks", totalChunks);
        result.put("uploadedChunks", Collections.emptyList());
        return result;
    }

    @Override
    public Map<String, Object> status(String uploadId) {
        validateUploadId(uploadId);
        try {
            List<VideoChunkEntity> rows = videoChunkMapper.selectList(new QueryWrapper<VideoChunkEntity>()
                    .eq("upload_id", uploadId)
                    .eq("status", 1)
                    .orderByAsc("chunk_index"));

            List<Integer> uploadedChunks = rows.stream()
                    .filter(row -> row.getChunkIndex() != null)
                    .filter(row -> row.getChunkPath() != null && Files.exists(Paths.get(row.getChunkPath())))
                    .map(VideoChunkEntity::getChunkIndex)
                    .toList();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("uploadId", uploadId);
            result.put("uploadedChunks", uploadedChunks);
            result.put("uploadedCount", uploadedChunks.size());
            return result;
        } catch (DataAccessException e) {
            log.error("查询分片状态失败 uploadId={}", uploadId, e);
            throw new ServiceException("查询分片状态失败");
        }
    }

    @Override
    public Map<String, Object> uploadChunk(String uploadId,
                                           Integer chunkIndex,
                                           Integer totalChunks,
                                           MultipartFile file,
                                           String videoId) {
        validateUploadId(uploadId);
        validateChunkIndex(chunkIndex, totalChunks);
        if (file == null || file.isEmpty()) {
            throw new ServiceException("分片文件不能为空");
        }

        try {
            Path dir = taskDir(uploadId);
            Files.createDirectories(dir);
            Path chunkPath = dir.resolve(chunkIndex + ".part").normalize();
            ensurePathInRoot(chunkPath, dir);
            file.transferTo(chunkPath);

            VideoChunkEntity existed = videoChunkMapper.selectOne(new QueryWrapper<VideoChunkEntity>()
                    .eq("upload_id", uploadId)
                    .eq("chunk_index", chunkIndex)
                    .last("LIMIT 1"));

            if (existed == null) {
                VideoChunkEntity entity = new VideoChunkEntity();
                entity.setUploadId(uploadId);
                entity.setVideoId(videoId);
                entity.setChunkIndex(chunkIndex);
                entity.setChunkPath(chunkPath.toString());
                entity.setStatus(1);
                entity.setCreateTime(LocalDateTime.now());
                videoChunkMapper.insert(entity);
            } else {
                existed.setVideoId(videoId);
                existed.setChunkPath(chunkPath.toString());
                existed.setStatus(1);
                videoChunkMapper.updateById(existed);
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("uploadId", uploadId);
            result.put("chunkIndex", chunkIndex);
            result.put("totalChunks", totalChunks);
            result.put("uploaded", true);
            return result;
        } catch (DataAccessException e) {
            log.error("保存分片记录失败 uploadId={}, chunkIndex={}", uploadId, chunkIndex, e);
            throw new ServiceException("保存分片记录失败");
        } catch (IOException e) {
            log.error("保存分片文件失败 uploadId={}, chunkIndex={}", uploadId, chunkIndex, e);
            throw new ServiceException("保存分片文件失败");
        }
    }

    @Override
    public Map<String, Object> merge(String uploadId, String fileName, Integer totalChunks) {
        validateUploadId(uploadId);
        validateMp4FileName(fileName);
        if (totalChunks == null || totalChunks <= 0 || totalChunks > MAX_TOTAL_CHUNKS) {
            throw new ServiceException("分片数量不合法");
        }

        Path outputPath = null;
        try {
            Path root = uploadRoot();
            Path dir = taskDir(uploadId);
            if (!Files.isDirectory(dir)) {
                throw new ServiceException("上传任务不存在或分片目录已清理");
            }

            List<Integer> missing = missingChunks(dir, totalChunks);
            if (!missing.isEmpty()) {
                throw new ServiceException("分片未上传完成，缺少: " + missing);
            }

            String targetName = UUID.randomUUID().toString().replace("-", "") + MP4_SUFFIX;
            outputPath = root.resolve(targetName).normalize();
            ensurePathInRoot(outputPath, root);

            try (OutputStream out = Files.newOutputStream(outputPath)) {
                for (int i = 0; i < totalChunks; i++) {
                    Path chunkPath = dir.resolve(i + ".part").normalize();
                    Files.copy(chunkPath, out);
                }
            }

            cleanupAfterMerge(uploadId);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("uploadId", uploadId);
            result.put("fileName", targetName);
            result.put("originalFileName", safeFileName(fileName));
            result.put("url", "/uploads/video/" + targetName);
            result.put("size", Files.size(outputPath));
            return result;
        } catch (ServiceException e) {
            throw e;
        } catch (IOException e) {
            if (outputPath != null) {
                try {
                    Files.deleteIfExists(outputPath);
                } catch (IOException deleteError) {
                    log.warn("删除未完成合并文件失败 path={}", outputPath, deleteError);
                }
            }
            log.error("合并分片失败 uploadId={}", uploadId, e);
            throw new ServiceException("合并分片失败");
        }
    }

    @Override
    public void cleanup(String uploadId) {
        validateUploadId(uploadId);
        try {
            Path dir = taskDir(uploadId);
            deleteDirectory(dir);
            videoChunkMapper.delete(new QueryWrapper<VideoChunkEntity>().eq("upload_id", uploadId));
        } catch (DataAccessException e) {
            log.error("清理分片数据库记录失败 uploadId={}", uploadId, e);
            throw new ServiceException("清理分片记录失败");
        } catch (IOException e) {
            log.error("清理分片目录失败 uploadId={}", uploadId, e);
            throw new ServiceException("清理分片目录失败");
        }
    }

    private List<Integer> missingChunks(Path dir, int totalChunks) {
        List<Integer> missing = new ArrayList<>();
        for (int i = 0; i < totalChunks; i++) {
            Path chunkPath = dir.resolve(i + ".part").normalize();
            if (!Files.exists(chunkPath) || !Files.isRegularFile(chunkPath)) {
                missing.add(i);
            }
        }
        return missing;
    }

    private void cleanupAfterMerge(String uploadId) {
        try {
            cleanup(uploadId);
        } catch (RuntimeException e) {
            log.warn("分片已合并，但临时数据清理失败 uploadId={}", uploadId, e);
        }
    }

    private Path uploadRoot() throws IOException {
        Path root = Paths.get(uploadConfig.getUploadDir()).toAbsolutePath().normalize();
        Files.createDirectories(root);
        return root;
    }

    private Path taskDir(String uploadId) throws IOException {
        Path root = uploadRoot().resolve(CHUNK_DIR_NAME).normalize();
        Path dir = root.resolve(uploadId).normalize();
        ensurePathInRoot(dir, root);
        return dir;
    }

    private void ensurePathInRoot(Path path, Path root) {
        if (!path.normalize().startsWith(root.normalize())) {
            throw new ServiceException("文件路径不合法");
        }
    }

    private void deleteDirectory(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            });
        } catch (IllegalStateException e) {
            if (e.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw e;
        }
    }

    private void validateUploadId(String uploadId) {
        if (uploadId == null || !uploadId.matches("^[A-Za-z0-9_-]{8,80}$")) {
            throw new ServiceException("上传任务ID不合法");
        }
    }

    private void validateChunkIndex(Integer chunkIndex, Integer totalChunks) {
        if (totalChunks == null || totalChunks <= 0 || totalChunks > MAX_TOTAL_CHUNKS) {
            throw new ServiceException("分片数量不合法");
        }
        if (chunkIndex == null || chunkIndex < 0 || chunkIndex >= totalChunks) {
            throw new ServiceException("分片序号不合法");
        }
    }

    private void validateMp4FileName(String fileName) {
        String safeName = safeFileName(fileName);
        if (!safeName.toLowerCase(Locale.ROOT).endsWith(MP4_SUFFIX)) {
            throw new ServiceException("只支持上传MP4格式视频");
        }
    }

    private String safeFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new ServiceException("文件名不能为空");
        }
        String normalized = fileName.replace("\\", "/");
        String name = normalized.substring(normalized.lastIndexOf('/') + 1).trim();
        if (name.isEmpty() || name.contains("..")) {
            throw new ServiceException("文件名不合法");
        }
        return name;
    }

}
