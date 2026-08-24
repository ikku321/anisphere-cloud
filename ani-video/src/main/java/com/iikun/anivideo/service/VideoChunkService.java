package com.iikun.anivideo.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 视频分片上传服务接口
 * <p>
 * 处理大文件分片上传相关业务逻辑
 * </p>
 */
public interface VideoChunkService {

    /**
     * 初始化一次分片上传任务。
     */
    Map<String, Object> init(String fileName, Long fileSize, Integer chunkSize);

    /**
     * 查询指定上传任务已经完成的分片序号。
     */
    Map<String, Object> status(String uploadId);

    /**
     * 保存单个分片。重复上传同一分片时覆盖文件并更新记录，便于断点续传。
     */
    Map<String, Object> uploadChunk(String uploadId,
                                    Integer chunkIndex,
                                    Integer totalChunks,
                                    MultipartFile file,
                                    String videoId);

    /**
     * 合并指定上传任务的全部分片并返回最终视频访问地址。
     */
    Map<String, Object> merge(String uploadId, String fileName, Integer totalChunks);

    /**
     * 清理指定上传任务的临时分片文件和数据库记录。
     */
    void cleanup(String uploadId);
}
