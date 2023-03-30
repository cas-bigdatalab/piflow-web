package cn.cnic.component.stopsComponent.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 上传算法包的具体文件记录表（stops_hub_file_record）
 *
 */
@Setter
@Getter
public class StopsHubFileRecord {

    private static final long serialVersionUID = 1L;

    private String id;
    private String fileName;        //file name
    private String filePath;        //file path
    private String stopsHubId;      //sparkHub id
    private String dockerImagesName;    //image name
    private Date crtDttm = new Date();           //create time

    private Boolean isComponent = false;  //is component(not save in db,get for link select,used for component list display )
    //TODO 是否要加上
    private StopsComponent stopsComponent;

}
