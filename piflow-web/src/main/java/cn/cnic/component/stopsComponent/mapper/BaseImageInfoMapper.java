package cn.cnic.component.stopsComponent.mapper;

import cn.cnic.component.stopsComponent.entity.BaseImageInfo;
import cn.cnic.component.stopsComponent.mapper.provider.BaseImageInfoMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BaseImageInfoMapper {

    @InsertProvider(type = BaseImageInfoMapperProvider.class,method = "addBaseImageInfo")
    public int addBaseImageInfo(BaseImageInfo baseImageInfo);

    @UpdateProvider(type = BaseImageInfoMapperProvider.class, method = "updateBaseImageInfo")
    public int updateBaseImageInfo(BaseImageInfo baseImageInfo);

    @Delete("delete from base_image_info where crt_user = #{username} and base_image_name = #{name}")
    public int deleteBaseImageInfo(@Param("username") String username, @Param("name") String name);

    @SelectProvider(type = BaseImageInfoMapperProvider.class,method = "getBaseImageInfoList")
    public List<BaseImageInfo> getBaseImageInfoList();

    @SelectProvider(type = BaseImageInfoMapperProvider.class,method = "getBaseImageInfoListByName")
    public List<BaseImageInfo> getBaseImageInfoListByName(@Param("name") String name);
}

