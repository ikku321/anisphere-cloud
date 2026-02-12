package com.iikun.userservice;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

/**
 * author iikun
 * time 2025/9/15 1:43
 * version 1.0.0
 * msg: 代码生成器
 */
@SpringBootTest
public class CodeGenerator {

    @Test
    public void test01(){
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/ani_sphere", "root", "ik101145")
                .globalConfig(builder -> builder.author("iikun")
                        .outputDir(System.getProperty("user.dir") + "/src/main/java")
                        .enableSwagger()) // 支持Swagger注解
                .packageConfig(builder -> builder
                        .parent("com.iikun.userservice")
                        .entity("entity")
                        .service("service")
                        .mapper("mapper")
                        .controller("controller")
                        // Mapper XML 输出路径
                        .pathInfo(Collections.singletonMap(OutputFile.mapper,
                                System.getProperty("user.dir") + "/src/main/resources/mapper")))
                .strategyConfig(builder -> builder
                        .addInclude("user")  // 填入表名
                        .entityBuilder().enableLombok()
                        .controllerBuilder().enableRestStyle())
                .execute();
    }

}
