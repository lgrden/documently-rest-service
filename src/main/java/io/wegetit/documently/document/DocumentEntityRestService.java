package io.wegetit.documently.document;

import io.wegetit.documently.template.TemplateService;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/documents")
public class DocumentEntityRestService {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final DocumentEntityService service;
	private final TemplateService templateService;
	
	@GetMapping
	public List<DocumentEntity> findAll() {
		return service.findAll();
	}
	
	@GetMapping(value = "/{id}")
	public DocumentEntity getById(@PathVariable String id) {
		return service.getById(id);
	}

	@GetMapping(value = "/{id}/html")
	public String html(@PathVariable String id, @RequestParam Map<String, String> requestParams) {
		DocumentEntity document = service.getById(id);
		return templateService.processAsHtml(requestParams, document.getTemplate());
	}
}
