package cn.cnic.component.system.mapper;

import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.mapper.provider.FileMapperProvider;
import cn.cnic.component.system.mapper.provider.SysUserMapperProvider;
import cn.cnic.component.system.vo.FileVo;
import cn.cnic.component.system.vo.SysUserVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface FileMapper {

    @InsertProvider(type = FileMapperProvider.class, method = "insert")
    int insert(File file);

    @Insert("<script>"
            + "insert into file (id, file_name, file_type, file_path, associate_type, associate_id,  " +
            "crt_dttm, crt_user, enable_flag, last_update_dttm, last_update_user, version) values "
            + "<foreach collection='list' item='item' index='index' separator=','>"
            + "(#{item.id}, #{item.fileName}, #{item.fileType}, #{item.filePath}, #{item.associateType}, #{item.associateId}, " +
            "#{item.crtDttmStr}, #{item.crtUser}, #{item.enableFlagNum}, #{item.lastUpdateDttmStr}, " +
            "#{item.lastUpdateUser}, 0)"
            + "</foreach>"
            + "</script>")
    int insertBatch(List<File> files);

    @Select("select * from file where id = #{id}")
    File getById(@Param("id") String id);

    @Select("<script>"
            + "select * from file where id in "
            + "<foreach collection='ids.split(\",\")' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    List<File> getListByIds(@Param("ids") String ids);

    @Select("select * from file where file_name = #{name} and enable_flag = 1")
    File getByName(@Param("name") String name);

    @Select("select * from file where file_path = #{path} and enable_flag = 1")
    File getByPath(@Param("path") String path);

    @Select("select * from file where associate_id = #{associateId}  and associate_type = #{associateType} and enable_flag = 1")
    File getByAssociateId(@Param("associateId") String associateId, @Param("associateType") Integer associateType);

    @Select("select * from file where associate_id = #{associateId}  and associate_type = 2 and enable_flag = 1")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT),
    })
    FileVo getByAssociateIdAndDataProductCoverType(String associateId);

    @Select("select * from file where associate_id = #{associateId}  and associate_type = 1 and enable_flag = 1")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT),
    })
    FileVo getByAssociateIdAndDataProductType(String associateId);

    @Delete("delete from file where id = #{id}")
    int deleteById(@Param("id") Long id);

    @Delete("<script>"
            + "delete from file where id in "
            + "<foreach collection='list' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    int deleteBatchById(List<Long> ids);

    @Update("update file set enable_flag = 0, last_update_dttm = #{date}, last_update_user = #{username} where associate_id = #{associateId}")
    int fakeDeleteByAssociateId(@Param("associateId") String associateId, @Param("username") String username, @Param("date") String date);

}
