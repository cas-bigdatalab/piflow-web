package cn.cnic.component.mxGraph.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import cn.cnic.component.mxGraph.entity.MxNodeImage;
import cn.cnic.component.mxGraph.mapper.provider.MxNodeImageMapperProvider;

@Mapper
public interface MxNodeImageMapper {

    /**
     * add addMxNodeImage
     *
     * @param mxNodeImage
     * @return
     */
    @InsertProvider(type = MxNodeImageMapperProvider.class, method = "addMxNodeImage")
    public int addMxNodeImage(MxNodeImage mxNodeImage);

    @SelectProvider(type = MxNodeImageMapperProvider.class,method = "userGetMxNodeImageListByImageType")
    public List<MxNodeImage> userGetMxNodeImageListByImageType(String username, String imageType);



}
