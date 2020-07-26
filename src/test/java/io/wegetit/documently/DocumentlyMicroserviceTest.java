package io.wegetit.documently;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.wegetit.documently.document.DocumentEntity;
import io.wegetit.documently.document.DocumentEntityRestService;
import io.wegetit.documently.exception.EntityNotFoundException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles(profiles="dev")
public class DocumentlyMicroserviceTest {

    private static final String HELO_ID = "f53e2f36-12fd-40c2-9588-1f3c716ae52a";

    @Autowired
    private DocumentEntityRestService restService;

    @Test
    void findAll() {
        assertThat(restService.findAll()).hasSize(2);
    }

    @Test
    void getByIdFound() {
        DocumentEntity document = restService.getById(HELO_ID);
        assertNotNull(document);
        assertEquals("Say Hello", document.getName());
        assertEquals("Script saying hello.", document.getDescription());
        assertThat(document.getFields()).hasSize(1);
        assertEquals("name", document.getFields().get(0).getName());
        assertEquals("Say your name", document.getFields().get(0).getDescription());
    }

    @Test
    void getByIdNotFound() {
        assertThatThrownBy( () -> restService.getById("wrong_id")).isInstanceOf(
            EntityNotFoundException.class);
    }

    @Test
    void html() {
        assertEquals("<h1>Hello John Doe.</h1>",
            restService.html(HELO_ID, Map.of("name", "John Doe")));
        assertEquals("<h1>Hello .</h1>",
            restService.html(HELO_ID, Map.of("doesNotExist", "John Doe")));
    }
}
