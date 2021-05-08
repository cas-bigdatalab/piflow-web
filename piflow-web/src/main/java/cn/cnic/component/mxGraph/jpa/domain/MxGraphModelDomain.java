package cn.cnic.component.mxGraph.jpa.domain;

import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.jpa.repository.MxGraphModelJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class MxGraphModelDomain {

    @Autowired
    private MxGraphModelJpaRepository mxGraphModelJpaRepository;

    private Specification<MxGraphModel> addEnableFlagParam() {
        Specification<MxGraphModel> specification = new Specification<MxGraphModel>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<MxGraphModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<MxGraphModel> addParam(String key, String value) {
        Specification<MxGraphModel> specification = new Specification<MxGraphModel>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<MxGraphModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get(key) means to get the name of the key field
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
        return specification;
    }

    public MxGraphModel getMxGraphModelById(String id) {
        MxGraphModel mxGraphModel = null;
        List<MxGraphModel> mxGraphModels = mxGraphModelJpaRepository.findAll(Specification.where(addEnableFlagParam()).and(addParam("id", id)));
        if (null != mxGraphModels && mxGraphModels.size() == 1) {
            mxGraphModel = mxGraphModels.get(0);
        }
        return mxGraphModel;
    }

    public MxGraphModel saveOrUpdate(MxGraphModel mxGraphModel) {
        return mxGraphModelJpaRepository.save(mxGraphModel);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return mxGraphModelJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public MxGraphModel getMxGraphModelByFlowId(String flowId) {
        return mxGraphModelJpaRepository.getMxGraphModelByFlowGroupId(flowId);
    }


}
