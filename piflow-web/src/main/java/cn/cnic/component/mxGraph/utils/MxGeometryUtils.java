package cn.cnic.component.mxGraph.utils;

import cn.cnic.component.mxGraph.entity.MxGeometry;

import java.util.Date;

public class MxGeometryUtils {

    public static MxGeometry mxGeometryNewNoId(String username) {

        MxGeometry mxGeometry = new MxGeometry();
        // basic properties (required when creating)
        mxGeometry.setCrtDttm(new Date());
        mxGeometry.setCrtUser(username);
        // basic properties
        mxGeometry.setEnableFlag(true);
        mxGeometry.setLastUpdateUser(username);
        mxGeometry.setLastUpdateDttm(new Date());
        mxGeometry.setVersion(0L);
        return mxGeometry;
    }

    public static MxGeometry initMxGeometryBasicPropertiesNoId(MxGeometry mxGeometry, String username) {
        if (null == mxGeometry) {
            return mxGeometryNewNoId(username);
        }
        // basic properties (required when creating)
        mxGeometry.setCrtDttm(new Date());
        mxGeometry.setCrtUser(username);
        // basic properties
        mxGeometry.setEnableFlag(true);
        mxGeometry.setLastUpdateUser(username);
        mxGeometry.setLastUpdateDttm(new Date());
        mxGeometry.setVersion(0L);
        return mxGeometry;
    }
}
