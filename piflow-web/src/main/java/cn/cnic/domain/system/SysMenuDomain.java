package cn.cnic.domain.system;

import cn.cnic.component.system.model.SysMenu;
import cn.cnic.repository.system.SysMenuJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Component
public class SysMenuDomain {

    @Autowired
    private SysMenuJpaRepository sysMenuJpaRepository;

    private Specification<SysMenu> addEnableFlagParam() {
        Specification<SysMenu> specification = new Specification<SysMenu>() {
			private static final long serialVersionUID = 1L;

			@Override
            public Predicate toPredicate(Root<SysMenu> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                //root.get("enableFlag") means to get the field name of enableFlag
                return criteriaBuilder.equal(root.get("enableFlag"), 1);
            }
        };
        return specification;
    }

    public SysMenu getSysMenuById(String id) {
        SysMenu sysMenu = null;
        Optional<SysMenu> sysMenuOptional = sysMenuJpaRepository.findById(id);
        if ("Optional.empty" != sysMenuOptional.toString()) {
            sysMenu = sysMenuOptional.get();
        }
        if (null != sysMenu && !sysMenu.getEnableFlag()) {
            sysMenu = null;
        }
        return sysMenu;
    }

    public List<SysMenu> getSysMenuList() {
        return sysMenuJpaRepository.findAll(addEnableFlagParam());
    }

    public SysMenu saveOrUpdate(SysMenu sysMenu) {
        return sysMenuJpaRepository.save(sysMenu);
    }

    public List<SysMenu> saveOrUpdate(List<SysMenu> sysMenuList) {
        return sysMenuJpaRepository.saveAll(sysMenuList);
    }

}
