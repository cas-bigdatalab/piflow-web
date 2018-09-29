package com.nature.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nature.base.util.LoggerUtil;
import com.nature.component.user.model.User;
import com.nature.component.user.service.UserService;

/**
 * Created by wangwei on 2016/9/2.
 */
@RestController
@RequestMapping({ "/home" })
public class UserCtrl {

	/**
	 * 引入日志，注意都是"org.slf4j"包下
	 */
	Logger logger = LoggerUtil.getLogger();

	@Autowired
	UserService userService;

	@RequestMapping(value = "/user")
	@ResponseBody
	public List<User> user(String name) {
		List<User> userList = userService.findByName(name);
		logger.info("111111111111111111111111111");
		return userList;
	}

	@RequestMapping(value = "/addUser")
	public User addUser(User user) {
		return userService.addUser(user);
	}

	@RequestMapping(value = "/deleteUser")
	public String deleteUser(int id) {
		String resultStr = "删除失败";
		int result = userService.deleteUser(id);
		if (1 <= result) {
			resultStr = "删除 成功";
		}
		return resultStr;
	}

	@RequestMapping(value = "/saveOrUpdate")
	public String saveOrUpdate(User user) {
		String resultStr = "更新操作失败";
		int result = userService.saveOrUpdate(user);
		if (1 <= result) {
			resultStr = "更新操作成功";
		}
		return resultStr;
	}

	@RequestMapping(value = "/getUserList")
	@ResponseBody
	public List<User> getUserList() {
		return userService.getUserList();
	}

}
//@RestController是对应的restful风格的控制器，@RequestMapping里面可以对应一个数组哦
