package kt.tripsync.repository;

import jakarta.persistence.EntityManager;
import kt.tripsync.domain.Travel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookmarkRepositoryTest {

    @TestConfiguration
    static class bookmarkRepositoryTestConfiguration {
        @Bean
        BookmarkRepository bookmarkRepository(EntityManager em) {
            return new BookmarkRepositoryImpl(em);
        }

        @Bean
        UserRepository userRepository(EntityManager em) {
            return new UserRepositoryImpl(em);
        }
    }

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("북마크 추가 성공 시, 해당 북마크가 정상적으로 조회된다.")
    void createBookmarkTest() {
        //given
        Long userId = userRepository.register("test", "test", "test", "test", "test");

        Travel bookmark = Travel.builder()
                .name("test")
                .build();

        //when
        bookmarkRepository.createBookmark(userId, bookmark);

        //then
        List<Travel> bookmarkList = bookmarkRepository.getBookmark(userId);

        Assertions.assertThat(bookmarkList).extracting("name").containsExactly(bookmark.getName());
    }

    @Test
    @DisplayName("북마크 삭제 성공 시, 해당 북마크 조회에 실패한다.")
    void deleteBookmarkTest() {
        //given
        Long userId = userRepository.register("test", "test", "test", "test", "test");

        Travel bookmark = Travel.builder()
                .name("test")
                .build();

        //when
        Long bookmarkId = bookmarkRepository.createBookmark(userId, bookmark);
        bookmarkRepository.deleteBookmark(userId, bookmarkId);

        //then
        List<Travel> bookmarkList = bookmarkRepository.getBookmark(userId);

        Assertions.assertThat(bookmarkList).extracting("name").doesNotContain(bookmark.getName());
    }
}