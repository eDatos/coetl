package es.gobcan.istac.coetl.service;

import es.gobcan.istac.coetl.domain.Usuario;

public interface MailService {

    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    void sendEmailFromTemplate(Usuario user, String templateName, String titleKey);

    void sendCreationEmail(Usuario user);
}