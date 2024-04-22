package cn.cnic.component.dataProduct.service;

import cn.cnic.component.dataProduct.vo.DataProductVo;
import cn.cnic.component.dataProduct.vo.ProductUserVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

public interface IDataProductService {

    String save(MultipartFile file, DataProductVo dataProductVo);

    String permissionForPublishing(DataProductVo dataProductVo);

    String delete(String id);

    String down(DataProductVo dataProductVo);

    String getByPage(DataProductVo dataProductVo);

    String getByPageForPublishing(DataProductVo dataProductVo);

    String applyPermission(ProductUserVo productUserVo);

    String getByPageForPermission(ProductUserVo productUserVo);

    String permissionForUse(ProductUserVo productUserVo);

    String getById(String id);

    Set<String> getDataSourceListFromProduct(String id, String datasetUrl);

    void downloadDataset(HttpServletResponse response, String dataProductId);
}
