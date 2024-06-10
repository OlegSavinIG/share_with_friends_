package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
public class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    public void testSerialize() throws Exception {
        LocalDateTime created = LocalDateTime.of(2023, 6, 10, 10, 0);
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Great item!")
                .authorName("John Doe")
                .created(created)
                .build();

        assertThat(this.json.write(commentDto))
                .hasJsonPathNumberValue("$.id")
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(this.json.write(commentDto))
                .hasJsonPathStringValue("$.text")
                .extractingJsonPathStringValue("$.text").isEqualTo("Great item!");
        assertThat(this.json.write(commentDto))
                .hasJsonPathStringValue("$.authorName")
                .extractingJsonPathStringValue("$.authorName").isEqualTo("John Doe");
        assertThat(this.json.write(commentDto))
                .hasJsonPathStringValue("$.created")
                .extractingJsonPathStringValue("$.created").isEqualTo("2023-06-10T10:00:00");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"text\":\"Great item!\"," +
                "\"authorName\":\"John Doe\",\"created\":\"2023-06-10T10:00:00\"}";

        CommentDto commentDto = this.json.parse(content).getObject();

        assertThat(commentDto.getId()).isEqualTo(1L);
        assertThat(commentDto.getText()).isEqualTo("Great item!");
        assertThat(commentDto.getAuthorName()).isEqualTo("John Doe");
        assertThat(commentDto.getCreated()).isEqualTo(LocalDateTime.of(2023, 6, 10, 10, 0));
    }
}
