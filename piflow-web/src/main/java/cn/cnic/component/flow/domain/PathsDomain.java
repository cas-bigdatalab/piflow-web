package cn.cnic.component.flow.domain;

import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.mapper.PathsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class PathsDomain {

    @Autowired
    private PathsMapper pathsMapper;

    public List<Paths> getPaths(String flowId, String pageId, String from, String to) {
        return pathsMapper.getPaths(flowId, pageId, from, to);
    }


}