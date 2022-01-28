package cn.cnic.component.system.service;

import org.springframework.stereotype.Service;


@Service
public interface ILogHelperService {

    public void logAuthSucceed(String action, String result);
    public void logAdmin(Integer type, String action, Boolean succeed, String result, String comment);
}
