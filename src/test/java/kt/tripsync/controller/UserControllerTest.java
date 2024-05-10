package kt.tripsync.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kt.tripsync.AbstractRestDocsTests;
import kt.tripsync.dto.request.*;
import kt.tripsync.dto.response.LoginResponseDTO;
import kt.tripsync.dto.response.RegisterResultResponseDTO;
import kt.tripsync.repository.UserRepository;
import kt.tripsync.service.UserService;
import kt.tripsync.session.SessionManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest extends AbstractRestDocsTests {

    @MockBean
    private SessionManager sessionManager;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원가입 성공 시, 200 응답과 함께 회원번호가 조회된다")
    public void register200Test() throws Exception {

        //given
        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                .userId("${USER_ID}")
                .email("${EMAIL}")
                .profileImg("${PROFILE_IMG}")
                .password("${PASSWORD}")
                .nickname("${NICKNAME}")
                .build();

        String body = objectMapper.writeValueAsString(registerRequestDTO);

        //when
        when(userService.register(any(RegisterRequestDTO.class))).thenReturn(1L);

        MvcResult mvcResult = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("userId")
                                        .type(STRING)
                                        .description("유저 아이디")
                                        .attributes(constraints("아이디 형식")),
                                fieldWithPath("password")
                                        .type(STRING)
                                        .description("유저 패스워드")
                                        .attributes(constraints("8자 이상 20자 이하 최소 1글자 이상의 영어, 숫자, 특수문자 포함")),
                                fieldWithPath("nickname")
                                        .type(STRING)
                                        .description("유저 닉네임")
                                        .attributes(constraints("2자 이상 10자 이하 형식")),
                                fieldWithPath("profileImg")
                                        .type(STRING)
                                        .description("유저 프로필 이미지")
                                        .attributes(constraints("URL")),
                                fieldWithPath("email")
                                        .type(STRING)
                                        .description("유저 이메일")
                                        .attributes(constraints("이메일 형식"))

                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .type(NUMBER)
                                        .description("멤버 고유 번호")
                        )))
                .andExpect(status().isOk())
                .andReturn();

        //then
        RegisterResultResponseDTO registerResultResponseDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RegisterResultResponseDTO.class);

        Assertions.assertThat(registerResultResponseDTO.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그인 성공 시, 200 응답과 함께 sessionId 가 조회된다.")
    public void login200Test() throws Exception {
        //given
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .userId("${USER_ID}")
                .password("${PASSWORD}")
                .build();

        String body = objectMapper.writeValueAsString(loginRequestDTO);

        //when
        when(userService.login(anyString(), anyString())).thenReturn(1L);
        when(sessionManager.createSession(anyLong())).thenReturn("${SESSION_ID}");

        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("userId")
                                        .type(STRING)
                                        .description("유저 아이디")
                                        .attributes(constraints("아이디 형식")),
                                fieldWithPath("password")
                                        .type(STRING)
                                        .description("유저 패스워드")
                                        .attributes(constraints("8자 이상 20자 이하 최소 1글자 이상의 영어, 숫자, 특수문자 포함"))
                        ),
                        responseFields(
                                fieldWithPath("sessionId")
                                        .type(STRING)
                                        .description("세션 아이디")
                        )))
                .andExpect(status().isOk())
                .andReturn();

        //then
        LoginResponseDTO loginResponseDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LoginResponseDTO.class);
        Assertions.assertThat(loginResponseDTO.getSessionId()).isEqualTo("${SESSION_ID}");
    }

    @Test
    @DisplayName("로그아웃 성공 시, 200 응답을 받는다.")
    public void logout200Test() throws Exception {

        //given
        LogoutRequestDTO session = LogoutRequestDTO.builder()
                .sessionId("${SESSION_ID}")
                .build();
        String body = objectMapper.writeValueAsString(session);

        //when
        ResultActions result = mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("sessionId")
                                        .type(STRING)
                                        .description("세션 아이디")
                        )));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("친구추가 성공 시, 200 응답을 받는다.")
    public void addFriend200Test() throws Exception {

        //given
        AddFriendRequestDTO addFriendRequestDTO = AddFriendRequestDTO.builder()
                .sessionId("${SESSION_ID}")
                .friendId(2L)
                .build();

        String body = objectMapper.writeValueAsString(addFriendRequestDTO);

        //when
        when(sessionManager.getUidBySessionId(anyString())).thenReturn(Optional.of(1L));

        ResultActions result = mockMvc.perform(post("/user/friend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("sessionId")
                                        .type(STRING)
                                        .description("세션 아이디"),
                                fieldWithPath("friendId")
                                        .type(NUMBER)
                                        .description("친구 고유번호")
                        )));

        //then
        result.andExpect(status().isOk());

    }

    @Test
    @DisplayName("친구삭제 성공 시, 200 응답을 받는다.")
    public void deleteFriend200Test() throws Exception {

        //given
        DeleteFriendRequestDTO deleteFriendRequestDTO = DeleteFriendRequestDTO.builder()
                .sessionId("{SESSION_ID}")
                .friendId(2L)
                .build();

        String body = objectMapper.writeValueAsString(deleteFriendRequestDTO);

        //when
        when(sessionManager.getUidBySessionId(anyString())).thenReturn(Optional.of(1L));

        ResultActions result = mockMvc.perform(delete("/user/friend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("sessionId")
                                        .type(STRING)
                                        .description("세션 아이디"),
                                fieldWithPath("friendId")
                                        .type(NUMBER)
                                        .description("친구 고유번호")
                        )));

        //then
        result.andExpect(status().isOk());
    }

    private static Attributes.Attribute constraints(String value) {
        return new Attributes.Attribute("constraints", value);
    }

}