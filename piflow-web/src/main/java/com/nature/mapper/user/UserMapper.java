package com.nature.mapper.user;

import com.nature.component.user.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
	@Select("select * from user_info where name like #{name}")
	public List<User> findUserByName(@Param("name") String name);

	@Select("select * from user_info where name = #{name}")
	public User findUserByNames(String name);

	@Select("select * from user_info where user_name = #{userName}")
	public User getUserByuserNames(String userName);

	@Select("select * from user_info")
	public List<User> getUserList();

	@Insert("insert into user_info(id,name,age) values (#{id},#{name},#{age})")
	public void addUser(User user);

	@Update("update user_info set name=#{name},age=#{age} where id=#{id}")
	public int saveOrUpdate(User user);

	@Delete("delete from user_info where id=#{id}")
	public int deleteUser(int id);
}
