package es.tenerife.secretaria.libro.web.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import es.tenerife.secretaria.libro.web.rest.vm.LoggerVM;

@RestController
@RequestMapping("/management")
public class LogsResource extends AbstractResource {

	@GetMapping("/logs")
	@Timed
	@PreAuthorize("hasPermission('LOGS', 'LEER')")
	public List<LoggerVM> getList() {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		return context.getLoggerList().stream().map(LoggerVM::new).collect(Collectors.toList());
	}

	@PutMapping("/logs")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Timed
	@PreAuthorize("hasPermission('LOGS', 'LEER')")
	public void changeLevel(@RequestBody LoggerVM jsonLogger) {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		context.getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
	}
}
