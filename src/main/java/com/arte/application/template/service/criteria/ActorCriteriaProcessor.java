package com.arte.application.template.service.criteria;

import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.criterion.Criterion;

import com.arte.application.template.domain.Actor;
import com.arte.application.template.service.criteria.util.CriteriaUtil;
import com.arte.application.template.web.rest.errors.CustomParameterizedException;
import com.arte.application.template.web.rest.errors.ErrorConstants;
import com.arte.libs.grammar.domain.QueryPropertyRestriction;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.CriteriaProcessorContext;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.converter.CriterionConverter;

public class ActorCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String TABLE_FIELD_NOMBRE = "nombre";
    private static final String TABLE_FIELD_APELLIDO1 = "apellido_1";
    private static final String TABLE_FIELD_APELLIDO2 = "apellido_2";

    private static final String ENTITY_FIELD_APELLIDO1 = "apellido1";

    public ActorCriteriaProcessor() {
        super(Actor.class);
    }

    public enum QueryProperty {
        ACTOR, APELLIDO1
    }

    public void registerProcessors() {
        //@formatter:off
        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.ACTOR)
                .withCriterionConverter(new SqlCriterionConverter())
                .build());
        registerOrderProcessor(
                OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.APELLIDO1)
                .withEntityProperty(ENTITY_FIELD_APELLIDO1)
                .build());
        //@formatter:on
    }

    public static class SqlCriterionConverter implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if (QueryProperty.ACTOR.name().equalsIgnoreCase(property.getLeftExpression()) && "ILIKE".equalsIgnoreCase(property.getOperationType().name())) {
                ArrayList<String> fields = new ArrayList<>(Arrays.asList(TABLE_FIELD_NOMBRE, TABLE_FIELD_APELLIDO1, TABLE_FIELD_APELLIDO2));
                return CriteriaUtil.buildAccentAndCaseInsensitiveCriterion(property, fields);
            }
            throw new CustomParameterizedException(String.format("Query param not supported: '%s'", property), ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name());
        }
    }
}