package org.techdemo.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebServerApplication {

    private static final Logger log = LoggerFactory.getLogger(WebServerApplication.class);

    public static void main(String[] argv) {

        log.debug("start...");
        SpringApplication app = new SpringApplication(WebServerApplication.class);
        app.run(argv);
    }
}
