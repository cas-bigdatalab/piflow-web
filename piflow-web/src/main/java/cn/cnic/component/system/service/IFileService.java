package cn.cnic.component.system.service;

import cn.cnic.component.dataProduct.vo.DataProductTypeVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface IFileService {

    String uploadFile(MultipartFile file, Integer associateType, String associateId);

    void getFileById(HttpServletResponse response, String id);

    void getFileListByIds(HttpServletResponse response, String ids);
}
