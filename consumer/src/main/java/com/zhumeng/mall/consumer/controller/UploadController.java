package com.zhumeng.mall.consumer.controller;

import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.common.ConstVar;
import com.zhumeng.api.common.UploadFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.zhumeng.api.common.ConstVar.SHOW_IMAGE_PATH;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/22/23:04
 * 描述你的类：
 */
@RestController
@CrossOrigin
@Api(tags = "UploadController",description = "公共上传图片管理")
@RequestMapping("/upload")
public class UploadController {

    @ApiOperation(value = "添加商品相册，品牌Logo等照片")//brand pics
    @RequestMapping(value = "/pics", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult upload(String name, HttpServletRequest request) {//dicname 存储的目录名字 也就是图片属性
        //判断前端来的数据格式是否是二进制流
        if(request instanceof MultipartHttpServletRequest){
            System.out.println(name);
            //转换成二进制流的请求对象
            MultipartHttpServletRequest request1=(MultipartHttpServletRequest) request;
            List<MultipartFile> list=request1.getFiles("file");//file前端文件上传组件的名字
            //遍历list
            Iterator<MultipartFile> it=list.iterator();
            while (it.hasNext()){
                MultipartFile photo=it.next();
                if(photo!=null){//有数据
                    //设置文件名 保证该图片的唯一性
                    String fileName= UUID.randomUUID().toString();
                    File file=UploadFile.getPath();
                    //将要上传的路径

                    //判断文件后缀是否符合上传的文件类型
                    String suffix=photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1);
                    if((ConstVar.IMG_TYPE_DMG.equals(suffix.toUpperCase()) ||
                            ConstVar.IMG_TYPE_GIF.equals(suffix.toUpperCase()) ||
                            ConstVar.IMG_TYPE_JPEG.equals(suffix.toUpperCase()) ||
                            ConstVar.IMG_TYPE_JPG.equals(suffix.toUpperCase()) ||
                            ConstVar.IMG_TYPE_PNG.equals(suffix.toUpperCase()) ||
                            ConstVar.IMG_TYPE_SVG.equals(suffix.toUpperCase()))) {
                        //构建真实的文件名字
                        String trueName=file.getAbsolutePath()+File.separator+fileName+"."+suffix;
                        //真实要存储在服务器的文件名
                        File file1=new File(trueName);
                        //传输到后端
                        try {
                            photo.transferTo(file1);
                            //传输图片
                            //把一些信息反馈给前端
                            Map<String,String> map=new HashMap(16);
                            Map<String, String> result = new HashMap<>(16);
                            result.put("contentType", photo.getContentType());
                            result.put("suffix", suffix);
                            result.put("fileName",fileName);
                            result.put("fileSize", photo.getSize() + "");
                            result.put("nav",SHOW_IMAGE_PATH+"/"+fileName+"."+suffix);//浏览图片的文件名称
                            return CommonResult.success(result);
                        } catch (IOException e) {
                            CommonResult.failed("上传失败");

                            e.printStackTrace();
                        }
                    }

                }
            }
        }
        return CommonResult.failed("上传失败");
    }

}
