package kt.tripsync.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kt.tripsync.AbstractRestDocsTests;
import kt.tripsync.dto.PlanDTO;
import kt.tripsync.dto.PlanDetailDTO;
import kt.tripsync.dto.PlanGroupDTO;
import kt.tripsync.dto.TravelDTO;
import kt.tripsync.dto.request.*;
import kt.tripsync.dto.response.*;
import kt.tripsync.repository.UserRepository;
import kt.tripsync.service.BookmarkService;
import kt.tripsync.service.PlanService;
import kt.tripsync.service.UserService;
import kt.tripsync.session.SessionManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookmarkController.class)
@ExtendWith(MockitoExtension.class)
class BookmarkControllerTest extends AbstractRestDocsTests {

    @MockBean
    private SessionManager sessionManager;

    @MockBean
    private BookmarkService bookmarkService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<TravelDTO> mockBookmarkList = List.of(TravelDTO.builder()
            .id(1L)
            .address("${ADDRESS}")
            .name("${NAME}")
            .mapX(11.53)
            .mapY(23.31)
            .thumbnail("${THUMBNAIL}")
            .build());

    @Test
    @DisplayName("북마크 생성 성공 시, 200 응답이 반환 된다.")
    void createBookmark200Test() throws Exception {

        //given
        CreateBookmarkRequestDTO createBookmarkRequestDTO = CreateBookmarkRequestDTO.builder()
                .sessionId("${SESSION_ID}")
                .travelDTO(mockBookmarkList.get(0))
                .build();

        String body = objectMapper.writeValueAsString(createBookmarkRequestDTO);

        //when
        when(sessionManager.getUidBySessionId(anyString())).thenReturn(Optional.of(1L));

        ResultActions result = mockMvc.perform(post("/user/bookmark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("sessionId")
                                        .type(STRING)
                                        .description("세션 아이디"),
                                fieldWithPath("travelDTO.id")
                                        .type(NUMBER)
                                        .description("여행지 고유번호")
                                        .optional(),
                                fieldWithPath("travelDTO.name")
                                        .type(STRING)
                                        .description("여행지 이름"),
                                fieldWithPath("travelDTO.address")
                                        .type(STRING)
                                        .description("여행지 주소"),
                                fieldWithPath("travelDTO.thumbnail")
                                        .type(STRING)
                                        .description("여행지 미리보기 URL"),
                                fieldWithPath("travelDTO.mapX")
                                        .type(NUMBER)
                                        .description("여행지 X 좌표"),
                                fieldWithPath("travelDTO.mapY")
                                        .type(NUMBER)
                                        .description("여행지 Y 좌표")

                        )));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("북마크 삭제 성공시, 200 응답이 반환 된다.")
    void deleteBookmark200Test() throws Exception {

        //given
        DeleteBookmarkRequestDTO deleteBookmarkRequestDTO = DeleteBookmarkRequestDTO.builder()
                .sessionId("${SESSION_ID}")
                .travelId(1L)
                .build();

        String body = objectMapper.writeValueAsString(deleteBookmarkRequestDTO);

        //when
        when(sessionManager.getUidBySessionId(anyString())).thenReturn(Optional.of(1L));

        ResultActions result = mockMvc.perform(delete("/user/bookmark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("sessionId")
                                        .type(STRING)
                                        .description("세션 아이디"),
                                fieldWithPath("travelId")
                                        .type(NUMBER)
                                        .description("여행지 고유번호")
                        )));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("북마크 조회 성공 시, 200 응답과 함께 북마크리스트가 반환 된다.")
    void getBookmark200Test() throws Exception {

        //given
        GetBookmarkRequestDTO getBookmarkRequestDTO = GetBookmarkRequestDTO.builder()
                .sessionId("${SESSION_ID}")
                .build();

        String body = objectMapper.writeValueAsString(getBookmarkRequestDTO);

        //when
        when(sessionManager.getUidBySessionId(anyString())).thenReturn(Optional.of(1L));
        when(bookmarkService.getBookmark(anyLong())).thenReturn(mockBookmarkList);

        MvcResult result = mockMvc.perform(get("/user/bookmark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("sessionId")
                                        .type(STRING)
                                        .description("세션 아이디")
                        ),
                        responseFields(
                                fieldWithPath("bookmarkList[].id")
                                        .type(NUMBER)
                                        .description("여행지 고유번호"),
                                fieldWithPath("bookmarkList[].name")
                                        .type(STRING)
                                        .description("여행지 이름"),
                                fieldWithPath("bookmarkList[].address")
                                        .type(STRING)
                                        .description("여행지 주소"),
                                fieldWithPath("bookmarkList[].thumbnail")
                                        .type(STRING)
                                        .description("여행지 미리보기 URL"),
                                fieldWithPath("bookmarkList[].mapX")
                                        .type(NUMBER)
                                        .description("여행지 X 좌표"),
                                fieldWithPath("bookmarkList[].mapY")
                                        .type(NUMBER)
                                        .description("여행지 Y 좌표")
                        )))
                .andExpect(status().isOk())
                .andReturn();

        //then
        GetBookmarkResponseDTO getBookmarkResponseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), GetBookmarkResponseDTO.class);
        Assertions.assertThat(getBookmarkResponseDTO.getBookmarkList().get(0).getId()).isEqualTo(1L);

    }

}