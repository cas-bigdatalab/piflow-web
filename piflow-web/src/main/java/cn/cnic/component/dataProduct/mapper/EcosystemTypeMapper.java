package cn.cnic.component.dataProduct.mapper;

import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.entity.EcosystemType;
import cn.cnic.component.dataProduct.entity.EcosystemTypeAssociate;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.dataProduct.mapper.provider.DataProductTypeMapperProvider;
import cn.cnic.component.dataProduct.mapper.provider.EcosystemTypeMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EcosystemTypeMapper {

    @InsertProvider(type = EcosystemTypeMapperProvider.class, method = "insert")
    @SelectKey(statement = "SELECT LAST_INSERT_ID() AS id", keyProperty = "id", keyColumn = "id", before = false, resultType = Long.class)
    Long insert(EcosystemType ecosystemType);

    @Select("select * from ecosystem_type where enable_flag = 1")
    List<EcosystemType> getAll();

    @Select("select * from ecosystem_type where id = #{id}")
    EcosystemType getById(@Param("id") Long id);

    @Select("<script>"
            + "select * from ecosystem_type "
            + "<where>"
            + " id in "
            + "<foreach collection='ecosystemTypeIds.split(\",\")' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + " and enable_flag = 1"
            + "</where>"
            + "</script>")
    List<EcosystemType> getByIds(@Param("ecosystemTypeIds") String ecosystemTypeIds);

    /**
     * @param ecosystemType:
     * @return int
     * @author tianyao
     * @description update EcosystemType
     * @date 2024/2/19 14:23
     */
    @UpdateProvider(type = EcosystemTypeMapperProvider.class, method = "update")
    int update(EcosystemType ecosystemType);

    /**
     * @param id:
     * @return int
     * @author tianyao
     * @description 逻辑删除
     * @date 2024/2/19 15:15
     */
    @Update("update ecosystem_type set enable_flag = 0 where id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("select * from ecosystem_type_associate where associate_id = #{associateId}")
    List<EcosystemTypeAssociate> getAssociateByAssociateId(@Param("associateId") String associateId);

    @Select("<script>"
            + "select associate_id from ecosystem_type_associate "
            + "<where>"
            + " associate_type = #{associateType} and"
            + " ecosystem_type_id in "
            + "<foreach collection='ecosystemTypeIds' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "</where>"
            + "</script>")
    List<String> getAssociateByEcosystemTypeIdsAndAssociateType(@Param("ecosystemTypeIds") List<Long> ecosystemTypeIds, @Param("associateType") Integer associateType);

    @InsertProvider(type = EcosystemTypeMapperProvider.class, method = "insertAssociate")
    int insertAssociate(ProductTypeAssociate associate);

    @UpdateProvider(type = EcosystemTypeMapperProvider.class, method = "updateAssociate")
    int updateAssociate(EcosystemTypeAssociate associate);

    @Insert("<script>"
            + "insert into ecosystem_type_associate (ecosystem_type_id, ecosystem_type_name, associate_id, associate_type, bak1, bak2, bak3) values "
            + "<foreach collection='list' item='item' index='index' separator=', '>"
            + "(#{item.ecosystemTypeId}, #{item.ecosystemTypeName}, #{item.associateId}, #{item.associateType}, #{item.bak1}, #{item.bak2}, #{item.bak3})"
            + "</foreach>"
            + "</script>")
    int insertAssociateBatch(List<EcosystemTypeAssociate> associates);

    @Delete("delete from ecosystem_type_associate where associate_id = #{associateId}")
    int deleteByAssociateId(@Param("associateId") String associateId);

    @Delete("<script>"
            + "delete from ecosystem_type_associate where associate_id in "
            + "<foreach collection='list' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    int deleteBatchByAssociateId(List<String> associateIds);
}
