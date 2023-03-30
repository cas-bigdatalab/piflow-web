package cn.cnic.component.stopsComponent.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.stopsComponent.entity.StopsHubFileRecord;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class StopsHubFileRecordMapperProvider {
    private String id;
    private String fileName;
    private String filePath;
    private String stopsHubId;
    private String dockerImagesName;
    private String crtDttmStr;


    private boolean preventSQLInjectionStopsHubFileRecord(StopsHubFileRecord record) {
        if (null == record) {
            return false;
        }
        this.id = SqlUtils.preventSQLInjection(record.getId());
        this.fileName = SqlUtils.preventSQLInjection(record.getFileName());
        this.filePath = SqlUtils.preventSQLInjection(record.getFilePath());
        this.stopsHubId = SqlUtils.preventSQLInjection(record.getStopsHubId());
        this.dockerImagesName = SqlUtils.preventSQLInjection(record.getDockerImagesName());
        this.crtDttmStr = SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(null != record.getCrtDttm() ? record.getCrtDttm() : new Date()));

        return true;
    }

    private void reset() {
        this.id = null;
        this.fileName = null;
        this.filePath = null;
        this.stopsHubId = null;
        this.dockerImagesName = null;
        this.crtDttmStr = null;
    }

    /**
     * add StopsHubFileRecord
     *
     * @param record
     * @return
     */
    public String addStopsHubFileRecord(StopsHubFileRecord record) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionStopsHubFileRecord(record);
        if (flag) {
            SQL sql = new SQL();
            sql.INSERT_INTO("stops_hub_file_record");
            sql.VALUES("id",id);
            sql.VALUES("file_name",fileName);
            sql.VALUES("file_path",filePath);
            sql.VALUES("stops_hub_id", stopsHubId);
            sql.VALUES("docker_images_name",dockerImagesName);
            sql.VALUES("crt_dttm",crtDttmStr);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }


}
