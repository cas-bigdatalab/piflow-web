package cn.cnic.domain.custom;


import cn.cnic.component.flow.model.Flow;
import cn.cnic.repository.process.ProcessGroupJpaRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Component
public class ProcessAndProcessGroupDomain {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private ProcessGroupJpaRepository processGroupJpaRepository;

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

    public Page<Map<String,Object>> getProcessAndProcessGroupListPage(int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crt_dttm"));
        return processGroupJpaRepository.getProcessAndProcessGroupListPage(null == param ? "" : param, pageRequest);
    }
    public Page<Map<String,Object>> getProcessAndProcessGroupListPageByUser(String username,int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crt_dttm"));
        return processGroupJpaRepository.getProcessAndProcessGroupListPageByUser(username, null == param ? "" : param, pageRequest);
    }

}
