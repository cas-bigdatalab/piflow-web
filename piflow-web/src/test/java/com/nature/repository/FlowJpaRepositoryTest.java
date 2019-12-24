package com.nature.repository;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.model.Flow;
import com.nature.repository.flow.FlowJpaRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class FlowJpaRepositoryTest extends ApplicationTests {

	@Autowired
	private FlowJpaRepository flowJpaRepository;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetOne() {
		Flow ff8081816dd7c769016dd7e95ecc0002 = flowJpaRepository.getOne("ff8081816dd7c769016dd7e95ecc0002");
		logger.info(ff8081816dd7c769016dd7e95ecc0002 + "");
	}

	private Specification<Flow> addEnableFlagParam() {
		Specification<Flow> specification = new Specification<Flow>() {
			@Override
			public Predicate toPredicate(Root<Flow> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				//Root.get("enableFlag") means to get the name of the enableFlag field.
				return criteriaBuilder.equal(root.get("enableFlag"), 1);
			}
		};
		return specification;
	}

	private Specification<Flow> addParam(String key, String value) {
		Specification<Flow> specification = new Specification<Flow>() {
			@Override
			public Predicate toPredicate(Root<Flow> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				//Root.get(key) means to get the name of the key field.
				return criteriaBuilder.equal(root.get(key), value);
			}
		};
		return specification;
	}

}
