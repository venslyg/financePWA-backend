package com.gvsolutions;

import com.gvsolutions.config.AsyncSyncConfiguration;
import com.gvsolutions.config.EmbeddedElasticsearch;
import com.gvsolutions.config.EmbeddedSQL;
import com.gvsolutions.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { AccountSystemBackendApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedElasticsearch
@EmbeddedSQL
public @interface IntegrationTest {
}
