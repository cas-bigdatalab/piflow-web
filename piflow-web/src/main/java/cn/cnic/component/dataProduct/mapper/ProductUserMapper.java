package cn.cnic.component.dataProduct.mapper;

import cn.cnic.component.dataProduct.entity.ProductUser;
import cn.cnic.component.dataProduct.mapper.provider.ProductUserMapperProvider;
import cn.cnic.component.dataProduct.vo.DataProductVo;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.mapper.provider.FlowMapperProvider;
import cn.cnic.component.flow.vo.FlowVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProductUserMapper {
    @Select("select * from product_user where product_id = #{dataProductId} and user_name = #{username} and state != 0 order by last_update_dttm DESC limit 1")
    ProductUser getOneByUserName(@Param("dataProductId") Long dataProductId, @Param("username") String username);
    @Select("select * from product_user where id = #{id}")
    ProductUser getById(Long id);
    @InsertProvider(type = ProductUserMapperProvider.class, method = "insert")
    int insert(ProductUser insertApply);

    @Update("update product_user set state = #{state}, opinion = #{opinion}, last_update_dttm = #{lastUpdateDttmStr}, last_update_user = #{lastUpdateUser} "
            + "where id = #{id}")
    int permissionForUse(ProductUser productUser);
}
