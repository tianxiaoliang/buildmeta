package org.flysnow.cloud.buildmeta.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.domain.dao.CollectorMapper;
import org.flysnow.cloud.buildmeta.domain.model.Branch;
import org.flysnow.cloud.buildmeta.domain.model.BranchExample;
import org.flysnow.cloud.buildmeta.domain.model.Build;
import org.flysnow.cloud.buildmeta.domain.model.BuildExample;
import org.flysnow.cloud.buildmeta.domain.model.Collector;
import org.flysnow.cloud.buildmeta.domain.model.CollectorExample;
import org.flysnow.cloud.buildmeta.domain.model.Repository;
import org.flysnow.cloud.buildmeta.domain.model.RepositoryExample;
import org.flysnow.cloud.buildmeta.domain.model.BuildExample.Criteria;
import org.flysnow.cloud.buildmeta.ui.resteasy.BuildResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectorService {
	private Logger logger = Logger.getLogger(BuildResource.class);

	@Autowired
	CollectorMapper collectorMapper;

	public void save(Collector collector) {
		// first check if exist
		CollectorExample example = new CollectorExample();
		org.flysnow.cloud.buildmeta.domain.model.CollectorExample.Criteria criteria = example
				.createCriteria();
		criteria.andTargetEqualTo(collector.getTarget());
		criteria.andRoleEqualTo(collector.getRole());
		criteria.andEnvIdEqualTo(collector.getEnvId());
		List<Collector> collectors = collectorMapper.selectByExample(example);
		if (collectors == null || collectors.size() == 0) {
			collectorMapper.insert(collector);
			logger.debug(String.format(
					"Save collector on role %s successfully!",
					collector.getRole()));
		} else {
			logger.info(String.format(
					"collector %s already exists on role %s on ENV %s",
					collector.getTarget(), collector.getRole(),
					collector.getEnvId()));
		}
	}

	public void deleteCollector(Integer id) {
		collectorMapper.deleteByPrimaryKey(id);
	}

	public List<Collector> getCollectors() {
		List<Collector> collectors = collectorMapper.selectByExample(null);
		return collectors;
	}

	public List<Collector> getCollectors(String role, String env, String c_type) {
		CollectorExample example = new CollectorExample();
		org.flysnow.cloud.buildmeta.domain.model.CollectorExample.Criteria criteria = example
				.createCriteria();
		criteria.andRoleEqualTo(role);
		criteria.andEnvIdEqualTo(env);
		criteria.andCTypeEqualTo(c_type);
		List<Collector> collectors = collectorMapper.selectByExampleWithBLOBs(example);
		return collectors;
	}

	public List<Collector> getBuilds(Map<String, String> queryParams) {
		CollectorExample example = new CollectorExample();
		org.flysnow.cloud.buildmeta.domain.model.CollectorExample.Criteria criteria = example
				.createCriteria();

		String role = queryParams.get("role");
		if (role != null) {
			criteria.andRoleEqualTo(role);
		}

		String env = queryParams.get("env");
		if (env != null) {
			criteria.andEnvIdEqualTo(env);
		}

		String target = queryParams.get("target");
		if (target != null) {
			criteria.andTargetEqualTo(target);
		}

		List<Collector> collectors = collectorMapper.selectByExample(example);
		return collectors;

	}

}
