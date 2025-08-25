package cn.cnic.component.schedule.dto;

import cn.cnic.common.Eunm.MessageProtocol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageConfigDTO {
    private Long id;
    private MessageProtocol protocol;
    private String properties;
}