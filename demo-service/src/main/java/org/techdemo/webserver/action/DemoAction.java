package org.techdemo.webserver.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoAction {

    private Logger log = LoggerFactory.getLogger(DemoAction.class);

    @GetMapping("/server/echo/{input}")
    public String echo(@PathVariable String input) {
        log.info("input {}", input);

        return "echo:" + input;
    }

    @GetMapping("/server/costtime/{second}")
    public String costTime(@PathVariable Integer second) {

        log.info("start cost time operate");

        try {

            for (int i = 0; i < second; i++) {
                Thread.sleep(1000);
                log.info(" second {}", i);
            }
            return "OK";

        } catch (InterruptedException e) {
            log.error(" interrupt {}", e);
            return e.getMessage();
        }
    }
}
