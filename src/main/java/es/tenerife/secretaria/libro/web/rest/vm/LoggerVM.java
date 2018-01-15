package es.tenerife.secretaria.libro.web.rest.vm;

import ch.qos.logback.classic.Logger;

public class LoggerVM {

	private String name;

	private String level;
	
	private Boolean inherited;

	public LoggerVM(Logger logger) {
		this.name = logger.getName();
		this.level = logger.getEffectiveLevel().toString();
		this.setInherited(logger.getLevel() == null);
	}

	public LoggerVM() {
		// Empty public constructor used by Jackson.
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public Boolean getInherited() {
		return inherited;
	}
	
	public void setInherited(Boolean inherited) {
		this.inherited = inherited;
	}

	@Override
	public String toString() {
		return "LoggerVM{" + "name='" + name + '\'' + ", level='" + level + '\'' + '}';
	}
}
