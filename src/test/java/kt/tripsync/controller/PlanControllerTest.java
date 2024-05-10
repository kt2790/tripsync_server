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
import kt.tripsync.dto.response.CreatePlanResponseDTO;
import kt.tripsync.dto.response.GetPlanResponseDTO;
import kt.tripsync.dto.response.LoginResponseDTO;
import kt.tripsync.dto.response.RegisterResultResponseDTO;
import kt.tripsync.repository.UserRepository;
import kt.tripsync.service.PlanService;
import kt.tripsync.service.UserService;
import kt.tripsync.session.SessionManager;
import org.assertj.core.api.Assertions;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlanController.class)
@ExtendWith(MockitoExtension.class)
class PlanControllerTest extends AbstractRestDocsTests {

    @MockBean
    private SessionManager sessionManager;

    @MockBean
    private PlanService planService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private PlanDTO mockPlanDTO;

    @BeforeEach
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());

        TravelDTO travelDTO = TravelDTO.builder()
                .id(1L)
                .address("${ADDRESS}")
                .name("${NAME}")
                .mapX(11.53)
                .mapY(23.31)
                .thumbnail("${THUMBNAIL}")
                .build();

        ArrayList<TravelDTO> travelDTOList = new ArrayList<>();
        travelDTOList.add(travelDTO);

        PlanDetailDTO planDetailDTO = PlanDetailDTO.builder()
                .travelDTOList(travelDTOList)
                .planContent("${PLAN_CONTENT}")
                .planDate(LocalDateTime.now())
                .build();

        ArrayList<PlanDetailDTO> planDetailDTOList = new ArrayList<>();
        planDetailDTOList.add(planDetailDTO);

        PlanGroupDTO planGroupDTO = PlanGroupDTO.builder()
                .userId(123L)
                .build();

        ArrayList<PlanGroupDTO> planGroupDTOList = new ArrayList<>();
        planGroupDTOList.add(planGroupDTO);

        mockPlanDTO = PlanDTO.builder()
                .title("${TITLE}")
                .planDetailDTOList(planDetailDTOList)
                .planGroupDTOList(planGroupDTOList)
                .build();
    }

    @Test
    @DisplayName("계획 생성 성공 시, 200 응답과 함께 planId가 반환된다")
    public void createPlan200Test() throws Exception {

        //given
        CreatePlanRequestDTO createPlanRequestDTO = CreatePlanRequestDTO.builder()
                .planDTO(mockPlanDTO)
                .sessionId("${SESSION_ID}")
                .build();

        String body = objectMapper.writeValueAsString(createPlanRequestDTO);


        //when
        when(sessionManager.getUidBySessionId(anyString())).thenReturn(Optional.of(1L));
        when(planService.createPlan(anyLong(), any())).thenReturn(1L);

        MvcResult mvcResult = mockMvc.perform(post("/user/plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("sessionId")
                                        .type(STRING)
                                        .description("세션 아이디"),
                                fieldWithPath("planDTO")
                                        .type(OBJECT)
                                        .description("계획 정보"),
                                fieldWithPath("planDTO.planId")
                                        .type(STRING)
                                        .description("계획 아이디")
                                        .optional(),
                                fieldWithPath("planDTO.title")
                                        .type(STRING)
                                        .description("계획 제목"),
                                fieldWithPath("planDTO.planDetailDTOList")
                                        .type(ARRAY)
                                        .description("상세 계획 리스트"),
                                fieldWithPath("planDTO.planGroupDTOList")
                                        .type(ARRAY)
                                        .description("동행 유저 리스트"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList")
                                        .type(ARRAY)
                                        .description("여행지 정보 리스트"),
                                fieldWithPath("planDTO.planGroupDTOList[].userId")
                                        .type(NUMBER)
                                        .description("동행 유저 고유번호"),
                                fieldWithPath("planDTO.planDetailDTOList[].planDate")
                                        .type(STRING)
                                        .description("여행 시작 시간"),
                                fieldWithPath("planDTO.planDetailDTOList[].planContent")
                                        .type(STRING)
                                        .description("상세 여행 내용"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].id")
                                        .type(NUMBER)
                                        .description("여행지 고유번호"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].name")
                                        .type(STRING)
                                        .description("여행지 이름"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].address")
                                        .type(STRING)
                                        .description("여행지 주소"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].thumbnail")
                                        .type(STRING)
                                        .description("여행지 미리보기 URL"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].mapX")
                                        .type(NUMBER)
                                        .description("여행지 X 좌표"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].mapY")
                                        .type(NUMBER)
                                        .description("여행지 Y 좌표")

                                ),
                        responseFields(
                                fieldWithPath("id")
                                        .type(NUMBER)
                                        .description("등록한 여행 계획 고유번호")
                        )))
                .andExpect(status().isOk())
                .andReturn();

        //then
        CreatePlanResponseDTO createPlanResponseDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreatePlanResponseDTO.class);

        Assertions.assertThat(createPlanResponseDTO.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("계획 수정 성공 시, 200 응답이 반환된다.")
    public void updatePlan200Test() throws Exception {

        //given
        mockPlanDTO.setPlanId(1L);

        UpdatePlanRequestDTO updatePlanRequestDTO = UpdatePlanRequestDTO.builder()
                .planDTO(mockPlanDTO)
                .sessionId("${SESSION_ID}")
                .build();

        String body = objectMapper.writeValueAsString(updatePlanRequestDTO);

        //when
        when(sessionManager.getUidBySessionId(anyString())).thenReturn(Optional.of(1L));
        when(planService.updatePlan(anyLong(), any())).thenReturn(1L);

        ResultActions result = mockMvc.perform(put("/user/plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("sessionId")
                                        .type(STRING)
                                        .description("세션 아이디"),
                                fieldWithPath("planDTO")
                                        .type(OBJECT)
                                        .description("계획 정보"),
                                fieldWithPath("planDTO.planId")
                                        .type(NUMBER)
                                        .description("계획 아이디"),
                                fieldWithPath("planDTO.title")
                                        .type(STRING)
                                        .description("계획 제목"),
                                fieldWithPath("planDTO.planDetailDTOList")
                                        .type(ARRAY)
                                        .description("상세 계획 리스트"),
                                fieldWithPath("planDTO.planGroupDTOList")
                                        .type(ARRAY)
                                        .description("동행 유저 리스트"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList")
                                        .type(ARRAY)
                                        .description("여행지 정보 리스트"),
                                fieldWithPath("planDTO.planGroupDTOList[].userId")
                                        .type(NUMBER)
                                        .description("동행 유저 고유번호"),
                                fieldWithPath("planDTO.planDetailDTOList[].planDate")
                                        .type(STRING)
                                        .description("여행 시작 시간"),
                                fieldWithPath("planDTO.planDetailDTOList[].planContent")
                                        .type(STRING)
                                        .description("상세 여행 내용"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].id")
                                        .type(NUMBER)
                                        .description("여행지 고유번호"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].name")
                                        .type(STRING)
                                        .description("여행지 이름"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].address")
                                        .type(STRING)
                                        .description("여행지 주소"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].thumbnail")
                                        .type(STRING)
                                        .description("여행지 미리보기 URL"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].mapX")
                                        .type(NUMBER)
                                        .description("여행지 X 좌표"),
                                fieldWithPath("planDTO.planDetailDTOList[].travelDTOList[].mapY")
                                        .type(NUMBER)
                                        .description("여행지 Y 좌표")

                        )));

        //then
        result.andExpect(status().isOk());

    }

    @Test
    @DisplayName("계획 삭제 성공 시, 200 응답이 반환된다.")
    public void deletePlan200Test() throws Exception {

        //given
        DeletePlanRequestDTO deletePlanRequestDTO = DeletePlanRequestDTO.builder()
                .sessionId("${SESSION_ID}")
                .planId(1L)
                .build();

        String body = objectMapper.writeValueAsString(deletePlanRequestDTO);

        //when
        when(sessionManager.getUidBySessionId(anyString())).thenReturn(Optional.of(1L));

        ResultActions result = mockMvc.perform(delete("/user/plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("sessionId")
                                        .type(STRING)
                                        .description("세션 아이디"),
                                fieldWithPath("planId")
                                        .type(NUMBER)
                                        .description("계획 고유번호")
                        )));

        //then
        result.andExpect(status().isOk());

    }

    @Test
    @DisplayName("계획 조희 성공 시, 200 응답과 함께 계획 리스트가 반환된다.")
    void getPlan200Test() throws Exception {
        //given
        GetPlanRequestDTO getPlanRequestDTO = GetPlanRequestDTO.builder()
                .sessionId("${SESSION_ID}")
                .build();

        String body = objectMapper.writeValueAsString(getPlanRequestDTO);

        ArrayList<PlanDTO> planDTOList = new ArrayList<>();
        mockPlanDTO.setPlanId(1L);

        planDTOList.add(mockPlanDTO);

        //when
        when(sessionManager.getUidBySessionId(anyString())).thenReturn(Optional.of(1L));
        when(planService.getPlanByUid(anyLong())).thenReturn(planDTOList);

        MvcResult mvcResult = mockMvc.perform(get("/user/plan")
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
                                fieldWithPath("planDTOList")
                                        .type(ARRAY)
                                        .description("계획 목록"),
                                fieldWithPath("planDTOList[].planId")
                                        .type(NUMBER)
                                        .description("계획 아이디")
                                        .optional(),
                                fieldWithPath("planDTOList[].title")
                                        .type(STRING)
                                        .description("계획 제목"),
                                fieldWithPath("planDTOList[].planDetailDTOList")
                                        .type(ARRAY)
                                        .description("상세 계획 리스트"),
                                fieldWithPath("planDTOList[].planGroupDTOList")
                                        .type(ARRAY)
                                        .description("동행 유저 리스트"),
                                fieldWithPath("planDTOList[].planDetailDTOList[].travelDTOList")
                                        .type(ARRAY)
                                        .description("여행지 정보 리스트"),
                                fieldWithPath("planDTOList[].planGroupDTOList[].userId")
                                        .type(NUMBER)
                                        .description("동행 유저 고유번호"),
                                fieldWithPath("planDTOList[].planDetailDTOList[].planDate")
                                        .type(STRING)
                                        .description("여행 시작 시간"),
                                fieldWithPath("planDTOList[].planDetailDTOList[].planContent")
                                        .type(STRING)
                                        .description("상세 여행 내용"),
                                fieldWithPath("planDTOList[].planDetailDTOList[].travelDTOList[].id")
                                        .type(NUMBER)
                                        .description("여행지 고유번호"),
                                fieldWithPath("planDTOList[].planDetailDTOList[].travelDTOList[].name")
                                        .type(STRING)
                                        .description("여행지 이름"),
                                fieldWithPath("planDTOList[].planDetailDTOList[].travelDTOList[].address")
                                        .type(STRING)
                                        .description("여행지 주소"),
                                fieldWithPath("planDTOList[].planDetailDTOList[].travelDTOList[].thumbnail")
                                        .type(STRING)
                                        .description("여행지 미리보기 URL"),
                                fieldWithPath("planDTOList[].planDetailDTOList[].travelDTOList[].mapX")
                                        .type(NUMBER)
                                        .description("여행지 X 좌표"),
                                fieldWithPath("planDTOList[].planDetailDTOList[].travelDTOList[].mapY")
                                        .type(NUMBER)
                                        .description("여행지 Y 좌표")
                        )))
                .andExpect(status().isOk())
                .andReturn();

        //then
        GetPlanResponseDTO getPlanResponseDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GetPlanResponseDTO.class);
        Assertions.assertThat(getPlanResponseDTO.getPlanDTOList().get(0).getPlanId()).isEqualTo(1L);
    }

    private static Attributes.Attribute constraints(String value) {
        return new Attributes.Attribute("constraints", value);
    }


}