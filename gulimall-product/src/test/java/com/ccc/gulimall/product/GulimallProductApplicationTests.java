package com.ccc.gulimall.product;

import com.aliyun.oss.OSSClient;
import com.ccc.gulimall.product.entity.BrandEntity;
import com.ccc.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class GulimallProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Autowired
    OSSClient ossClient;
    @Test
    void contextLoads() {
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\Software\\upupone\\wallpaper\\UpupooWallpaper\\2000557332\\image\\bg.png");
            ossClient.putObject("gulimall-cntt","bg.png",fileInputStream);
            ossClient.shutdown();
            System.out.println("上传成功");
        } catch (FileNotFoundException e) {


        }
    }

}
