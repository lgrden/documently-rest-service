package io.wegetit.documently.template;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Service
public class TemplateService {

    public String processAsHtml(Map<String, String> map, String input) {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateEngine.setTemplateResolver(templateResolver);
        Context context = new Context();
        map.forEach((k, v) -> context.setVariable(k, v));
        input = extendTemplate(input);
        return templateEngine.process(input, context);
    }

    private  String extendTemplate(String template) {
        return template.replaceAll("(\\$\\{[^}]+})", "\\[\\($1\\)\\]");
    }
}
