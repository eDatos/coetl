package es.tenerife.secretaria.libro.web.rest.util;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.arte.libs.grammar.antlr.DefaultQueryExprVisitor;
import com.arte.libs.grammar.antlr.QueryExprCompiler;
import com.arte.libs.grammar.domain.QueryRequest;
import com.arte.libs.grammar.domain.QuerySort;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;

import es.tenerife.secretaria.libro.service.criteria.UsuarioCriteriaProcessor;

@Component
public class QueryUtil {

	private static final Logger logger = LoggerFactory.getLogger(QueryUtil.class);
	private static final String INCLUDE_DELETED_HINT = "HINT INCLUDE_DELETED SET 'true'";
	private QueryExprCompiler queryExprCompiler = new QueryExprCompiler();

	public DetachedCriteria queryToUserCriteria(String query) {
		return queryToCriteria(query, new UsuarioCriteriaProcessor());
	}

	public String queryIncludingDeleted(String query) {
		return new StringBuilder(query).append(" ").append(INCLUDE_DELETED_HINT).toString();
	}

	private DetachedCriteria queryToCriteria(String query, AbstractCriteriaProcessor processor) {
		QueryRequest queryRequest = null;
		logger.debug("Request to map query: {}", query);
		if (StringUtils.isNotBlank(query)) {
			DefaultQueryExprVisitor visitor = new DefaultQueryExprVisitor();
			queryExprCompiler.parse(query, visitor);
			queryRequest = visitor.getQueryRequest();
			queryRequest.setSort(new QuerySort());
		}
		return processor.process(queryRequest);
	}

}
