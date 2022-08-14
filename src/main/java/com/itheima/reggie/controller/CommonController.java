package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 *  文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {


    /**
     *  文件上传
     */


    // 定义一个存储路径的遍历
    // 使用@Value这个注解会注入配置文件对应的值，赋值给basePath遍历
    @Value("${reggie.path}")
    private String basePath;


    @PostMapping("/upload")
//    在Controller的方法中声明一个MultipartFile接口类型的参数即可接收上传的文件。
    public R<String> upload(MultipartFile file){ // 这里的参数名要和前端发送的文件名（name = file）一致
        // file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString()); // 打印前端上传过来的file对象

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename(); // xxx.jpg
        // 获取后缀名：  xxx.jpg
        // String类中的substring方法，可以从传入的索引处截取，截取到末尾，得到新的字符串
        // lastIndexOf是返回指定字符在此字符串中最后一次出现处的索引
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); // 得到后缀

        // 使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        //UUID：通用唯一识别码，保证唯一性
        // 随机生成的UUID + 后缀即可
        String fileName = UUID.randomUUID().toString() + suffix;// dsfdss.jpg

        // 创建一个目录对象
        File dir = new File(basePath); // 这个路径写配置文件中的路径
        // 判断当前目录是否存在
        if (!dir.exists()){
            // 如果目录不存在，需要创建
            dir.mkdir();
        }


        try {
            // 将临时文件转存到指定位置
//            file.transferTo(new File("C:\\Users\\aufs\\UserFile\\hello.jpg"));// 这个相当于把这个图片名字写死了
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return R.success(fileName); // 将文件名返回给前端

    }

    /**
     *  文件下载
     * @param name 文件名
     * @param response 响应对象，通过响应对象可以创建其对应的输出流，将文件写回浏览器，在浏览器展示图片
     */
    @GetMapping("/download") // 这个是前端的路径
    // 定义一个文件下载的方法，下载的路径和前端代码一致
    public void download(String name, HttpServletResponse response){

        try {
            // 创建一个输入流对象，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // 创建一个输出流对象，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            // 设置响应的内容类型  图片格式
            response.setContentType("image/jpeg");

            // 输入流去读，读到的数据去写
            int len = 0; // 定义一个初始的长度length
            // 创建一个字节数组对象长度为1024，通过这个长度，可以让输入流，每次读1MB
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){ // 这里不等于-1 的意思是没有读完，就执行下面的语句（读完了是-1）
                // 读到多少，就往浏览器写多少，从0开始写，写完len这个全部长度
                outputStream.write(bytes,0,len);
                outputStream.flush(); // 刷新流
            }

            // 关闭两个流
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
