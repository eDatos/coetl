package com.arte.application.template.service.criteria.util;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.arte.libs.grammar.domain.QueryPropertyRestriction;

public final class CriteriaUtil {

    private CriteriaUtil() {

    }

    private static final String POSTGRE_DB_FUNCTION = "lower_unaccent";

    /**
     * Apply Accent and Case Insensitive search conditions in WHERE clause
     * 
     * @param property the query parameters properties
     * @param fields the database fields on which the search is made
     * @return the criterion restrictions of the search conditions
     */
    public static Criterion searchAccentAndCaseInsensitiveCriterion(QueryPropertyRestriction property, List<String> fields) {
        final String OR_DELIMETER = " or ";
        String sentence = fields.stream().map(field -> doSearchSentenceSql(property, field, POSTGRE_DB_FUNCTION)).collect(Collectors.joining(OR_DELIMETER));
        String sql = "(" + sentence + ")";
        return Restrictions.sqlRestriction(sql);
    }

    /**
     * Apply Accent and Case Insensitive search conditions in WHERE clause
     * 
     * @param property the query parameters properties
     * @param fields the database fields on which the search is made
     * @param dbFunction the database function that executes the Accent and Case Insensitive
     * @return the criterion restrictions of the search conditions
     */
    public static Criterion searchAccentAndCaseInsensitiveCriterion(QueryPropertyRestriction property, List<String> fields, String dbFunction) {
        final String OR_DELIMETER = " or ";
        String sentence = fields.stream().map(field -> doSearchSentenceSql(property, field, dbFunction)).collect(Collectors.joining(OR_DELIMETER));
        String sql = "(" + sentence + ")";
        return Restrictions.sqlRestriction(sql);
    }

    private static String doSearchSentenceSql(QueryPropertyRestriction property, String field, String dbFunction) {
        return "(" + dbFunction + "({alias}." + field + ") " + property.getOperationType() + " '%' || " + dbFunction + "('" + property.getRightValue() + "') || '%')";
    }
}
