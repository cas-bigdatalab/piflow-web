package cn.cnic.component.mxGraph.jpa.domain;

import cn.cnic.component.mxGraph.entity.MxNodeImage;
import cn.cnic.component.mxGraph.jpa.repository.MxNodeImageJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class MxNodeImageDomain {

    @Autowired
    private MxNodeImageJpaRepository mxNodeImageJpaRepository;

    private Specification<MxNodeImage> addEnableFlagParam() {
        Specification<MxNodeImage> specification = new Specification<MxNodeImage>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<MxNodeImage> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    private Specification<MxNodeImage> addParam(String key, String value) {
        Specification<MxNodeImage> specification = new Specification<MxNodeImage>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<MxNodeImage> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get(key) means to get the name of the key field
                return criteriaBuilder.equal(root.get(key), value);
            }
        };
        return specification;
    }

    public MxNodeImage saveOrUpdate(MxNodeImage mxNodeImage) {
        return mxNodeImageJpaRepository.save(mxNodeImage);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return mxNodeImageJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public MxNodeImage getMxNodeImageById(String id) {
        MxNodeImage mxNodeImage = null;
        List<MxNodeImage> mxNodeImages = mxNodeImageJpaRepository.findAll(Specification.where(addEnableFlagParam()).and(addParam("id", id)));
        if (null != mxNodeImages && mxNodeImages.size() == 1) {
            mxNodeImage = mxNodeImages.get(0);
        }
        return mxNodeImage;
    }

    public List<MxNodeImage> userGetMxNodeImageList(String username) {
        Specification<MxNodeImage> where = Specification.where(addEnableFlagParam().and(addParam("crtUser", username)));
        return mxNodeImageJpaRepository.findAll(where, Sort.by(Sort.Direction.DESC, "lastUpdateDttm"));

    }

    public List<MxNodeImage> adminGetMxNodeImageList() {
        Specification<MxNodeImage> where = Specification.where(addEnableFlagParam());
        return mxNodeImageJpaRepository.findAll(where, Sort.by(Sort.Direction.DESC, "lastUpdateDttm"));

    }

    public List<MxNodeImage> userGetMxNodeImageListByImageType(String username,String imageType) {
        Specification<MxNodeImage> where = Specification.where(addEnableFlagParam().and(addParam("crtUser", username)).and(addParam("imageType", imageType)));
        return mxNodeImageJpaRepository.findAll(where, Sort.by(Sort.Direction.DESC, "lastUpdateDttm"));
    }

    public List<MxNodeImage> adminGetMxNodeImageListByImageType(String imageType) {
        Specification<MxNodeImage> where = Specification.where(addEnableFlagParam().and(addParam("imageType", imageType)));
        return mxNodeImageJpaRepository.findAll(where, Sort.by(Sort.Direction.DESC, "lastUpdateDttm"));
    }

}
