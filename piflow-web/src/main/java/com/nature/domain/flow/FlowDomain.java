package com.nature.domain.flow;

import com.nature.component.flow.model.Flow;
import com.nature.repository.flow.FlowJpaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class FlowDomain {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private FlowJpaRepository flowJpaRepository;

    public Flow getFlowById(String id) {
        Flow flow = flowJpaRepository.getOne(id);
        if (null != flow && !flow.getEnableFlag()) {
            flow = null;
        }
        return flow;
    }

    public Flow saveOrUpdate(Flow flow) {
        return flowJpaRepository.save(flow);
    }

    public List<Flow> saveOrUpdate(List<Flow> flowList) {
        return flowJpaRepository.saveAll(flowList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return flowJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public Flow getFlowByPageId(String fid, String pageId) {
        return flowJpaRepository.getFlowByPageId(fid, pageId);
    }

    public String getFlowIdByNameAndFlowGroupId(String fid, String flowName) {
        return flowJpaRepository.getFlowIdByNameAndFlowGroupId(fid, flowName);
    }

    public String getMaxStopPageIdByFlowGroupId(String flowGroupId) {
        return flowJpaRepository.getMaxStopPageIdByFlowGroupId(flowGroupId);
    }

    /**
     * Query all flow paging queries
     *
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<Flow> getFlowListParam(String param) {
        String sqlStr = "select 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("and is_example = 0 ");
        strBuf.append("and fk_flow_group_id is null ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("and ( ");
            strBuf.append("name like '%" + param + "%' ");
            strBuf.append("or description like '%" + param + "%' ");
            strBuf.append(") ");
        }
        //strBuf.append(SqlUtils.addQueryByUserRole(true, false));
        strBuf.append("order by crt_dttm desc ");
        sqlStr = strBuf.toString();
        Query query = entityManager.createNativeQuery(sqlStr,Flow.class);
        return query.getResultList();
    }

}
