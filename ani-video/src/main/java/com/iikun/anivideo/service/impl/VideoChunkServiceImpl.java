package com.iikun.anivideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

}
