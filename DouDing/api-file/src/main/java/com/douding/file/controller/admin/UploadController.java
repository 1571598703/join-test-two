package com.douding.file.controller.admin;

import com.douding.server.domain.File;
import com.douding.server.domain.Teacher;
import com.douding.server.domain.Test;
import com.douding.server.dto.FileDto;
import com.douding.server.dto.ResponseDto;
import com.douding.server.enums.FileUseEnum;
import com.douding.server.service.FileService;
import com.douding.server.service.TestService;
import com.douding.server.util.Base64ToMultipartFile;
import com.douding.server.util.CopyUtil;
import com.douding.server.util.UuidUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Base64;
import java.util.List;

/*
    返回json 应用@RestController
    返回页面  用用@Controller
 */
@RequestMapping("/admin/file")
@RestController

public class UploadController {

    private static final Logger LOG = LoggerFactory.getLogger(UploadController.class);
    public static final String BUSINESS_NAME = "文件上传";
    @Resource
    private TestService testService;

    @Value("${file.path}")
    private String FILE_PATH;

    @Value("${file.domain}")
    private String FILE_DOMAIN;

    @Resource
    private FileService fileService;

    @RequestMapping("/upload")
//    public ResponseDto upload(@RequestBody FileDto fileDto) throws Exception {
    public ResponseDto upload(@RequestBody FileDto fileDto) throws Exception {

        System.out.println("请求进来了");

        ResponseDto<File> responseDto = new ResponseDto<>();
        responseDto.setSuccess(true);
        File copy = CopyUtil.copy(fileDto, File.class);
        String shard = fileDto.getShard();
        // 去掉base64前缀 data:image/jpeg;base64,
        shard = shard.substring(shard.indexOf(",", 1) + 1);
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(shard);
        ByteBuffer byteBuffer = ByteBuffer.allocate(decode.length);
        byteBuffer.put(decode);
//        FileChannel data = new FileInputStream("data").getChannel();
//        int read = data.read(byteBuffer);
//        System.out.println(read);
        FileChannel channel = new FileOutputStream("ceshi.jpg").getChannel();
        channel.write(byteBuffer);
        copy.setPath(shard);
        fileDto.setPath(shard);
        fileService.save(fileDto);
        channel.close();
        responseDto.setContent(copy);
        return responseDto;

    }

    //合并分片
    public void merge(FileDto fileDto) throws Exception {
        LOG.info("合并分片开始");

    }

    @GetMapping("/check/{key}")
    public ResponseDto check(@PathVariable String key) throws Exception {
        LOG.info("检查上传分片开始：{}", key);

        ResponseDto<File> responseDto = new ResponseDto<>();
//
//        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(key));
//
//        bos.write(key.getBytes());
//        String path = bos.getClass().getResource(key).getPath();
//        bos.close();
        if (key != null) {
            responseDto.setCode(null);
        }
        return responseDto;
    }

}//end class
