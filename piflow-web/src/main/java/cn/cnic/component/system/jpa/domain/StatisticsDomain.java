package cn.cnic.component.system.jpa.domain;

import cn.cnic.component.system.entity.Statistics;
import cn.cnic.component.system.jpa.repository.StatisticsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticsDomain {

    @Autowired
    private StatisticsJpaRepository statisticsJpaRepository;

    public Statistics getStatisticsById(String id) {
        return statisticsJpaRepository.getOne(id);
    }

    public List<Statistics> getStatisticsList() {
        return statisticsJpaRepository.findAll();
    }

    public Statistics saveOrUpdate(Statistics statistics) {
        return statisticsJpaRepository.save(statistics);
    }

    public List<Statistics> saveOrUpdate(List<Statistics> statisticsList) {
        return statisticsJpaRepository.saveAll(statisticsList);
    }

}
