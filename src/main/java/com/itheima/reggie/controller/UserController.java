package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.utils.SMSUtils;
import com.github.qcloudsms.httpclient.HTTPException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *  发送验证码的请求
     * @param user
     * @param session， 将验证码存入session中
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone=user.getPhone();
        if(!StringUtils.isEmpty(phone)) {
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);

            //需要将生成的验证码保存到Session
            session.setAttribute(phone,code); // 键值对的类型
            return R.success("手机验证码短信发送成功");
        }
        return R.error("手机短信发送失败");
    }

    /**
     *  移动端用户登录
     * @param map
     * @param session
     * @return
     */

    @PostMapping("/login")
                                // 使用Map类来封装，键值对，phone：code
    public R<User> login(@RequestBody Map map, HttpSession session) {

        log.info(map.toString());

        // 获取手机号
        String phone = map.get("phone").toString(); // 将得到的value转换成字符串类型

        // 获取验证码
        String code = map.get("code").toString();
        // 从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);

        // 进行验证码比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeInSession !=null && codeInSession.equals(code)){

            // 如果能够比对成功，说明登录成功
            // 判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone); // 如果手机号码和数据库中的号码相等，说明已经注册过

            User user = userService.getOne(queryWrapper);

            if (user == null){ // 说明是新用户
                // 不为空则是新用户，自动完成注册
                user = new User();// 重新创建一个新的对象，覆盖原来的，重置设置其属性值
                user.setPhone(phone);
                user.setStatus(1); // 状态：1表示正常
                userService.save(user); // 添加到数据库中
            }

            // 登录成功后，将登录后的id，传给session
            // 当http请求中session有id时：就不会闪退页面，
            session.setAttribute("user",user.getId()); // key为“user”，value为：id

            return R.success(user);
        }
        return R.error("短信发送失败");
    }


    /**
     *  用户登出
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        //清理Session中保存的当前用户登录的id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }

}
