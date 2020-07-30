package cn.cnic.third.vo.stop;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class StopsHubVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mountId;
    private List<ThirdStopsComponentVo> stops;

}
