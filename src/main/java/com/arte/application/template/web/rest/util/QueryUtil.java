package com.arte.application.template.web.rest.util;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.arte.application.template.service.criteria.ActorCriteriaProcessor;
import com.arte.application.template.service.criteria.OperacionCriteriaProcessor;
import com.arte.application.template.service.criteria.PeliculaCriteriaProcessor;
import com.arte.application.template.service.criteria.RolCriteriaProcessor;
import com.arte.application.template.service.criteria.UsuarioCriteriaProcessor;
import com.arte.libs.grammar.antlr.DefaultQueryExprVisitor;
import com.arte.libs.grammar.antlr.QueryExprCompiler;
import com.arte.libs.grammar.domain.QueryRequest;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;

@Component
public class QueryUtil {

    private static final Logger logger = LoggerFactory.getLogger(QueryUtil.class);
    private static final String INCLUDE_DELETED_HINT = "HINT INCLUDE_DELETED SET 'true'";
    private QueryExprCompiler queryExprCompiler = new QueryExprCompiler();

    public DetachedCriteria queryToUserCriteria(String query) {
        return queryToCriteria(query, new UsuarioCriteriaProcessor());
    }

    public DetachedCriteria queryToOperacionCriteria(String query) {
        return queryToCriteria(query, new OperacionCriteriaProcessor());
    }

    public DetachedCriteria queryToRolCriteria(String query) {
        return queryToCriteria(query, new RolCriteriaProcessor());
    }

    public DetachedCriteria queryToPeliculaCriteria(String query) {
        return queryToCriteria(query, new PeliculaCriteriaProcessor());
    }

    public DetachedCriteria queryToActorCriteria(String query) {
        return queryToCriteria(query, new ActorCriteriaProcessor());
    }

    public String queryIncludingDeleted(String query) {
        return new StringBuilder(query).append(" ").append(INCLUDE_DELETED_HINT).toString();
    }

    public String pageableSortToQueryString(Pageable pageable) {
        if (pageable == null || pageable.getSort() == null) {
            return StringUtils.EMPTY;
        }

        StringBuilder result = new StringBuilder();
        for (Order pageableOrder : pageable.getSort()) {
            if (!"ID".equalsIgnoreCase(pageableOrder.getProperty())) {
                if (result.length() == 0) {
                    result.append(" ORDER BY ");
                } else {
                    result.append(", ");
                }
                result.append(pageableOrder.getProperty().toUpperCase());
                if (pageableOrder.isAscending()) {
                    result.append(" ").append("ASC");
                } else {
                    result.append(" ").append("DESC");
                }
            }
        }
        return result.toString();
    }

    private DetachedCriteria queryToCriteria(String query, AbstractCriteriaProcessor processor) {
        QueryRequest queryRequest = null;
        logger.debug("Petición para mapear query: {}", query);
        if (StringUtils.isNotBlank(query)) {
            DefaultQueryExprVisitor visitor = new DefaultQueryExprVisitor();
            queryExprCompiler.parse(query, visitor);
            queryRequest = visitor.getQueryRequest();
        }
        return processor.process(queryRequest);
    }

}
