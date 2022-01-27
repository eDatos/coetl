package es.gobcan.istac.coetl.invocation.facade;

import java.util.Locale;

import org.siemac.metamac.rest.notices.v1_0.domain.Message;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.MessageBuilder;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.invocation.service.MetamacApisLocator;

@Component(NotificationRestInternalFacade.BEAN_ID)
public class NotificationRestInternalFacadeImpl implements NotificationRestInternalFacade{

    private final Logger log = LoggerFactory.getLogger(NotificationRestInternalFacadeImpl.class);

    private static final String GESTOR_CONSOLA_ETL = "GESTOR_CONSOLA_ETL";

    @Autowired
    private MetamacApisLocator restApiLocator;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void sendExecutionErrorEtlNotice(Etl etl) {
        String subjectCode = "notice.etl.execution.error.subject";
        String messageCode = "notice.etl.execution.error.message";

        String[] args = {etl.getName(), etl.getCode()};

        try {
            Notice notice = createNotice(subjectCode, messageCode, args, etl.getExternalItem().getCode());

            restApiLocator.getNoticesRestInternalFacadeV10().createNotice(notice);
        } catch (Exception e) {
            log.debug("Error al enviar notificacion : {}", e);
        }
    }

    private Notice createNotice(String subjectCode, String messageCode, String[] args, String operationCode){
        String subject = generateTextI18n(subjectCode, args);
        Message message = createMessage(messageCode, args);

        // @formatter:off
        return NoticeBuilder.notification()
            .withMessages(message)
            .withSendingApplication(GESTOR_CONSOLA_ETL)
            .withSubject(subject)
            .withApplications(GESTOR_CONSOLA_ETL)
            .withStatisticalOperations(operationCode)
            .build();
    }

    private Message createMessage(String messageCode, String[] args) {
        String localisedMessage = generateTextI18n(messageCode, args);

        // @formatter:off
        return  MessageBuilder.message()
            .withText(localisedMessage)
            .build();
        // @formatter:on
    }

    private String generateTextI18n(String text, String[] args) {
        Locale locale = LocaleContextHolder.getLocale();

        return messageSource.getMessage(text, args, locale);
    }
}
