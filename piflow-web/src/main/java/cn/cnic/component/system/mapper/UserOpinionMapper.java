package cn.cnic.component.system.mapper;

import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.entity.UserOpinion;
import cn.cnic.component.system.mapper.provider.SysUserMapperProvider;
import cn.cnic.component.system.mapper.provider.UserOpinionMapperProvider;
import cn.cnic.component.system.vo.SysUserVo;
import cn.cnic.component.system.vo.UserOpiniontVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * 注意事项：检索sql都需要加上enable_flag = 1
 */
@Mapper
public interface UserOpinionMapper {

    @InsertProvider(type = UserOpinionMapperProvider.class, method = "insert")
    public int insert(UserOpinion userOpinion);

    @UpdateProvider(type = UserOpinionMapperProvider.class, method = "update")
    public int update(UserOpinion userOpinion);

    @Select("select * from user_opinion WHERE id=#{id} and enable_flag = 1")
    public UserOpinion getById(@Param("id") String id);



    @Update("UPDATE user_opinion set enable_flag = 0 WHERE id=#{id}")
    public int delete(@Param("id") String id);

    @Select("<script>"
            + "select * from user_opinion as dp "
            + "<where>"
            + "<if test=\"company != null and company != '' \">"
            + " and dp.company like CONCAT('%', #{company}, '%') "
            + "</if>"
            + "<if test=\"organization != null and organization != '' \">"
            + " and dp.organization like CONCAT('%', #{organization}, '%') "
            + "</if>"
            + "<if test=\"name != null and name != '' \">"
            + " and dp.name like CONCAT('%', #{name}, '%') "
            + "</if>"
            + "<if test=\"crtUser != null and crtUser != '' \">"
            + " and dp.crt_user like CONCAT('%', #{crtUser}, '%') "
            + "</if>"
            + " and dp.enable_flag = 1 "
            + "</where>"
            + "</script>")
    List<UserOpinion> getByPageWithAdmin(UserOpiniontVo userOpiniontVo);


    @Select("<script>"
            + "select * from user_opinion as dp "
            + "<where>"
            + "<if test=\"name != null and name != '' \">"
            + " and dp.name like CONCAT('%', #{name}, '%') "
            + "</if>"
            + " and crt_user = #{crtUser} "
            + " and dp.enable_flag = 1 "
            + "</where>"
            + "</script>")
    List<UserOpinion> getByPage(UserOpiniontVo userOpiniontVo);

    @UpdateProvider(type = UserOpinionMapperProvider.class, method = "updateState")
    int updateState(UserOpiniontVo userOpiniontVo);
}
