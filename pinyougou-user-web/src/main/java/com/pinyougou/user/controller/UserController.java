package com.pinyougou.user.controller;

import java.util.List;

import com.pinyougou.common.util.PhoneFormatCheckUtils;
import com.pinyougou.entity.Error;
import com.pinyougou.user.service.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.User;
import com.github.pagehelper.PageInfo;
import com.pinyougou.entity.Result;

import javax.validation.Valid;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {
        //判断手机号格式
        if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
            return new Result(false, "手机号格式不正确");
        }
        try {
            //生成验证码
            userService.createSmsCode(phone);
            return new Result(true, "验证码发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "验证码发送失败");
        }
    }

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<User> findAll() {
        return userService.findAll();
    }


    @RequestMapping("/findPage")
    public PageInfo<User> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return userService.findPage(pageNo, pageSize);
    }

    /**
     * 增加
     *
     * @param user
     * @return
     */
    @RequestMapping("/add/{smsCode}")
    public Result add(@Valid @RequestBody User user, BindingResult bindingResult, @PathVariable("smsCode") String smsCode) {
        try {
            if (bindingResult.hasErrors()) {
                Result result = new Result(false, "失败");
                List<FieldError> fieldErrors = bindingResult.getFieldErrors();
                for (FieldError fieldError : fieldErrors) {
                    result.getErrorList().add(new Error(fieldError.getField(), fieldError.getDefaultMessage()));
                }
                return result;
            }
            if (!userService.checkSmsCode(user.getPhone(), smsCode)) {
                Result result = new Result(false, "验证码错误!");
                result.getErrorList().add(new Error("smsCode","验证码错误!"));
                return result;
            }
            userService.add(user);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param user
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody User user) {
        try {
            userService.update(user);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne/{id}")
    public User findOne(@PathVariable(value = "id") Long id) {
        return userService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids) {
        try {
            userService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }


    @RequestMapping("/search")
    public PageInfo<User> findPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestBody User user) {
        return userService.findPage(pageNo, pageSize, user);
    }

}
