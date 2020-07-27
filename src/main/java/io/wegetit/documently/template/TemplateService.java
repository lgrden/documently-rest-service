package io.wegetit.documently.template;

import java.util.Map;

import io.wegetit.documently.document.DocumentEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Service
public class TemplateService {

    public String processAsHtml(DocumentEntity document, Map<String, String> map) {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateEngine.setTemplateResolver(templateResolver);
        Context context = new Context();
        document.getFields().stream().forEach(p -> context.setVariable(p.getName(), "<span style=\"font-weight: bold; color:red;\">{"+p.getDescription()+"}</span>"));
        map.forEach((k, v) -> context.setVariable(k, v));
        return templateEngine.process(extendTemplate(document.getTemplate()), context);
    }

    private  String extendTemplate(String template) {
        return template.replaceAll("(\\$\\{[^}]+})", "\\[\\($1\\)\\]");
    }
}
