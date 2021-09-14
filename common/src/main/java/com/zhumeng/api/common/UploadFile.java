package com.zhumeng.api.common;

import org.springframework.util.ClassUtils;

import java.io.File;

/**
 * 创建人：朱蒙
 * 创建时间：2021/2/26/20:56
 * 描述你的类：
 */
public class UploadFile {
    public final static String IMG_PATH_PREFIX="static/upload/images/";
    //主要是得到本项目下本模块的绝对路径，例如
    // //E:\\DOC\\java\\MALL\\mymall\\mall-consumer\\target\\classes\
    public static File getPath(){
        String path=ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String newPath=path+IMG_PATH_PREFIX;
        File file=new File(newPath);
        if(!file.exists()){
            file.mkdir();
        }
        return file;
    }
}
