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
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.converter.CriterionConverter;

public class PeliculaCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String TABLE_FIELD_TITULO = "titulo";

    private static final String ENTITY_FIELD_ANNO_ESTRENO = "annoEstreno";
    private static final String ENTITY_FIELD_IDIOMA = "idioma.id";

    public PeliculaCriteriaProcessor() {
        super(Pelicula.class);
    }

    public enum QueryProperty {
        ID, ANNO_ESTRENO, IDIOMAS, CATEGORIAS, TITULO
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
                .withQueryProperty(QueryProperty.ANNO_ESTRENO)
                .withEntityProperty(ENTITY_FIELD_ANNO_ESTRENO)
                .sortable()
                .build());
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.longRestrictionProcessor()
                .withQueryProperty(QueryProperty.IDIOMAS)
                .withEntityProperty(ENTITY_FIELD_IDIOMA)
                .sortable()
                .build());
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.restrictionProcessor()
                .withQueryProperty(QueryProperty.CATEGORIAS)
                .withCriterionConverter(new SqlCriterionConverter())
                .build());
      //@formatter:on
    }

    private static class SqlCriterionConverter implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if (QueryProperty.ID.name().equalsIgnoreCase(property.getLeftExpression())) {
                return queryByIds(property);
            }
            if (QueryProperty.TITULO.name().equalsIgnoreCase(property.getLeftExpression())) {
                ArrayList<String> fields = new ArrayList<>(Arrays.asList(TABLE_FIELD_TITULO));
                return CriteriaUtil.buildAccentAndCaseInsensitiveCriterion(property, fields);
            }
            if (QueryProperty.CATEGORIAS.name().equalsIgnoreCase(property.getLeftExpression())) {
                return queryByCategorias(property);
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
    }
}
