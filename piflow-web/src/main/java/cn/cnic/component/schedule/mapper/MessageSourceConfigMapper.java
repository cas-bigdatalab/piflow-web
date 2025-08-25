package cn.cnic.component.schedule.mapper;

import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
import org.apache.ibatis.annotations.*;

import java.io.PushbackInputStream;
import java.util.Date;
import java.util.List;

/**
 * 消息源配置Repository接口
 */
@Mapper
public interface MessageSourceConfigMapper {

    /**
     * 插入新的消息源配置
     */
    @Insert("INSERT INTO message_trigger_task_definition (name,type,  protocol, properties, filter_rule_type, filter_rule_json, target_workflow_id, target_workflow_name, context_mapping_json, concurrency_limit, status, creator_id, create_time, update_time) " +
            "VALUES (#{name}, #{type}, #{protocol}, #{properties},  #{filterRuleType}, #{filterRuleJson}, #{targetWorkflowId},#{targetWorkflowName}, #{contextMappingJson}, #{concurrencyLimit}, #{status}, #{creatorId}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(MessageTriggerTaskDefinition config);

    /**
     * 更新消息源配置
     */
    @Update({
        "<script>",
        "UPDATE message_trigger_task_definition",
        "<set>",
        "  <if test='name != null'>name = #{name},</if>",
        "  <if test='type != null'>type = #{type},</if>",
        "  <if test='protocol != null'>protocol = #{protocol},</if>",
        "  <if test='properties != null'>properties = #{properties},</if>",
        "  <if test='filterRuleType != null'>filter_rule_type = #{filterRuleType},</if>",
        "  <if test='filterRuleJson != null'>filter_rule_json = #{filterRuleJson},</if>",
        "  <if test='targetWorkflowId != null'>target_workflow_id = #{targetWorkflowId},</if>",
        "  <if test='targetWorkflowName != null'>target_workflow_name = #{targetWorkflowName},</if>",
        "  concurrency_limit = #{concurrencyLimit},",
        "  <if test='creatorId != null'>creator_id = #{creatorId},</if>",
        "  <if test='status != null'>status = #{status},</if>",
        "  <if test='updateTime != null'>update_time = #{updateTime},</if>",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int update(MessageTriggerTaskDefinition config);

    /**
     * 删除消息源配置
     */
    @Delete("DELETE FROM message_trigger_task_definition WHERE id=#{id}")
    int deleteById(@Param("id") Long id);

    /**
     * 根据ID获取消息源配置
     */
    @Select("SELECT * FROM message_trigger_task_definition WHERE id=#{id}")
    MessageTriggerTaskDefinition getById(@Param("id") Long id);

    /**
     * 根据ID获取并发数量
     */
    @Select("SELECT concurrency_limit FROM message_trigger_task_definition WHERE id=#{id}")
    int getConcurrencyLimitById(@Param("id") Long id);

    @Select({
        "<script>",
        "SELECT * FROM message_trigger_task_definition",
        "<where>",
        "  <if test='search != null and search != \"\"'>",
        "    (name LIKE CONCAT('%', #{search}, '%')",
        "     OR protocol LIKE CONCAT('%', #{search}, '%')",
        "     OR properties LIKE CONCAT('%', #{search}, '%'))",
        "  </if>",
        "</where>",
        "ORDER BY id DESC",
        "LIMIT #{limit} OFFSET #{offset}",
        "</script>"
    })
    List<MessageTriggerTaskDefinition> selectByPageAndParam(
        @Param("offset") int offset,
        @Param("limit") int limit,
        @Param("search") String search
    );

    @Select("SELECT properties FROM message_trigger_task_definition WHERE id=#{id}")
    String getMessageSourcePasswordById(Long id);

    @Update("UPDATE message_trigger_task_definition set status=#{status}, update_time=#{updateTime} where id=#{id}")
    int updateStatusById(Long id, String status, Date updateTime);
}