package com.zhumeng.mall.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.baidu.aip.face.AipFace;
import com.zhumeng.api.anno.LogType;
import com.zhumeng.api.anno.SystemLog;
import com.zhumeng.api.anno.UserLoginToken;
import com.zhumeng.api.common.AIFactoryUtil;
import com.zhumeng.api.common.CommonResult;
import com.zhumeng.api.dto.AIBaiduFaceBean;
import com.zhumeng.api.dto.AIFaceBean;
import com.zhumeng.api.dto.UmsAdminLoginParam;
import com.zhumeng.api.model.UmsAdmin;
import com.zhumeng.api.service.ITokenService;
import com.zhumeng.api.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建人：朱蒙
 * 创建时间：2021/1/28/23:12
 * 描述你的类：
 * @author Lenovo
 */

@RestController
@Api(tags = "AdminController", description = "后台用户管理")
@RequestMapping("/admin")
@CrossOrigin//解决跨域问题，
public class UmsUserController {

    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.IUserService",
            interfaceClass = IUserService.class,
            timeout = 120000
    )
    private IUserService userService;

    @Reference(
            version = "1.0.0",
            interfaceName = "com.zhumeng.api.service.ITokenService",
            interfaceClass = ITokenService.class,
            timeout = 120000
    )
    private ITokenService tokenService;
    //得到百度人脸识别对象
    private AipFace aipFace= AIFactoryUtil.getAipFace();
    @ApiOperation(value = "登录以后返回token")
    @PostMapping(value = "/login")
    @ResponseBody
    //还没进入到系统 因此token没有产生
    //@SystemLog(description = "登录", type = LogType.USER_LOGIN)
    public CommonResult login(@RequestBody UmsAdminLoginParam user) {
        UmsAdmin admin=userService.findByUsername(user.getUsername());
        //通过用户名找到用户
        //判断该用户是否存在
        if(admin==null){
            return CommonResult.validateFailed("用户名不存在！");
        }
        //验证密码
        if(!user.getPassword().equals(admin.getPassword())){
            Map map=new HashMap();
            map.put("error_code",500);
            return CommonResult.failed(map);
        }else{
            //密码验证成功，生成token
            String token=tokenService.getToken(admin.getId().toString(),admin.getPassword());
            Map map=new HashMap();
            map.put("token",token);
            map.put("error_code","200");
            map.put("tokenHead",tokenHead);
            return CommonResult.success(map);
            //送入前端
        }
    }

    /**
     * 脸部登录一个用户
     * @param
     * @return
     */
    @ApiOperation(value = "脸部登录以后返回token")
    @PostMapping(value = "/flogin")
    @ResponseBody
    //@SystemLog(description = "人脸登录", type = LogType.USER_LOGIN)
    public CommonResult faceLogin(@RequestBody AIFaceBean faceBean) {
        Map<String,String> tokenMap=new HashMap<String,String>();
        String groupid="login";
        //搜索完毕后，百度提供了一个结果
        JSONObject resultJson=aipFace.search(faceBean.getImgdata(),"BASE64",groupid,null);
        //转换结果
        AIBaiduFaceBean faceBean1=JSON.parseObject(resultJson.toString(), AIBaiduFaceBean.class);
        if("0".equals(faceBean1.getError_code())&&("SUCCESS".equals(faceBean1.getError_msg()))){
            //验证成功，这里对人脸先检索，是否已经录入，设置判定条件为返回score(人脸识别相似度)大于80即代表同一个人
            if(faceBean1.getResult().getUser_list().get(0).getScore()>80f){
                faceBean.setError_code(faceBean1.getError_code());
                faceBean.setError_msg(faceBean1.getError_msg());
                //从百度人脸库中取出userid
                String userid=faceBean1.getResult().getUser_list().get(0).getUser_id();
                //根据userid找到该用户
                UmsAdmin admin=userService.findUserById(Long.parseLong(userid));
                String username=admin.getUsername();
                String password=admin.getPassword();
                //做token验证
                String token=tokenService.getToken(userid,password);
                //得到token码 和用户的json 并且存储id 方便进行其他的登录
                tokenMap.put("error_code",faceBean1.getError_code());
                tokenMap.put("token", token);
                tokenMap.put("tokenHead", tokenHead);
                tokenMap.put("username", username);
                tokenMap.put("password", password);
                return CommonResult.success(tokenMap);
            }else {
                tokenMap.put("error_code",faceBean1.getError_code());
                return CommonResult.failed(tokenMap);
            }
        }else {
            tokenMap.put("error_code",faceBean1.getError_code());
            return CommonResult.failed(tokenMap);
        }
    }


//38:00
    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping(value = "/info")
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "获取当前登录用户信息", type = LogType.USER_INFO)
    public CommonResult getAdminInfo(HttpServletRequest request) {
        Map<String,Object> map=new HashMap();
        String token=request.getHeader(tokenHeader);
        UmsAdmin admin=userService.findByUmsAdmin(token.split("@")[1]);
        map.put("username",admin.getUsername());
        map.put("roles",new String[]{"TEST"});
        map.put("icon",admin.getIcon());
        return CommonResult.success(map);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    @UserLoginToken
    @SystemLog(description = "登出", type = LogType.USER_LOGIN_OUT)
    public CommonResult logout() {
        return CommonResult.success(null);
    }
    /**
     * 人脸注册
     * @return
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/reg")
    @ResponseBody
    public CommonResult reg(@RequestBody UmsAdmin umsAdmin){
        //先判断用户名是否已经注册了
        if(userService.findByUsername(umsAdmin.getUsername())!=null){
            //!=null证明该用户名已经存在
            Map<String,String> map=new HashMap<String,String>();
            map.put("error_code","500");
            return CommonResult.failed(map);
        }
        UmsAdmin admin=userService.reg(umsAdmin);
        String userid=admin.getId().toString();
        //人脸图片弄进百度

        String groupid="login";
        //删除字符串前的提示信息 "data:image/png;base64,"
        String b64=umsAdmin.getPic().substring(22);
        HashMap<String,String> map=new HashMap();
        map.put("user_info",umsAdmin.getPassword());
        aipFace.addUser(b64,"BASE64",groupid,userid,map);

        Map<String,String> map1=new HashMap();
        map1.put("error_code","200");
        return CommonResult.success(map1);
    }
}
