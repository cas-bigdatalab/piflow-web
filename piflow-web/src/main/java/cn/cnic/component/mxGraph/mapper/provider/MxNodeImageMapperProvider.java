package cn.cnic.component.mxGraph.mapper.provider;

import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.mxGraph.entity.MxNodeImage;
import org.apache.commons.lang3.StringUtils;

public class MxNodeImageMapperProvider {

    private String imageName;
    private String imagePath;
    private String imageUrl;
    private String imageType;

    private boolean preventSQLInjectionMxNodeImage(MxNodeImage mxNodeImage) {
        if (null == mxNodeImage || StringUtils.isBlank(mxNodeImage.getLastUpdateUser())) {
            return false;
        }

        // Selection field
        this.imageName = SqlUtils.preventSQLInjection(mxNodeImage.getImageName());
        this.imagePath = SqlUtils.preventSQLInjection(mxNodeImage.getImagePath());
        this.imageUrl = SqlUtils.preventSQLInjection(mxNodeImage.getImageUrl());
        this.imageType = SqlUtils.preventSQLInjection(mxNodeImage.getImageType());
        return true;
    }

    private void reset() {
        this.imageName = null;
        this.imagePath = null;
        this.imageUrl = null;
        this.imageType = null;
    }

    /**
     * add MxNodeImage
     *
     * @param mxNodeImage
     * @return
     */
    public String addMxNodeImage(MxNodeImage mxNodeImage) {
        String sqlStr = "select *";
        boolean flag = preventSQLInjectionMxNodeImage(mxNodeImage);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO mx_node_image ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("image_name, ");
            strBuf.append("image_path, ");
            strBuf.append("image_url, ");
            strBuf.append("image_type ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(mxNodeImage) + ", ");
            strBuf.append(this.imageName + ", ");
            strBuf.append(this.imagePath + ", ");
            strBuf.append(this.imageUrl + ", ");
            strBuf.append(this.imageType + " ");
            strBuf.append(")");
            sqlStr = strBuf.toString();
        }
        reset();
        return sqlStr;
    }

    public String userGetMxNodeImageListByImageType(String username, String imageType) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(imageType)) {
            return "select 0";
        }
        String str = "select mni.* from mx_node_image mni where mni.enable_flag=1 and mni.crt_user=" + SqlUtils.preventSQLInjection(username) + " and mni.image_type=" + SqlUtils.preventSQLInjection(imageType) + " order by mni.last_update_dttm desc";
        return str;
    }

}
