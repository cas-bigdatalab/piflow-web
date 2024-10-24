package cn.cnic.base.vo;

import cn.cnic.base.BaseModelNoId;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BasePageVo extends BaseModelNoId {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer limit;
    private Integer page;


}