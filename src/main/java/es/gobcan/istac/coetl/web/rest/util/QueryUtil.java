package es.gobcan.istac.coetl.web.rest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.arte.libs.grammar.antlr.DefaultQueryExprVisitor;
import com.arte.libs.grammar.antlr.QueryExprCompiler;
import com.arte.libs.grammar.domain.QueryRequest;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;

import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.service.criteria.ComputationalThreadCriteriaProcessor;
import es.gobcan.istac.coetl.service.criteria.EtlCriteriaProcessor;
import es.gobcan.istac.coetl.service.criteria.EtlCriteriaProcessor.QueryProperty;

@Component
public class QueryUtil {

    private static final Logger logger = LoggerFactory.getLogger(QueryUtil.class);
    private static final String INCLUDE_DELETED_HINT = "HINT INCLUDE_DELETED SET 'true'";
    private static final String AND = " AND ";
    private static final String EQ = " EQ ";
    private QueryExprCompiler queryExprCompiler = new QueryExprCompiler();

    public DetachedCriteria queryToEtlCriteria(Pageable pageable, String query) {
        return queryToCriteria(pageable, query, new EtlCriteriaProcessor());
    }

    public DetachedCriteria queryToComputationalThreadCriteria(Pageable pageable, String query) {
        return queryToCriteria(pageable, query, new ComputationalThreadCriteriaProcessor());
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

    private DetachedCriteria queryToCriteria(Pageable pageable, String query, AbstractCriteriaProcessor processor) {
        String finalQuery = query;
        if (finalQuery == null) {
            finalQuery = StringUtils.EMPTY;
        }

        finalQuery += pageableSortToQueryString(pageable);

        finalQuery = moveHintToTheEnd(finalQuery);

        try {
            QueryRequest queryRequest = null;
            logger.debug("Petición para mapear query: {}", finalQuery);
            if (StringUtils.isNotBlank(finalQuery)) {
                DefaultQueryExprVisitor visitor = new DefaultQueryExprVisitor();
                queryExprCompiler.parse(finalQuery, visitor);
                queryRequest = visitor.getQueryRequest();
                if (queryRequest.getRestriction() == null && StringUtils.isNotBlank(query) && isNotQueryHint(query)) {
                    throw new CustomParameterizedExceptionBuilder().code(ErrorConstants.ERR_INTERNAL_SERVER_ERROR).message("Search Parameter not supported").build();
                }
            }

            return processor.process(queryRequest);
        } catch (Exception e) {
            throw new CustomParameterizedExceptionBuilder().message("Incorrect query parameter").code(ErrorConstants.QUERY_NO_SOPORTADA).build();
        }
    }

    private String moveHintToTheEnd(String query) {
        Pattern pattern = Pattern.compile("( HINT [\\w\\s=\']+) (:?(:?ORDER).+)?$");
        Matcher matcher = pattern.matcher(query);
        String finalQuery = query;
        if (matcher.find()) {
            finalQuery = query.replace(matcher.group(1), "") + matcher.group(1);
        }

        return finalQuery;
    }

    private boolean isNotQueryHint(String query) {
        Pattern pattern = Pattern.compile("( HINT [\\w\\s=\']+)");
        Matcher matcher = pattern.matcher(query);
        return !matcher.matches();
    }

    public String getQueryByLastExecution(String lastExecutionStartDate, String lastExecutionResult, StringBuilder queryBuilder) {
        StringBuilder query = new StringBuilder();
        String and = StringUtils.isNotBlank(queryBuilder) ? AND : "";
        if (StringUtils.isNotBlank(lastExecutionResult) && StringUtils.isNotBlank(lastExecutionStartDate)) {
            query.append(and).append(QueryProperty.LAST_EXECUTION_CUSTOM).append(" IN ( ").append(String.format("'%s'", lastExecutionResult)).append(", ")
            .append(String.format("'%s'", lastExecutionStartDate)).append(")");

        } else if (StringUtils.isNotBlank(lastExecutionResult) && StringUtils.isBlank(lastExecutionStartDate)) {
            query.append(and).append(QueryProperty.LAST_EXECUTION_BY_RESULT).append(EQ).append(String.format("'%s'", lastExecutionResult)).toString();

        } else if (StringUtils.isBlank(lastExecutionResult) && StringUtils.isNotBlank(lastExecutionStartDate)) {
            query.append(and).append(QueryProperty.LAST_EXECUTION).append(EQ).append(String.format("'%s'", lastExecutionStartDate)).toString();
        }

        return query.toString();
    }

    public String getQueryByExecutionPlatform(String executionPlatform, StringBuilder queryBuilder) {
        StringBuilder query = new StringBuilder();
        String and = StringUtils.isNotBlank(queryBuilder) ? AND : "";
        if (executionPlatform != null && !executionPlatform.isEmpty()) {
            return query.append(and).append(QueryProperty.EXECUTION_PLATFORM).append(EQ).append("'").append(executionPlatform).append("'").toString();
        }
        return query.toString();
    }

    public String getQueryByExternalItemRestriction(Long restriction, StringBuilder queryBuilder) {
        StringBuilder query = new StringBuilder();
        String and = StringUtils.isNotBlank(queryBuilder) ? AND : "";
        if (restriction != null) {
            return query.append(and).append(QueryProperty.STATISTICAL_OPERATION).append(EQ).append(restriction).toString();
        }
        return query.toString();
    }

}
