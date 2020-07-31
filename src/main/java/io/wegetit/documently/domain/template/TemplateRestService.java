package io.wegetit.documently.domain.template;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/templates")
public class TemplateRestService {

    private final TemplateService templateService;

    @Operation(summary = "List all templates")
    @ApiResponse(responseCode = "200", description = "Template list")
    @GetMapping
    public List<String> findAll() {
        return templateService.findAll();
    }
}
