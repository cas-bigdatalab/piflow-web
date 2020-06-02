package cn.cnic.component.stopsComponent.transactional;

import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class StopGroupTransactional {

    public int jointAddStopsComponentGroup(StopsComponentGroup stopsComponentGroup){
        return 0;
    }

    public int addStopsComponentGroup(StopsComponentGroup stopsComponentGroup){
        return 0;
    }

}
