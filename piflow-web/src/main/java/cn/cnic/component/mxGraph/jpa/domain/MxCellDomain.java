package cn.cnic.component.mxGraph.jpa.domain;

import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.jpa.repository.MxCellJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Component
public class MxCellDomain {

    @Autowired
    private MxCellJpaRepository mxCellJpaRepository;

    private Specification<MxCell> addEnableFlagParam() {
        Specification<MxCell> specification = new Specification<MxCell>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<MxCell> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<MxCell> addParam(String key, String value) {
        Specification<MxCell> specification = new Specification<MxCell>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<MxCell> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get(key) means to get the name of the key field
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
        return specification;
    }

    public MxCell getMxCellById(String id) {
        MxCell mxCell = null;
        List<MxCell> mxCells = mxCellJpaRepository.findAll(Specification.where(addEnableFlagParam()).and(addParam("id", id)));
        if (null != mxCells && mxCells.size() == 1) {
            mxCell = mxCells.get(0);
        }
        return mxCell;
    }

    public MxCell saveOrUpdate(MxCell mxCell) {
        return mxCellJpaRepository.save(mxCell);
    }

    public List<MxCell> saveOrUpdate(List<MxCell> mxCell) {
        return mxCellJpaRepository.saveAll(mxCell);
    }

    public int updateEnableFlagById(String id, boolean enableFlag, String username) {
        MxCell mxCell = mxCellJpaRepository.getOne(id);
        if (null == mxCell) {
            return 0;
        }
        mxCell.setEnableFlag(enableFlag);
        mxCell.setLastUpdateDttm(new Date());
        mxCell.setLastUpdateUser(username);
        mxCellJpaRepository.save(mxCell);
        return 1;
    }

    public Integer getMaxPageIdByMxGraphModelId(String mxGraphModelId) {
        return mxCellJpaRepository.getMaxPageIdByMxGraphModelId(mxGraphModelId);
    }
}
