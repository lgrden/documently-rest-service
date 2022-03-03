package io.wegetit.documently.domain.document;

import io.wegetit.documently.validation.ValidTemplate;
import io.wegetit.sau.core.validator.constraints.UUID;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "documents")
@TypeAlias("document")
@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DocumentEntity {

	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder(toBuilder = true)
	public static class DocumentField {

		@NotEmpty
		@Length(max = 64)
		private String name;

		@NotEmpty
		@Length(max = 1024)
		private String description;
	}

	@Id
	@NotNull
	@UUID
	private String id;
	
	@NotEmpty
	@Length(max = 64)
	private String name;
	
	@NotEmpty
	@Length(max = 1024)
	private String description;

	private List<DocumentField> fields = List.of();

	@NotEmpty
	private String content;

	@NotNull
	private LocalDateTime created;

	@ValidTemplate
	private String template;
}
