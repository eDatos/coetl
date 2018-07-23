package es.gobcan.coetl.service.criteria;

import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.criterion.Criterion;

import com.arte.libs.grammar.domain.QueryPropertyRestriction;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.CriteriaProcessorContext;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.converter.CriterionConverter;

import es.gobcan.coetl.domain.Pelicula;
import es.gobcan.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.coetl.errors.ErrorConstants;
import es.gobcan.coetl.service.criteria.util.CriteriaUtil;

public class PeliculaCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String TABLE_FIELD_ID = "id";
    private static final String TABLE_FIELD_TITULO = "titulo";
    private static final String ENTITY_FIELD_FECHA_ESTRENO = "fechaEstreno";
    private static final String ENTITY_FIELD_IDIOMA = "idioma.id";
    private static final String ENTITY_FIELD_CATEGORIAS = "categorias";
    private static final String ENTITY_FIELD_ACTORES = "actores";

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
                RestrictionProcessorBuilder.longRestrictionProcessor()
                .withQueryProperty(QueryProperty.ID)
                .withEntityProperty(TABLE_FIELD_ID)
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
        
        registerRestrictionProcessor(RestrictionProcessorBuilder.longRestrictionProcessor()
                .withQueryProperty(QueryProperty.CATEGORIAS)
                .withAlias(ENTITY_FIELD_CATEGORIAS, "c")
                .withEntityProperty("c.id")
                .build());
        
        registerRestrictionProcessor(RestrictionProcessorBuilder.longRestrictionProcessor()
                .withQueryProperty(QueryProperty.ACTORES)
                .withAlias(ENTITY_FIELD_ACTORES, "a")
                .withEntityProperty("a.id")
                .build());
      //@formatter:on
    }

    private static class SqlCriterionConverter implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if (QueryProperty.TITULO.name().equalsIgnoreCase(property.getLeftExpression()) && "ILIKE".equalsIgnoreCase(property.getOperationType().name())) {
                ArrayList<String> fields = new ArrayList<>(Arrays.asList(TABLE_FIELD_TITULO));
                return CriteriaUtil.buildAccentAndCaseInsensitiveCriterion(property, fields);
            }
            throw new CustomParameterizedExceptionBuilder().message(String.format("Parámetro de búsqueda no soportado: '%s'", property))
                    .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }
    }
}
