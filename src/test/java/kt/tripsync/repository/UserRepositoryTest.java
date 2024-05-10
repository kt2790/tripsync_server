package kt.tripsync.repository;


import jakarta.persistence.EntityManager;
import kt.tripsync.domain.User;
import kt.tripsync.exception.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {

    @TestConfiguration
    static class userRepositoryTestConfiguration {
        @Bean
        UserRepository userRepository(EntityManager em) {
            return new UserRepositoryImpl(em);
        }
    }

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원 가입 성공 시, 정상적으로 회원이 조회된다.")
    void registerSuccessTest() {
        //given
        String userId = "id";
        String password = "pw";
        String nickname = "nickname";
        String profileImg = "profileImg";
        String email = "email";

        //when
        userRepository.register(userId, password, nickname, profileImg, email);

        //then
        User findUser = userRepository.findUserByUserId(userId).orElseThrow(UserNotFoundException::new);
        Assertions.assertThat(findUser.getUserId()).isEqualTo(userId);

    }

    @Test
    @DisplayName("회원 가입 후 회원 탈퇴 성공 시, 회원 조회가 실패한다.")
    void unregisterSuccessTest() {
        //given
        String userId = "id";
        String password = "pw";
        String nickname = "nickname";
        String profileImg = "profileImg";
        String email = "email";

        //when
        Long id = userRepository.register(userId, password, nickname, profileImg, email);
        userRepository.unregister(id);

        //then
        Assertions.assertThatThrownBy(() -> userRepository.findUserByUserId(userId).orElseThrow(UserNotFoundException::new)).isInstanceOf(UserNotFoundException.class);
    }
}