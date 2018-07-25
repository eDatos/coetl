package es.gobcan.coetl.service.impl;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.repository.EtlRepository;
import es.gobcan.coetl.service.EtlService;
import es.gobcan.coetl.web.rest.util.QueryUtil;

@Service
public class EtlServiceImp implements EtlService {

    private static final Logger LOG = LoggerFactory.getLogger(EtlServiceImp.class);

    @Autowired
    EtlRepository etlRepository;

    @Autowired
    QueryUtil queryUtil;

    @Override
    public Etl create(Etl etl) {
        LOG.debug("Request to create an ETL : {}", etl);
        return save(etl);
    }

    @Override
    public Etl update(Etl etl) {
        LOG.debug("Request to update an ETL : {}", etl);
        return save(etl);
    }

    @Override
    public Etl delete(Etl etl) {
        LOG.debug("Request to delete an ETL : {}", etl);
        return save(etl);
    }

    @Override
    public Etl findOne(Long id) {
        LOG.debug("Request to find an ETL : {}", id);
        return etlRepository.findOne(id);
    }

    @Override
    public Page<Etl> findAll(String query, boolean includeDeleted, Pageable pageable) {
        LOG.debug("Request to find all ETLs by query : {}", query);
        DetachedCriteria criteria = buildEtlCriteria(query, includeDeleted, pageable);
        return etlRepository.findAll(criteria, pageable);
    }

    private Etl save(Etl etl) {
        LOG.debug("Request to save an ETL : {}", etl);
        return etlRepository.save(etl);
    }

    private DetachedCriteria buildEtlCriteria(String query, boolean includeDeleted, Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(query)) {
            queryBuilder.append(query);
        }
        String finalQuery = getFinalQuery(includeDeleted, queryBuilder);
        return queryUtil.queryToEtlCriteria(pageable, finalQuery);
    }

    private String getFinalQuery(boolean includeDeleted, StringBuilder queryBuilder) {
        String finalQuery = queryBuilder.toString();
        if (BooleanUtils.isTrue(includeDeleted)) {
            finalQuery = queryUtil.queryIncludingDeleted(finalQuery);
        }
        return finalQuery;
    }

}
