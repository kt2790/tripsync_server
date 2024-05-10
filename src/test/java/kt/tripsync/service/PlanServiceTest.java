package kt.tripsync.service;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kt.tripsync.domain.Plan;
import kt.tripsync.dto.PlanDTO;
import kt.tripsync.dto.PlanDetailDTO;
import kt.tripsync.dto.PlanGroupDTO;
import kt.tripsync.dto.TravelDTO;
import kt.tripsync.repository.PlanRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    PlanRepository planRepository;

    @InjectMocks
    PlanServiceImpl planServiceImpl;

    PlanDTO mockPlanDTO;

    @BeforeEach
    public void init() {
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
    @DisplayName("계획 생성 요청 시, 정상적으로 계획 생성이 완료된다.")
    void createPlanSuccessTest() {
        //given
        Long userId = 1L;
        Long planId = 1L;

        when(planRepository.createPlan(anyLong(), any(), any(), any(), any())).thenReturn(planId);

        //when
        Long createPlanId = planServiceImpl.createPlan(userId, mockPlanDTO);

        //then
        Assertions.assertThat(createPlanId).isEqualTo(planId);
    }

    @Test
    @DisplayName("계획 업데이트 요청 시, 정상적으로 계획 업데이트가 완료된다.")
    void updateSuccessPlanTest() {
        //given
        Long userId = 1L;
        Long planId = 1L;

        when(planRepository.updatePlan(any(), any(), any(), any(), any(), any())).thenReturn(planId);

        //when
        Long updatePlanId = planServiceImpl.updatePlan(userId, mockPlanDTO);

        //then
        Assertions.assertThat(updatePlanId).isEqualTo(planId);
    }

    @Test
    @DisplayName("계획 삭제 요청 시, 정상적으로 계획 삭제가 완료된다.")
    void deleteSuccessPlanTest() {
        //given
        Long userId = 1L;
        Long planId = 1L;

        //when, then
        Assertions.assertThatCode(() -> planServiceImpl.deletePlan(userId, planId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("계획 조회 요청 시, 정상적으로 계획이 조회된다.")
    void getPlanByUid() {
        //given
        Long userId = 1L;
        Long planId = 1L;

        Plan mockPlan = Plan.builder()
                .id(planId)
                .build();
        List<Plan> mockPlanList = new ArrayList<>();

        mockPlanList.add(mockPlan);

        when(planRepository.getPlanByUid(anyLong())).thenReturn(mockPlanList);

        //when
        List<PlanDTO> planList = planServiceImpl.getPlanByUid(userId);

        //then
        Assertions.assertThat(planList).extracting("planId").containsExactlyElementsOf(mockPlanList.stream().map(Plan::getId).toList());
    }
}