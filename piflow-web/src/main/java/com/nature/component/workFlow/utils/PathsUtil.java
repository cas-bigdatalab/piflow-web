package com.nature.component.workFlow.utils;

import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.vo.PathsVo;
import com.nature.component.workFlow.vo.PropertyVo;
import com.nature.component.workFlow.vo.StopsVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class PathsUtil {
    /**
     * pathsList实体转Vo
     *
     * @param pathsList
     * @return
     */
    public static List<PathsVo> pathsListPoToVo(List<Paths> pathsList){
        List<PathsVo> pathsVoList = null;
        if(null!=pathsList&&pathsList.size()>0){
            pathsVoList = new ArrayList<PathsVo>();
            for (Paths paths : pathsList){
                if(null!=paths){
                    PathsVo pathsVo = new PathsVo();
                    BeanUtils.copyProperties(paths, pathsVo);
                    pathsVoList.add(pathsVo);
                }
            }
        }
        return pathsVoList;
    }



}
