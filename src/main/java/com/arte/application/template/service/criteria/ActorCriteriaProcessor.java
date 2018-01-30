package com.arte.application.template.service.criteria;

import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.arte.application.template.domain.Actor;
import com.arte.application.template.service.criteria.util.CriteriaUtil;
import com.arte.application.template.web.rest.errors.CustomParameterizedException;
import com.arte.libs.grammar.domain.QueryPropertyRestriction;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.CriteriaProcessorContext;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.converter.CriterionConverter;

public class ActorCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String TABLE_FIELD_NOMBRE = "nombre";
    private static final String TABLE_FIELD_APELLIDO1 = "apellido_1";
    private static final String TABLE_FIELD_APELLIDO2 = "apellido_2";

    public ActorCriteriaProcessor() {
        super(Actor.class);
    }

    public enum QueryProperty {
        PELICULA, ACTOR
    }

    public void registerProcessors() {
        //@formatter:off
        registerRestrictionProcessor(RestrictionProcessorBuilder.longRestrictionProcessor()
                .withQueryProperty(QueryProperty.PELICULA)
                .withCriterionConverter(new SqlCriterionConverter())
                .build());
        //@formatter:on
    }

    public static class SqlCriterionConverter implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if (QueryProperty.PELICULA.name().equalsIgnoreCase(property.getLeftExpression()) && property.getOperationType().name().equalsIgnoreCase("NE")) {
                return queryByNotPelicula(property);
            }
            if (QueryProperty.PELICULA.name().equalsIgnoreCase(property.getLeftExpression()) && property.getOperationType().name().equalsIgnoreCase("EQ")) {
                return queryByPelicula(property);
            }
            if (QueryProperty.ACTOR.name().equalsIgnoreCase(property.getLeftExpression()) && property.getOperationType().name().equalsIgnoreCase("ILIKE")) {
                ArrayList<String> fields = new ArrayList<>(Arrays.asList(TABLE_FIELD_NOMBRE, TABLE_FIELD_APELLIDO1, TABLE_FIELD_APELLIDO2));
                return CriteriaUtil.buildAccentAndCaseInsensitiveCriterion(property, fields);
            }
            throw new CustomParameterizedException(String.format("Query param not supported: '%s", property));
        }

        private Criterion queryByNotPelicula(QueryPropertyRestriction property) {
            String consulta = "{alias}.id not in (" + "SELECT PA.ACTOR_ID " + "FROM PELICULA_ACTOR PA " + "WHERE PA.PELICULA_ID = %s" + ")";
            String sql = String.format(consulta, property.getRightExpression());
            return Restrictions.sqlRestriction(sql);
        }

        private Criterion queryByPelicula(QueryPropertyRestriction property) {
            String consulta = "{alias}.id in (" + "SELECT PA.ACTOR_ID " + "FROM PELICULA_ACTOR PA " + "WHERE PA.PELICULA_ID = %s" + ")";
            String sql = String.format(consulta, property.getRightExpression());
            return Restrictions.sqlRestriction(sql);
        }
    }
}
