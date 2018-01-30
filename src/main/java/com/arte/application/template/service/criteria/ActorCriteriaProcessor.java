package com.arte.application.template.service.criteria;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.arte.application.template.domain.Actor;
import com.arte.application.template.web.rest.errors.CustomParameterizedException;
import com.arte.libs.grammar.domain.QueryPropertyRestriction;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.CriteriaProcessorContext;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.converter.CriterionConverter;

public class ActorCriteriaProcessor extends AbstractCriteriaProcessor {

    public ActorCriteriaProcessor() {
        super(Actor.class);
    }

    public enum QueryProperty {
        PELICULA
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

            throw new CustomParameterizedException(String.format("Query param not supported: '%s", property));
        }

        private Criterion queryByNotPelicula(QueryPropertyRestriction property) {
            String consulta = "{alias}.id not in (" + "SELECT PA.actor_id " + "FROM pelicula_actor PA " + "WHERE PA.pelicula_id = %s" + ")";
            String sql = String.format(consulta, property.getRightExpression());
            return Restrictions.sqlRestriction(sql);
        }
    }
}
