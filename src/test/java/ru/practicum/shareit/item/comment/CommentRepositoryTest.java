package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.item.ItemRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;
    private Item item;
    private Comment comment;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        commentRepository.deleteAll();

        user = User.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();
        user = userRepository.save(user);

        item = Item.builder()
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .user(user)
                .build();
        item = itemRepository.save(item);

        comment = Comment.builder()
                .text("Great item!")
                .author(user)
                .item(item)
                .createTime(LocalDateTime.now())
                .build();
        comment = commentRepository.save(comment);
    }

    @Test
    public void testSaveComment() {
        Comment savedComment = commentRepository.save(comment);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getText()).isEqualTo(comment.getText());
    }

    @Test
    public void testFindById() {
        Optional<Comment> foundComment = commentRepository.findById(comment.getId());

        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getText()).isEqualTo(comment.getText());
    }

    @Test
    public void testFindAll() {
        List<Comment> comments = commentRepository.findAll();

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getText()).isEqualTo(comment.getText());
    }

    @Test
    public void testDeleteById() {
        commentRepository.deleteById(comment.getId());

        Optional<Comment> deletedComment = commentRepository.findById(comment.getId());

        assertThat(deletedComment).isNotPresent();
    }
}
