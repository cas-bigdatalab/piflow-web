package com.nature.repository.statistics;

import com.nature.component.statistics.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface StatisticsJpaRepository extends JpaRepository<Statistics, String>, JpaSpecificationExecutor<Statistics>, Serializable {

}
