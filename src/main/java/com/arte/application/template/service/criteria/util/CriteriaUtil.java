package com.arte.application.template.service.criteria.util;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.arte.libs.grammar.domain.QueryPropertyRestriction;

public final class CriteriaUtil {

    private static final String POSTGRE_DB_FUNCTION = "lower_unaccent";

    private static final String OR_DELIMETER = " or ";

    private CriteriaUtil() {

    }

    /**
     * Apply Accent and Case Insensitive search conditions in WHERE clause with default database function
     * 
     * @param property the query parameters properties
     * @param fields the database fields on which the search is made
     * @return the criterion restrictions of the search conditions
     */
    public static Criterion buildAccentAndCaseInsensitiveCriterion(QueryPropertyRestriction property, List<String> fields) {
        return buildAccentAndCaseInsensitiveCriterion(property, fields, POSTGRE_DB_FUNCTION);
    }

    /**
     * Apply Accent and Case Insensitive search conditions in WHERE clause
     * 
     * @param property the query parameters properties
     * @param fields the database fields on which the search is made
     * @param dbFunction the database function that executes the Accent and Case Insensitive
     * @return the criterion restrictions of the search conditions
     */
    public static Criterion buildAccentAndCaseInsensitiveCriterion(QueryPropertyRestriction property, List<String> fields, String dbFunction) {
        String sentence = fields.stream().map(field -> makeSearchSentenceSql(property, field, dbFunction)).collect(Collectors.joining(OR_DELIMETER));
        String sql = "(" + sentence + ")";
        return Restrictions.sqlRestriction(sql);
    }

    private static String makeSearchSentenceSql(QueryPropertyRestriction property, String field, String dbFunction) {
        return "(" + dbFunction + "({alias}." + field + ") " + property.getOperationType() + " " + dbFunction + "('" + property.getRightValue() + "'))";
    }
}
