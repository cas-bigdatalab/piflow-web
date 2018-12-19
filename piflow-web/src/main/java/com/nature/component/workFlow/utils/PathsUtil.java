package com.nature.component.workFlow.utils;

import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.vo.PathsVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PathsUtil {
    /**
     * pathsList实体转Vo
     *
     * @param pathsList
     * @return
     */
    public static List<PathsVo> pathsListPoToVo(List<Paths> pathsList) {
        List<PathsVo> pathsVoList = null;
        if (null != pathsList && pathsList.size() > 0) {
            pathsVoList = new ArrayList<PathsVo>();
            for (Paths paths : pathsList) {
                if (null != paths) {
                    PathsVo pathsVo = new PathsVo();
                    BeanUtils.copyProperties(paths, pathsVo);
                    pathsVoList.add(pathsVo);
                }
            }
        }
        return pathsVoList;
    }

    /**
     * pathsVoList Vo转实体
     * @param pathsVoList
     * @return
     */
    public static List<Paths> pathsListVoToPo(List<PathsVo> pathsVoList) {
        List<Paths> pathsList = null;
        if (null != pathsList && pathsList.size() > 0) {
            pathsList = new ArrayList<Paths>();
            for (PathsVo pathsVo : pathsVoList) {
                if (null != pathsVo) {
                    Paths paths = new Paths();
                    BeanUtils.copyProperties(pathsVo, paths);
                    paths.setId(Utils.getUUID32());
                    paths.setCrtDttm(new Date());
                    paths.setCrtUser("add");
                    paths.setLastUpdateDttm(new Date());
                    paths.setLastUpdateUser("add");
                    paths.setEnableFlag(true);
                    pathsList.add(paths);
                }
            }
        }
        return pathsList;
    }


}
