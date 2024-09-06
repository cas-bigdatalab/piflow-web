package cn.cnic.component.dataProduct.mapper;

import cn.cnic.component.dataProduct.entity.DataProduct;
import cn.cnic.component.dataProduct.mapper.provider.DataProductMapperProvider;
import cn.cnic.component.dataProduct.vo.DataProductVo;
import cn.cnic.component.dataProduct.vo.ProductUserVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface DataProductMapper {

    @InsertProvider(type = DataProductMapperProvider.class, method = "insert")
    int insert(DataProduct dataProduct);

    @Insert("<script>"
            + "insert into data_product (id, process_id, property_id, property_name, dataset_url, `name`, "
            + "description, permission, keyword, sdPublisher, email, `state`, opinion, down_reason, "
            + "is_share, doi_id, cstr_id, subject_type_id, time_range, spacial_range, dataset_size, dataset_type, associate_id, "
            + "crt_dttm, crt_user, enable_flag, last_update_dttm, last_update_user, version, ``, bak2, bak3) values "
            + "<foreach collection='list' item='item' index='index' separator=', '>"
            + "(#{item.id}, #{item.processId}, #{item.propertyId}, #{item.propertyName}, #{item.datasetUrl}, #{item.name}, "
            + "#{item.description}, #{item.permission}, #{item.keyword}, #{item.sdPublisher}, #{item.email}, #{item.state}, #{item.opinion}, #{item.downReason}, "
            + "#{item.isShare}, #{item.doiId}, #{item.cstrId}, #{item.subjectTypeId}, #{item.timeRange}, #{item.spacialRange}, #{item.datasetSize}, #{item.datasetType}, "
            + "#{item.associateId}, "
            + "#{item.crtDttmStr}, #{item.crtUser}, #{item.enableFlagNum}, #{item.lastUpdateDttmStr}, #{item.lastUpdateUser}, 0, #{item.company}, #{item.bak2}, #{item.bak3})"
            + "</foreach>"
            + "</script>")
    int addBatch(List<DataProduct> dataProducts);

    @Update("update data_product set state = #{state}, last_update_dttm = #{lastUpdateDttmStr} where process_id = #{processId} and enable_flag = 1")
    int updateStateByProcessIdWithNoChangeUser(@Param("processId") String processId, @Param("state") Integer state, @Param("lastUpdateDttmStr") String lastUpdateDttmStr);

    @Select("select CAST(dp.id AS CHAR) as id, dp.*, pt.product_type_id as productTypeId, pt.product_type_name as productTypeName from data_product as dp "
            + "left join product_type_associate as pt on pt.associate_id = CAST(dp.id AS CHAR) "
            + "where process_id = #{processId} and enable_flag = 1")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT),
            @Result(column = "id", property = "coverFile", javaType = String.class, jdbcType = JdbcType.BIGINT, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductCoverType", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "file", javaType = String.class, jdbcType = JdbcType.BIGINT, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductType", fetchType = FetchType.LAZY))
    })
    List<DataProductVo> getListByProcessId(@Param("processId") String processId);

    @UpdateProvider(type = DataProductMapperProvider.class, method = "update")
    int update(DataProduct dataProduct);

    @UpdateProvider(type = DataProductMapperProvider.class, method = "updatePermission")
    int updatePermission(DataProduct dataProduct);

    @UpdateProvider(type = DataProductMapperProvider.class, method = "delete")
    int delete(DataProduct dataProduct);

    @Select("select * from data_product where id = #{id} and enable_flag = 1")
    DataProduct getById(@Param("id") Long id);

    @Select("select * from data_product where id = #{id} and enable_flag = 1")
    DataProduct getBasicInfoById(@Param("id") Long id);

    @UpdateProvider(type = DataProductMapperProvider.class, method = "down")
    int down(DataProduct dataProduct);

    @Select("<script>"
            + "select CAST(dp.id as CHAR) as id, dp.*, pt.product_type_id as productTypeId, pt.product_type_name as productTypeName from data_product as dp"
            + "<if test=\"productTypeId != null \">"
            + " inner join product_type_associate as pta on pta.associate_id = CAST(dp.id AS CHAR) and pta.product_type_id = #{productTypeId} and (pta.state = 1 or pta.state = 3) "
            + "</if>"
            + "left join product_type_associate as pt on pt.associate_id = CAST(dp.id AS CHAR) "
            + "<where>"
            + " dp.state = 5 and crt_user = #{username} and dp.enable_flag = 1 "
            + "<if test=\"keyword != null and keyword != '' \">"
            + "and (dp.keyword like CONCAT('%', #{keyword}, '%') or dp.name like CONCAT('%', #{keyword}, '%'))"
            + "</if>"
            + "</where>"
            + "</script>")
    @Results({
//            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT),
            @Result(column = "associate_id", property = "associateId", javaType = String.class, jdbcType = JdbcType.CHAR),
//            @Result(column = "id", property = "productTypeId", javaType= Long.class, jdbcType= JdbcType.BIGINT, typeHandler=cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.dataProduct.mapper.DataProductTypeMapper.getAssociateByAssociateId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "coverFile", javaType = String.class, jdbcType = JdbcType.BIGINT, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductCoverType", fetchType = FetchType.LAZY)),
//            @Result(column = "id", property = "file", javaType = String.class, jdbcType = JdbcType.BIGINT, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductType", fetchType = FetchType.LAZY))
    })
    List<DataProductVo> getByPageForHomePage(@Param("productTypeId") Long productTypeId, @Param("keyword") String keyword, @Param("username") String username);

    @Select("<script>"
            + "select CAST(dp.id as CHAR) as id, dp.*, pt.product_type_id as productTypeId, pt.product_type_name as productTypeName from data_product as dp "
            + "<if test=\"productTypeId != null \">"
            + "inner join product_type_associate as pta on pta.associate_id = dp.id and pta.product_type_id = #{productTypeId} "
            + "</if>"
            + "left join product_type_associate as pt on pt.associate_id = CAST(dp.id AS CHAR) "
            + "<where>"
            + "<if test=\"sdPublisher != null and sdPublisher != '' \">"
            + " and dp.sdPublisher = #{sdPublisher} "
            + "</if>"
            + "<if test=\"keyword != null and keyword != '' \">"
            + " and dp.keyword like CONCAT('%', #{keyword}, '%') "
            + "</if>"
            + "<if test=\"state != null \">"
            + " and dp.state = #{state} "
            + "</if>"
            + " and dp.enable_flag = 1 "
            + "</where>"
            + "</script>")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT),
//            @Result(column = "id", property = "productTypeId", javaType= Long.class, jdbcType= JdbcType.BIGINT, typeHandler=cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.dataProduct.mapper.DataProductTypeMapper.getAssociateByAssociateId", fetchType = FetchType.LAZY)),
//            @Result(column = "id", property = "productTypeName", javaType= Long.class, jdbcType= JdbcType.BIGINT, typeHandler=cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.dataProduct.mapper.DataProductTypeMapper.getAssociateTypeNameByAssociateId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "coverFile", javaType = String.class, jdbcType = JdbcType.BIGINT, typeHandler = cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductCoverType", fetchType = FetchType.LAZY)),
//            @Result(column = "id", property = "file", javaType = String.class, jdbcType = JdbcType.BIGINT, typeHandler = cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductType", fetchType = FetchType.LAZY))
    })
    List<DataProductVo> getByPageForPublishing(DataProductVo dataProductVo);

    //管理员 数据产品发布管理菜单列表，显示别人待审核的，以及自己已发布的,被拒绝发布的，已下架的,可搜索标题、状态、创建人、单位
    @Select("<script>"
            + "select CAST(dp.id as CHAR) as id, dp.*, pt.product_type_id as productTypeId, pt.product_type_name as productTypeName from data_product as dp "
            + "<if test=\"productTypeId != null \">"
            + "inner join product_type_associate as pta on pta.associate_id = dp.id and pta.product_type_id = #{productTypeId} "
            + "</if>"
            + "left join product_type_associate as pt on pt.associate_id = CAST(dp.id AS CHAR) "
            + "<where>"
            + "<if test=\"sdPublisher != null and sdPublisher != '' \">"
            + " and dp.sdPublisher = #{sdPublisher} "
            + "</if>"
            + "<if test=\"keyword != null and keyword != '' \">"
            + " and dp.keyword like CONCAT('%', #{keyword}, '%') "
            + "</if>"
            + "<if test=\"company != null and company != '' \">"
            + " and dp.company like CONCAT('%', #{company}, '%') "
            + "</if>"
            + "<if test=\"name != null and name != '' \">"
            + " and dp.name like CONCAT('%', #{name}, '%') "
            + "</if>"
            + "<if test=\"crtUser != null and crtUser != '' \">"
            + " and dp.crt_user like CONCAT('%', #{crtUser}, '%') "
            + "</if>"
            + "<choose>"
            + "    <when test=\"state != null\">"
            + "        and dp.state = #{state} "
            + "    </when>"
            + "    <otherwise>"
            + "        and dp.state in (4, 5, 6, 7) "
            + "    </otherwise>"
            + "</choose>"
            // + " and ((crt_user != #{crtUser} and dp.state = 4) or (crt_user = #{crtUser} and dp.state in (5, 6, 7)))"
            + " and dp.enable_flag = 1 "
            + "</where>"
            + "</script>")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT),
            @Result(column = "id", property = "coverFile", javaType = String.class, jdbcType = JdbcType.BIGINT, typeHandler = cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductCoverType", fetchType = FetchType.LAZY)),
//            @Result(column = "id", property = "file", javaType = String.class, jdbcType = JdbcType.BIGINT, typeHandler = cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductType", fetchType = FetchType.LAZY))
    })
    List<DataProductVo> getByPageForPublishingWithAdmin(DataProductVo dataProductVo);

    //数据产品发布者 数据产品发布管理菜单列表，显示自己待审核的、已发布的,被拒绝发布的，已下架的, 可搜索标题、状态
    @Select("<script>"
            + "select CAST(dp.id as CHAR) as id, dp.*, pt.product_type_id as productTypeId, pt.product_type_name as productTypeName from data_product as dp "
            + "<if test=\"productTypeId != null \">"
            + "inner join product_type_associate as pta on pta.associate_id = dp.id and pta.product_type_id = #{productTypeId} "
            + "</if>"
            + "left join product_type_associate as pt on pt.associate_id = CAST(dp.id AS CHAR) "
            + "<where>"
            + "<if test=\"keyword != null and keyword != '' \">"
            + " and dp.keyword like CONCAT('%', #{keyword}, '%') "
            + "</if>"
            + "<if test=\"name != null and name != '' \">"
            + " and dp.name like CONCAT('%', #{name}, '%') "
            + "</if>"
            + "<choose>"
            + "    <when test=\"state != null\">"
            + "        and dp.state = #{state} "
            + "    </when>"
            + "    <otherwise>"
            + "        and dp.state in (4, 5, 6, 7) "
            + "    </otherwise>"
            + "</choose>"
            //+ " and crt_user = #{crtUser} and dp.state in (4, 5, 6, 7) "
            + " and dp.enable_flag = 1 "
            + "</where>"
            + "</script>")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT),
            @Result(column = "id", property = "coverFile", javaType = String.class, jdbcType = JdbcType.BIGINT, typeHandler = cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductCoverType", fetchType = FetchType.LAZY)),
//            @Result(column = "id", property = "file", javaType = String.class, jdbcType = JdbcType.BIGINT, typeHandler = cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductType", fetchType = FetchType.LAZY))
    })
    List<DataProductVo> getByPageForPublishingWithSdPublisher(DataProductVo dataProductVo);



    //台站管理员 数据产品发布管理菜单列表，显示本台站待审核的、已发布的,被拒绝发布的，已下架的, 可搜索标题、状态、创建人
    @Select("<script>"
            + "select CAST(dp.id as CHAR) as id, dp.*, pt.product_type_id as productTypeId, pt.product_type_name as productTypeName from data_product as dp "
            + "<if test=\"productTypeId != null \">"
            + "inner join product_type_associate as pta on pta.associate_id = dp.id and pta.product_type_id = #{productTypeId} "
            + "</if>"
            + "left join product_type_associate as pt on pt.associate_id = CAST(dp.id AS CHAR) "
            + "<where>"
            + "<if test=\"keyword != null and keyword != '' \">"
            + " and dp.keyword like CONCAT('%', #{keyword}, '%') "
            + "</if>"
            + "<if test=\"name != null and name != '' \">"
            + " and dp.name like CONCAT('%', #{name}, '%') "
            + "</if>"
            + "<if test=\"crtUser != null and crtUser != '' \">"
            + " and dp.crt_user like CONCAT('%', #{crtUser}, '%') "
            + "</if>"
            + "<choose>"
            + "    <when test=\"state != null\">"
            + "        and dp.state = #{state} "
            + "    </when>"
            + "    <otherwise>"
            + "        and dp.state in (4, 5, 6, 7) "
            + "    </otherwise>"
            + "</choose>"
            + " and dp.company = #{company} and dp.enable_flag = 1 "
            + "</where>"
            + "</script>")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT),
            @Result(column = "id", property = "coverFile", javaType = String.class, jdbcType = JdbcType.BIGINT, typeHandler = cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductCoverType", fetchType = FetchType.LAZY)),
//            @Result(column = "id", property = "file", javaType = String.class, jdbcType = JdbcType.BIGINT, typeHandler = cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductType", fetchType = FetchType.LAZY))
    })
    List<DataProductVo> getByPageForPublishingWithORSAdmin(DataProductVo dataProductVo);

    @Select("<script>"
            + "select CAST(du.id as CHAR) as id, CAST(du.product_id AS CHAR) as productId, du.* from product_user as du "
            + "inner join data_product as dp on dp.id = du.product_id and dp.crt_user = #{username}"
            + "<where>"
            + " du.state != 0 "
            + "</where>"
            + "</script>")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT),
            @Result(column = "product_id", property = "productId", javaType = String.class, jdbcType = JdbcType.BIGINT),
    })
    List<ProductUserVo> getByPageForPermission(ProductUserVo productUserVo, String username);

    @Select("<script>"
            + "select CAST(dp.id as CHAR) as id, dp.*, pt.product_type_id as productTypeId, pt.product_type_name as productTypeName from data_product as dp "
            + "left join product_type_associate as pt on pt.associate_id = CAST(dp.id AS CHAR) "
            + "<where>"
            + " dp.id = #{productId} and dp.enable_flag = 1 "
            + "</where>"
            + "</script>")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT),
            @Result(column = "id", property = "coverFile", javaType = String.class, jdbcType = JdbcType.BIGINT, typeHandler = cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductCoverType", fetchType = FetchType.LAZY)),
//            @Result(column = "id", property = "file", javaType = String.class, jdbcType = JdbcType.BIGINT, typeHandler = cn.cnic.common.typeHandler.LongToStringTypeHandler.class, one = @One(select = "cn.cnic.component.system.mapper.FileMapper.getByAssociateIdAndDataProductType", fetchType = FetchType.LAZY))
    })
    DataProductVo getFullInfoById(@Param("productId") Long id);

    @Select("select id, name from data_product where name = #{name} and enable_flag = 1")
    List<DataProduct> getListByName(@Param("name") String name);
    @Select("select id, name from data_product where name = #{name} and id != #{id} and enable_flag = 1")
    List<DataProduct> getListByNameAndId(@Param("id") String id, @Param("name") String name);

    @Select("<script>"
            + "select * from data_product "
            + "<where>"
            + " id in "
            + "<foreach collection='ids' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "</where>"
            + "</script>")
    List<DataProduct> getByIds(@Param("ids") String[] ids);

    @Update("<script>"
            + "update data_product set enable_flag = 0 "
            + "<where>"
            + " id in "
            + "<foreach collection='ids' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "</where>"
            + "</script>")
    int updateEnableFlagToFalse(@Param("ids") String[] ids);
}
