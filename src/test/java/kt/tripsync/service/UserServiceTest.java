package kt.tripsync.service;

import kt.tripsync.domain.User;
import kt.tripsync.dto.request.AddFriendRequestDTO;
import kt.tripsync.dto.request.RegisterRequestDTO;
import kt.tripsync.dto.request.UnregisterRequestDTO;
import kt.tripsync.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("회원가입 정상 요청 시, 회원가입 성공 및 회원 ID를 반환한다.")
    void registerSuccessTest() {
        //given
        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                .profileImg("profileImg")
                .userId("userId")
                .password("password")
                .email("email")
                .nickname("nickname")
                .build();

        Long userId = 1L;

        when(userRepository.register(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(userId);

        //when
        Long registerUserId = userServiceImpl.register(registerRequestDTO);


        //then
        Assertions.assertThat(registerUserId).isEqualTo(userId);
    }

    @Test
    @DisplayName("회원 탈퇴 정상 요청 시, 회원 탈퇴가 성공한다.")
    void unregister() {
        //given
        Long userId = 1L;

        //when, then
        Assertions.assertThatCode(() -> userServiceImpl.unregister(userId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("친구 추가 정상 요청 시, 친구 추가가 성공한다.")
    void addFriend() {
        //given
        Long userId = 1L;
        Long friendId = 2L;

        //when, then
        Assertions.assertThatCode(() -> userServiceImpl.addFriend(userId, friendId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("친구 삭제 정상 요청 시, 친구 삭제가 성공한다.")
    void deleteFriend() {
        //given
        Long userId = 1L;
        Long friendId = 2L;

        //when, then
        Assertions.assertThatCode(() -> userServiceImpl.deleteFriend(userId, friendId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("로그인 정상 요청 시, 로그인에 성공한다.")
    void login() {
        //given
        String userId = "id";
        String password = "pw";
        User user = User.builder()
                .userId("id")
                .password("pw")
                .build();

        when(userRepository.findUserByUserId(anyString())).thenReturn(Optional.of(user));

        //when, then
        Assertions.assertThatCode(() -> userServiceImpl.login(userId, password)).doesNotThrowAnyException();
    }
}