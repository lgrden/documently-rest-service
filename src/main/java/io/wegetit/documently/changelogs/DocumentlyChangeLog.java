package io.wegetit.documently.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import io.wegetit.documently.domain.document.DocumentEntity;
import io.wegetit.documently.domain.document.DocumentEntityRepository;
import io.wegetit.sau.core.validator.IValidatorService;
import io.wegetit.sau.mongo.MongoJsonLoaderUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@ChangeLog(order = "001")
public class DocumentlyChangeLog {

    private static final Consumer<DocumentEntity> DOCUMENT_CONVERTER = p -> {
        p.setCreated(LocalDateTime.now());
    };

    @ChangeSet(order = "001", id = "loadDocuments", author = "grlu", runAlways = true)
    public void loadCurrencies(DocumentEntityRepository repository, IValidatorService validatorService) throws IOException {
        MongoJsonLoaderUtils.loadAllIfEmpty(repository, "documents.json", DocumentEntity.class, DOCUMENT_CONVERTER, validatorService.consumerValidator());
    }
}
