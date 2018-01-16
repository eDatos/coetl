package com.arte.application.template.config.audit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import com.arte.application.template.config.AuditConstants;
import com.arte.application.template.config.Constants;
import com.arte.application.template.security.SecurityUtils;

@Component
public class AuditEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher publisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	public void publish(String type, String identifier, String message) {
		HashMap<String, Object> map = new HashMap<>();
		map.put(AuditConstants.CODE, identifier);
		map.put(AuditConstants.MESSAGE, message);
		publish(type, map);
	}

	public void publish(String type, String identifier) {
		HashMap<String, Object> map = new HashMap<>();
		map.put(AuditConstants.CODE, identifier);
		publish(type, map);
	}

	private void publish(String type, Map<String, Object> map) {
		AuditEvent event = new AuditEvent(getCurrentUser(), type, map);
		publish(event);
	}

	private void publish(AuditEvent event) {
		if (this.publisher != null) {
			this.publisher.publishEvent(new AuditApplicationEvent(event));
		}
	}

	private String getCurrentUser() {
		String userName = SecurityUtils.getCurrentUserLogin();
		return userName != null ? userName : Constants.SYSTEM_ACCOUNT;
	}
}