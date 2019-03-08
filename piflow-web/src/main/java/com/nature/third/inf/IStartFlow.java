package com.nature.third.inf;

import com.nature.base.vo.StatefulRtnBase;
import com.nature.base.vo.UserVo;
import com.nature.component.process.model.Process;

public interface IStartFlow {

    /**
     * 启动process
     *
     * @param process
     * @return
     */
    public StatefulRtnBase startProcess(Process process, String checkpoint, UserVo currentUser);

}
