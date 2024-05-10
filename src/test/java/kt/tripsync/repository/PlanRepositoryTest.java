package kt.tripsync.repository;

import jakarta.persistence.EntityManager;
import kt.tripsync.domain.Plan;
import kt.tripsync.dto.PlanDTO;
import kt.tripsync.dto.PlanDetailDTO;
import kt.tripsync.dto.PlanGroupDTO;
import kt.tripsync.dto.TravelDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlanRepositoryTest {

    @TestConfiguration
    static class planRepositoryTestConfiguration {
        @Bean
        PlanRepository planRepository(EntityManager em) {
            return new PlanRepositoryImpl(em);
        }

        @Bean
        UserRepository userRepository(EntityManager em) {
            return new UserRepositoryImpl(em);
        }
    }

    @Autowired
    PlanRepository planRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("계획 생성 성공 시, 계획이 정상적으로 조회 된다.")
    void createPlanTest() {
        //given
        Long userId = userRepository.register("test", "test", "test", "test", "test");

        TravelDTO travelDTO = TravelDTO.builder()
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

        PlanDTO mockPlanDTO = PlanDTO.builder()
                .title("${TITLE}")
                .planDetailDTOList(planDetailDTOList)
                .planGroupDTOList(planGroupDTOList)
                .build();

        //when
        Long planId = planRepository.createPlan(userId,
                mockPlanDTO.getPlanEntity(),
                mockPlanDTO.getPlanGroupEntityList(),
                mockPlanDTO.getPlanDetailEntityList(),
                mockPlanDTO.getTravelEntityList());

        Plan plan = planRepository.getPlanById(planId);

        //then
        Assertions.assertThat(plan.getTitle()).isEqualTo(mockPlanDTO.getTitle());
    }

    @Test
    @DisplayName("계획 삭제 성공 시, 해당 계획 조회에 실패한다.")
    void deletePlanTest() {

        Long userId = userRepository.register("test", "test", "test", "test", "test");

        //given
        TravelDTO travelDTO = TravelDTO.builder()
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

        PlanDTO mockPlanDTO = PlanDTO.builder()
                .title("${TITLE}")
                .planDetailDTOList(planDetailDTOList)
                .planGroupDTOList(planGroupDTOList)
                .build();

        //when
        Long planId = planRepository.createPlan(userId,
                mockPlanDTO.getPlanEntity(),
                mockPlanDTO.getPlanGroupEntityList(),
                mockPlanDTO.getPlanDetailEntityList(),
                mockPlanDTO.getTravelEntityList());

        planRepository.deletePlan(planId);

        Plan plan = planRepository.getPlanById(planId);

        //then
        Assertions.assertThat(plan).isNull();

    }




}