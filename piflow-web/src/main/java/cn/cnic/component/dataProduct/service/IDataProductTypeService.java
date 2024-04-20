package cn.cnic.component.dataProduct.service;

import cn.cnic.component.dataProduct.vo.DataProductTypeVo;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.controller.requestVo.FlowInfoVoRequestAdd;
import cn.cnic.controller.requestVo.FlowInfoVoRequestUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDataProductTypeService {

    String save(MultipartFile file, DataProductTypeVo dataProductTypeVo);

    String delete(Long id);

    String get();

    String getWithEcosystemType();

    String preference(Long id, Integer preference);

}
