package cn.cnic.controller.requestVo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSpaceListRequestVo {
    private String dsAppId;
    private String dsSign;
    private String dsEmail;
    private String dsRemote;
    private String dsSpaceId;
    private String hash;
}
