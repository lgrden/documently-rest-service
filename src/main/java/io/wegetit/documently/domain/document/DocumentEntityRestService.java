package io.wegetit.documently.domain.document;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.wegetit.documently.domain.template.TemplateService;
import io.wegetit.sau.core.errorhandler.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/documents")
public class DocumentEntityRestService {

	private final DocumentEntityService service;
	private final DocumentService documentService;
	private final TemplateService templateService;

	@Operation(summary = "List all documents")
	@ApiResponse(responseCode = "200", description = "Document list")
	@GetMapping
	public List<DocumentInfo> findAll() {
		return service.findAll();
	}

	@Operation(summary = "Get document by its id")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Found document", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = DocumentEntity.class))}),
		@ApiResponse(responseCode = "404", description = "Document not found", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
	})
	@GetMapping(value = "/{id}")
	public DocumentEntity getById(@Parameter(description = "id of document to be searched", required = true) @PathVariable String id) {
		return service.getById(id);
	}

	@Operation(summary = "Generates HTML document with parameters")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Document generated", content = {
					@Content(mediaType = "application/html", schema = @Schema(implementation = String.class))}),
			@ApiResponse(responseCode = "404", description = "Document not found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
	})
	@GetMapping(value = "/{id}/html")
	public String html(@Parameter(description = "id of document", required = true) @PathVariable String id,
					   @Parameter(description = "key/value parameters", example = "{\"name\": \"John\", \"surname\": \"Doe\"}") @RequestParam Map<String, String> requestParams) {
		DocumentEntity document = service.getById(id);
		return documentService.processAsHtml(document, requestParams);
	}
}
