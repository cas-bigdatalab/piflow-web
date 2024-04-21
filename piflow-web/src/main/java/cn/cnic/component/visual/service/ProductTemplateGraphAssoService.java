package cn.cnic.component.visual.service;

import cn.cnic.base.vo.BasePageVo;
import cn.cnic.component.visual.entity.ProductTemplateGraphAssoDto;
import cn.cnic.component.visual.util.ResponseResult;

import java.util.List;

public interface ProductTemplateGraphAssoService {

    ResponseResult addProductTemplateGraphAsso(ProductTemplateGraphAssoDto productTemplateGraphAssoDto);

    ProductTemplateGraphAssoDto selectById(String id);

    List<ProductTemplateGraphAssoDto> selectByUsername(BasePageVo basePageVo);
}
