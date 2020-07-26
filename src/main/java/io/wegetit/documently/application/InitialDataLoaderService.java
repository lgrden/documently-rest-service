package io.wegetit.documently.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wegetit.documently.document.DocumentEntity;
import io.wegetit.documently.document.DocumentEntityService;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("dev")
@AllArgsConstructor
public class InitialDataLoaderService {

	private final ObjectMapper mapper;
	private final DocumentEntityService document;
	private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	@PostConstruct
	private void initData() {
		document.initialize(readJsonFile(resolver.getResource("/data/documents.json"), DocumentEntity.class));
	}
	
	private <T> List<T> readJsonFile(Resource resource, Class<T> type) {
		if (!resource.exists()) {
			log.info("No initial resource found for " + type.getSimpleName());
			return Collections.emptyList();
		}
		try {
			long start = System.currentTimeMillis();
			List<T> list = mapper.readValue(resource.getInputStream(), mapper.getTypeFactory().constructCollectionType(List.class, type));
			long end = System.currentTimeMillis();
			log.info("Loaded " + list.size() + " elements of " + type.getSimpleName() + " in " + (end - start) + " ms.");
			return list;
		} catch (Exception e) {
			throw new IllegalStateException("Problem reading " + type.getSimpleName() + " from stream.", e);
		}
	}
}
