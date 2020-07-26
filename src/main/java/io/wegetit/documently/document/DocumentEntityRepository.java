package io.wegetit.documently.document;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface DocumentEntityRepository extends MongoRepository<DocumentEntity, String> {

    @Query(value = "{}", fields="{ 'id' : 1, 'name' : 1, 'description' : 1, 'created' : 1}")
    List<DocumentEntity> findDocumentsLight(Sort sort);
}
