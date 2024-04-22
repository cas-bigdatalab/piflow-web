package cn.cnic.component.visual.service.impl;

import cn.cnic.base.utils.PageHelperUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.base.vo.BasePageVo;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.visual.entity.ProductTemplateGraphAssoDto;
import cn.cnic.component.visual.mapper.piflowdb.ProductTemplateGraphAssoMapper;
import cn.cnic.component.visual.service.ProductTemplateGraphAssoService;
import cn.cnic.component.visual.util.ResponseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductTemplateGraphAssoServiceImpl implements ProductTemplateGraphAssoService {

    private final ProductTemplateGraphAssoMapper productTemplateGraphAssoMapper;

    @Autowired
    public ProductTemplateGraphAssoServiceImpl(ProductTemplateGraphAssoMapper productTemplateGraphAssoMapper) {
        this.productTemplateGraphAssoMapper = productTemplateGraphAssoMapper;
    }

    @Override
    public ResponseResult addProductTemplateGraphAsso(ProductTemplateGraphAssoDto productTemplateGraphAssoDto) {
        int insert = productTemplateGraphAssoMapper.insert(productTemplateGraphAssoDto);
        long id = productTemplateGraphAssoDto.getId();
        if (insert == 1) {
            return ResponseResult.success(id);
        } else {
            return ResponseResult.error("写入关联表vis_product_template_graph_asso失败!");
        }
    }

    @Override
    public ProductTemplateGraphAssoDto selectById(String id) {
        return productTemplateGraphAssoMapper.selectById(id);
    }

    @Override
    public ResponseResult selectByUsername(BasePageVo basePageVo) {
        String username = SessionUserUtil.getCurrentUsername();
        Page<ProductTemplateGraphAssoDto> page = new Page<>(basePageVo.getPage(), basePageVo.getLimit());

        QueryWrapper<ProductTemplateGraphAssoDto> wrapper = new QueryWrapper<>();
        wrapper.eq("owner", username);
        Page<ProductTemplateGraphAssoDto> page1 = productTemplateGraphAssoMapper.selectPage(page, wrapper);
        int total = (int)page1.getTotal();
        List<ProductTemplateGraphAssoDto> data = page1.getRecords();

        return ResponseResult.success(data,total);
    }

}
