package cn.cnic.component.process.service;

import cn.cnic.component.process.vo.ErrorLogMappingVo;

public interface IErrorLogMappingService {

    String save(ErrorLogMappingVo errorLogMappingVo);

    String delete(String id);

    String getByPage(ErrorLogMappingVo errorLogMappingVo);

    String getById(String id);
}
