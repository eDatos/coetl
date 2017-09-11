package es.tenerife.secretaria.libro.service.criteria;

import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;

import es.tenerife.secretaria.libro.domain.Usuario;

public class UsuarioCriteriaProcessor extends AbstractCriteriaProcessor {

	private static final String ENTITY_FIELD_LOGIN = "login";
	private static final String ENTITY_FIELD_NOMBRE = "nombre";
	private static final String ENTITY_FIELD_APELLIDO1 = "apellido1";
	private static final String ENTITY_FIELD_APELLIDO2 = "apellido2";

	public UsuarioCriteriaProcessor() {
		super(Usuario.class);
	}

	public enum QueryProperty {
		NOMBRE, APELLIDO1, APELLIDO2, LOGIN
	}

	@Override
	public void registerProcessors() {
		//@formatter:off
    	registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.LOGIN)
                .withEntityProperty(ENTITY_FIELD_LOGIN).build());
    	
    	registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
    			.withQueryProperty(QueryProperty.NOMBRE)
    			.withEntityProperty(ENTITY_FIELD_NOMBRE).build());
    	
    	registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
    			.withQueryProperty(QueryProperty.APELLIDO1)
    			.withEntityProperty(ENTITY_FIELD_APELLIDO1).build());
    	
    	registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
    			.withQueryProperty(QueryProperty.APELLIDO2)
    			.withEntityProperty(ENTITY_FIELD_APELLIDO2).build());
        //@formatter:on
	}

}
