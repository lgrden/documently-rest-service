package io.wegetit.documently.domain.document;

import io.wegetit.documently.validation.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DocumentInfo {
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

    public static DocumentInfo of(DocumentEntity e) {
        return DocumentInfo.builder()
            .id(e.getId())
            .name(e.getName())
            .description(e.getDescription())
            .build();
    }
}
