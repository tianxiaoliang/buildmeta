package org.flysnow.cloud.buildmeta.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.flysnow.cloud.buildmeta.domain.dao.FarmMapper;
import org.flysnow.cloud.buildmeta.domain.model.Branch;
import org.flysnow.cloud.buildmeta.domain.model.BranchExample;
import org.flysnow.cloud.buildmeta.domain.model.Build;
import org.flysnow.cloud.buildmeta.domain.model.BuildExample;
import org.flysnow.cloud.buildmeta.domain.model.Collector;
import org.flysnow.cloud.buildmeta.domain.model.CollectorExample;
import org.flysnow.cloud.buildmeta.domain.model.Farm;
import org.flysnow.cloud.buildmeta.domain.model.FarmExample;
import org.flysnow.cloud.buildmeta.domain.model.Repository;
import org.flysnow.cloud.buildmeta.domain.model.RepositoryExample;
import org.flysnow.cloud.buildmeta.domain.model.BuildExample.Criteria;
import org.flysnow.cloud.buildmeta.ui.resteasy.BuildResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FarmService {
	private Logger logger = Logger.getLogger(BuildResource.class);

	@Autowired
	FarmMapper farmMapper;

	public boolean save(Farm farm) {
		// first check if exist
		FarmExample example = new FarmExample();
		org.flysnow.cloud.buildmeta.domain.model.FarmExample.Criteria criteria = example
				.createCriteria();
		criteria.andScalrEndpointEqualTo(farm.getScalrEndpoint());
		criteria.andScalrEnvIdEqualTo(farm.getScalrEnvId());
		criteria.andScalrFarmIdEqualTo(farm.getScalrFarmId());
		List<Farm> farms = farmMapper.selectByExample(example);
		if (farms == null || farms.size() == 0) {
			farmMapper.insert(farm);
			logger.debug(String.format("Save farm  %s successfully!",
					farm.getScalrFarmName()));
			return true;
		} else {
			logger.info(String.format(
					"farm %s already exists, env %s on scalr %s",
					farm.getScalrFarmName(), farm.getScalrEnvId(),
					farm.getScalrEndpoint()));
			return false;
		}
	}

	public void deleteCollector(Integer id) {
		farmMapper.deleteByPrimaryKey(id);
	}

	public List<Farm> getfarms() {
		List<Farm> farms = farmMapper.selectByExample(null);
		return farms;
	}
	public List<Farm> getFarms(String endpoint) {
		FarmExample example = new FarmExample();
		org.flysnow.cloud.buildmeta.domain.model.FarmExample.Criteria criteria = example
				.createCriteria();
		criteria.andScalrEndpointEqualTo(endpoint);
		List<Farm> farms = farmMapper.selectByExample(example);
		return farms;
	}

}
