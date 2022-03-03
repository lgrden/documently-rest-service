package io.wegetit.documently.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {

    @RequestMapping("/")
    public String index() {
        return "forward:/swagger-ui.html";
    }
}
