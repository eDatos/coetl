package es.gobcan.coetl.web.rest.util;

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

import es.gobcan.coetl.service.criteria.EtlCriteriaProcessor;
import es.gobcan.coetl.service.criteria.UsuarioCriteriaProcessor;

@Component
public class QueryUtil {

    private static final Logger logger = LoggerFactory.getLogger(QueryUtil.class);
    private static final String INCLUDE_DELETED_HINT = "HINT INCLUDE_DELETED SET 'true'";
    private QueryExprCompiler queryExprCompiler = new QueryExprCompiler();

    public DetachedCriteria queryToUserCriteria(Pageable pageable, String query) {
        return queryToCriteria(pageable, query, new UsuarioCriteriaProcessor());
    }

    public DetachedCriteria queryToEtlCriteria(Pageable pageable, String query) {
        return queryToCriteria(pageable, query, new EtlCriteriaProcessor());
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

        QueryRequest queryRequest = null;
        logger.debug("Petición para mapear query: {}", finalQuery);
        if (StringUtils.isNotBlank(finalQuery)) {
            DefaultQueryExprVisitor visitor = new DefaultQueryExprVisitor();
            queryExprCompiler.parse(finalQuery, visitor);
            queryRequest = visitor.getQueryRequest();
        }
        return processor.process(queryRequest);
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

}
