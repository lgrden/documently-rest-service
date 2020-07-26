package io.wegetit.documently.document;

import io.wegetit.documently.exception.EntityNotFoundException;
import io.wegetit.documently.validation.ValidatorService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

@AllArgsConstructor
@Service
public class DocumentEntityService {

	private static final Sort SORT = Sort.by("name").descending();

	private final ValidatorService validator;
	private final DocumentEntityRepository repository;

	public List<DocumentEntity> findAll() {
		return repository.findDocumentsLight(SORT);
	}
	
	public DocumentEntity getById(String id) {
		return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(format("Document %s not found.", id)));
	}

	public DocumentEntity save(DocumentEntity entity) {
		if (StringUtils.isEmpty(entity.getId())) {
			entity.setId(UUID.randomUUID().toString());
		}
		if (entity.getCreated() == null) {
			entity.setCreated(LocalDateTime.now());
		}
		validator.validate(entity);
		return repository.save(entity);
	}

	public void initialize(List<DocumentEntity> data) {
		data.forEach(p -> {
			p.setCreated(LocalDateTime.now());
			validator.validate(p);	
		});
		repository.saveAll(data.stream().filter(p -> !repository.findById(p.getId()).isPresent()).collect(Collectors.toList()));
	}
}
