package cn.cnic.component.dataProduct.mapper;

import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.dataProduct.mapper.provider.DataProductTypeMapperProvider;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.mapper.provider.FlowMapperProvider;
import cn.cnic.component.flow.vo.FlowVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface DataProductTypeMapper {
    @Select("select * from data_product_type where id = #{id}")
    DataProductType getById(@Param("id") Long id);

    @Select("select * from data_product_type where parent_id = #{parentId}")
    List<DataProductType> getByParentId(@Param("parentId") Long parentId);

    @Select("select * from data_product_type where name = #{name} and parent_id = #{parentId} and enable_flag = 1")
    DataProductType getByNameAndParentId(@Param("name") String name, @Param("parentId") Long parentId);

    @InsertProvider(type = DataProductTypeMapperProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID() AS id", keyProperty = "id", keyColumn = "id", before = false, resultType = Long.class)
    Long insert(DataProductType dataProductType);

    /**
     * @param dataProductType:
     * @return int
     * @author tianyao
     * @description update DataProductType
     * @date 2024/2/19 14:23
     */
    @UpdateProvider(type = DataProductTypeMapperProvider.class, method = "update")
    int update(DataProductType dataProductType);

    /**
     * @param id:
     * @return int
     * @author tianyao
     * @description 逻辑删除
     * @date 2024/2/19 15:15
     */
    @Update("update data_product_type set enable_flag = 0 where id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("select dpt.*, CAST(file.id AS CHAR) as fileId, file.file_name as fileName, file.file_path as filePath from data_product_type as dpt " +
            "left join file on file.associate_id = CAST(dpt.id AS CHAR) and file.associate_type = 0 and file.enable_flag = 1 " +
            "where dpt.enable_flag = 1 order by dpt.last_update_dttm desc")
    List<DataProductType> getAllWithNoLogin();

    @Select("select pt.*, pta.state as state, CAST(file.id AS CHAR) as fileId, file.file_name as fileName, file.file_path as filePath from data_product_type as pt "
            + "left join file on file.associate_id = CAST(pt.id AS CHAR) and file.associate_type = 0 and file.enable_flag = 1 "
            + "left join product_type_associate as pta on pt.id = pta.product_type_id and pta.associate_type = 2 and pta.associate_id = #{username} "
            + "where pt.enable_flag = 1 order by pt.last_update_dttm desc"
    )
    List<DataProductType> getAll(@Param("username") String userName);

    @Select("select * from product_type_associate where product_type_id = #{typeId} and associate_type = 2 and associate_id = #{username}")
    ProductTypeAssociate getAssociateByTypeIdAndUserName(@Param("typeId") Long typeId, @Param("username") String username);

    @InsertProvider(type = DataProductTypeMapperProvider.class, method = "insertAssociate")
    int insertAssociate(ProductTypeAssociate associate);

    @Update("update product_type_associate set state = #{preference} where id = #{id}")
    int updatePreference(@Param("id") Long id, @Param("preference") Integer preference);

    @Select("select * from product_type_associate where associate_id = #{associateId}")
    ProductTypeAssociate getAssociateByAssociateId(@Param("associateId") String associateId);

    @UpdateProvider(type = DataProductTypeMapperProvider.class, method = "updateAssociate")
    int updateAssociate(ProductTypeAssociate associate);

    @Delete("<script>"
            + "delete from product_type_associate where associate_id in "
            + "<foreach collection='list' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    int deleteBatchByAssociateId(List<String> associateIds);

    @Delete("delete from product_type_associate where associate_id = #{associateId}")
    int deleteByAssociateId(@Param("associateId") String associateId);

}
