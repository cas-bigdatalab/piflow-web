package cn.cnic.component.schedule.listener;

import cn.cnic.common.Eunm.MessageProtocol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class IMessageConfig {
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Integer connectionTimeout;


    public MessageProtocol getProtocolType() // 返回配置所属的协议类型
    {
        return null;
    }
}
