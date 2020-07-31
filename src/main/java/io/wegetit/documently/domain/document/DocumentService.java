package io.wegetit.documently.domain.document;

import io.wegetit.documently.domain.template.TemplateService;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@AllArgsConstructor
@Service
public class DocumentService {

    private final TemplateService templateService;

    public String processAsHtml(DocumentEntity document, Map<String, String> map) {
        TemplateEngine templateEngine = new TemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateEngine.setTemplateResolver(templateResolver);
        Context context = new Context();
        document.getFields().stream().forEach(p -> context.setVariable(p.getName(), "<span style=\"font-weight: bold; color:red;\">{"+p.getDescription()+"}</span>"));
        map.forEach((k, v) -> context.setVariable(k, v));
        String content = templateEngine.process(extendTemplate(document.getContent()), context);
        Optional<String> templateContent = templateService.findTemplateContent(document.getTemplate());
        return templateContent.isPresent() ? templateContent.get().replace("{{content}}", content): content;
    }

    private  String extendTemplate(String template) {
        return template.replaceAll("(\\$\\{[^}]+})", "\\[\\($1\\)\\]");
    }
}
