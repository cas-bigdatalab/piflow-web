package cn.cnic.component.mxGraph.jpa.domain;

import cn.cnic.component.mxGraph.entity.MxGeometry;
import cn.cnic.component.mxGraph.jpa.repository.MxGeometryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class MxGeometryDomain {

    @Autowired
    private MxGeometryJpaRepository mxGeometryJpaRepository;

    private Specification<MxGeometry> addEnableFlagParam() {
        Specification<MxGeometry> specification = new Specification<MxGeometry>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<MxGeometry> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<MxGeometry> addParam(String key, String value) {
        Specification<MxGeometry> specification = new Specification<MxGeometry>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<MxGeometry> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get(key) means to get the name of the key field
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
        return specification;
    }

    public MxGeometry getMxGeometryById(String id) {
        MxGeometry mxGeometry = null;
        List<MxGeometry> mxGeometrys = mxGeometryJpaRepository.findAll(Specification.where(addEnableFlagParam()).and(addParam("id", id)));
        if (null != mxGeometrys && mxGeometrys.size() == 1) {
            mxGeometry = mxGeometrys.get(0);
        }
        return mxGeometry;
    }

    public MxGeometry saveOrUpdate(MxGeometry mxGeometry) {
        return mxGeometryJpaRepository.save(mxGeometry);
    }

    public List<MxGeometry> saveOrUpdate(List<MxGeometry> mxGeometry) {
        return mxGeometryJpaRepository.saveAll(mxGeometry);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return mxGeometryJpaRepository.updateEnableFlagById(id, enableFlag);
    }
}
