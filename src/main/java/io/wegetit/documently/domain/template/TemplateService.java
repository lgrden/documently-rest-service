package io.wegetit.documently.domain.template;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TemplateService {

    private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public List<String> findAll() {
        try {
            return Arrays.stream(resolver.getResources("/templates/*.*")).map(p -> p.getFilename()).collect(
                Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }

    public boolean exists(String name) {
        return resolver.getResource("/templates/" + name).exists();
    }

    public Optional<String> findTemplateContent(String name) {
        if (StringUtils.isEmpty(name)) {
            return Optional.empty();
        }
        try {
            Resource resource = resolver.getResource("/templates/" + name);
            return Optional.of(IOUtils.toString(resource.getURL(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            return  Optional.empty();
        }
    }
}
