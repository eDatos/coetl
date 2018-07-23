package es.gobcan.coetl.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import es.gobcan.coetl.CoetlApp;
import es.gobcan.coetl.web.rest.LogsResource;
import es.gobcan.coetl.web.rest.vm.LoggerVM;

/**
 * Test class for the LogsResource REST controller.
 *
 * @see LogsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoetlApp.class)
public class LogsResourceIntTest {

    private static final String ENDPOINT_URL = "/management/logs";

    private MockMvc restLogsMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        LogsResource logsResource = new LogsResource();
        this.restLogsMockMvc = MockMvcBuilders.standaloneSetup(logsResource).build();
    }

    @Test
    public void getAllLogs() throws Exception {
        restLogsMockMvc.perform(get(ENDPOINT_URL)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void changeLogs() throws Exception {
        LoggerVM logger = new LoggerVM();
        logger.setLevel("INFO");
        logger.setName("ROOT");

        restLogsMockMvc.perform(put(ENDPOINT_URL).contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(logger))).andExpect(status().isNoContent());
    }

    @Test
    public void testLogstashAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        assertThat(context.getLogger("ROOT").getAppender("ASYNC_LOGSTASH")).isInstanceOf(AsyncAppender.class);
    }
}
