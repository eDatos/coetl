package com.arte.application.template.service.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.arte.application.template.domain.Pelicula;
import com.arte.application.template.service.criteria.util.CriteriaUtil;
import com.arte.application.template.web.rest.errors.CustomParameterizedException;
import com.arte.libs.grammar.domain.QueryPropertyRestriction;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.CriteriaProcessorContext;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.converter.CriterionConverter;

public class PeliculaCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String TABLE_FIELD_TITULO = "titulo";

    private static final String ENTITY_FIELD_FECHA_ESTRENO = "fechaEstreno";
    private static final String ENTITY_FIELD_IDIOMA = "idioma.id";

    public PeliculaCriteriaProcessor() {
        super(Pelicula.class);
    }

    public enum QueryProperty {
        ID, FECHAESTRENO, IDIOMA, CATEGORIAS, TITULO, ACTORES
    }

    @Override
    public void registerProcessors() {
      //@formatter:off
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.ID)
                .withCriterionConverter(new SqlCriterionConverter())
                .build());
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.TITULO)
                .withCriterionConverter(new SqlCriterionConverter())
                .build());
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.dateRestrictionProcessor()
                .withQueryProperty(QueryProperty.FECHAESTRENO)
                .withEntityProperty(ENTITY_FIELD_FECHA_ESTRENO)
                .build());
        registerOrderProcessor(
                OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.FECHAESTRENO)
                .withEntityProperty(ENTITY_FIELD_FECHA_ESTRENO)
                .build());
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.longRestrictionProcessor()
                .withQueryProperty(QueryProperty.IDIOMA)
                .withEntityProperty(ENTITY_FIELD_IDIOMA)
                .build());
        registerOrderProcessor(
                OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.IDIOMA)
                .withEntityProperty(ENTITY_FIELD_IDIOMA)
                .build());
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.restrictionProcessor()
                .withQueryProperty(QueryProperty.CATEGORIAS)
                .withCriterionConverter(new SqlCriterionConverter())
                .build());
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.restrictionProcessor()
                .withQueryProperty(QueryProperty.ACTORES)
                .withCriterionConverter(new SqlCriterionConverter())
                .build());
      //@formatter:on
    }

    private static class SqlCriterionConverter implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if (QueryProperty.ID.name().equalsIgnoreCase(property.getLeftExpression()) && "IN".equalsIgnoreCase(property.getOperationType().name())) {
                return queryByIds(property);
            }
            if (QueryProperty.TITULO.name().equalsIgnoreCase(property.getLeftExpression()) && "ILIKE".equalsIgnoreCase(property.getOperationType().name())) {
                ArrayList<String> fields = new ArrayList<>(Arrays.asList(TABLE_FIELD_TITULO));
                return CriteriaUtil.buildAccentAndCaseInsensitiveCriterion(property, fields);
            }
            if (QueryProperty.CATEGORIAS.name().equalsIgnoreCase(property.getLeftExpression()) && "IN".equalsIgnoreCase(property.getOperationType().name())) {
                return queryByCategorias(property);
            }
            if (QueryProperty.ACTORES.name().equalsIgnoreCase(property.getLeftExpression()) && "IN".equalsIgnoreCase(property.getOperationType().name())) {
                return queryByActores(property);
            }
            throw new CustomParameterizedException(String.format("Query param not supported: '%s", property));
        }

        private Criterion queryByIds(QueryPropertyRestriction property) {
            String consulta = "{alias}.id " + property.getOperationType() + "(%s)";
            String sql = String.format(consulta, property.getRightExpressions().stream().collect(Collectors.joining(",")));
            return Restrictions.sqlRestriction(sql);
        }

        private Criterion queryByCategorias(QueryPropertyRestriction property) {
            //@formatter:off
            String consulta = "{alias}.id in ("
                  + "SELECT PC.PELICULA_ID "
                  + "FROM PELICULA_CATEGORIA PC "
                  + "WHERE (PC.CATEGORIA_ID " + property.getOperationType() + " (%s)))";
            //@formatter:on
            String sql = String.format(consulta, property.getRightExpressions().stream().collect(Collectors.joining(",")));
            return Restrictions.sqlRestriction(sql);
        }

        private Criterion queryByActores(QueryPropertyRestriction property) {
            //@formatter:off
            String consulta = "{alias}.id in ("
                  + "SELECT PA.PELICULA_ID "
                  + "FROM PELICULA_ACTOR PA "
                  + "WHERE (PA.ACTOR_ID " + property.getOperationType() + " (%s)))";
            //@formatter:on
            String sql = String.format(consulta, property.getRightExpressions().stream().collect(Collectors.joining(",")));
            return Restrictions.sqlRestriction(sql);
        }
    }
}
