package cn.cnic.component.system.jpa.repository;

import cn.cnic.component.system.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface StatisticsJpaRepository extends JpaRepository<Statistics, String>, JpaSpecificationExecutor<Statistics>, Serializable {

}
