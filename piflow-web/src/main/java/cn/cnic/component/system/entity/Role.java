package cn.cnic.component.system.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Role extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Long parentId;
    private String code; //对应角色英文名大写
    private String name; //对应角色中文名
    private String description;
}
