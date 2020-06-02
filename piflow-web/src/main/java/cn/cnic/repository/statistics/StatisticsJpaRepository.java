package cn.cnic.repository.statistics;

import cn.cnic.component.statistics.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface StatisticsJpaRepository extends JpaRepository<Statistics, String>, JpaSpecificationExecutor<Statistics>, Serializable {

}
