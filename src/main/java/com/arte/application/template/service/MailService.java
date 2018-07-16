package com.arte.application.template.service;

import com.arte.application.template.domain.Usuario;

public interface MailService {

    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);

    void sendEmailFromTemplate(Usuario user, String templateName, String titleKey);

    void sendCreationEmail(Usuario user);
}