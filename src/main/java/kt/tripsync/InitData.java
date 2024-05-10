package kt.tripsync;

import kt.tripsync.dto.PlanDTO;
import kt.tripsync.dto.PlanDetailDTO;
import kt.tripsync.dto.PlanGroupDTO;
import kt.tripsync.dto.TravelDTO;
import kt.tripsync.dto.request.RegisterRequestDTO;
import kt.tripsync.service.PlanService;
import kt.tripsync.service.UserService;
import kt.tripsync.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitData {
    private final UserService userService;
    private final PlanService planService;
    private final SessionManager sessionManager;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {

        RegisterRequestDTO testUser = RegisterRequestDTO.builder()
                .userId("test")
                .email("test")
                .password("test")
                .profileImg("test")
                .nickname("test")
                .build();

        Long id = userService.register(testUser);

        for (long i = 0L; i < 100L; i++) {

            TravelDTO travelDTO = TravelDTO.builder()
                    .address("${ADDRESS}" + i)
                    .name("${NAME}" + i)
                    .mapX(11.53)
                    .mapY(23.31)
                    .thumbnail("${THUMBNAIL}" + i)
                    .build();

            ArrayList<TravelDTO> travelDTOList = new ArrayList<>();
            travelDTOList.add(travelDTO);

            PlanDetailDTO planDetailDTO = PlanDetailDTO.builder()
                    .travelDTOList(travelDTOList)
                    .planContent("${PLAN_CONTENT}" + i)
                    .planDate(LocalDateTime.now())
                    .build();

            ArrayList<PlanDetailDTO> planDetailDTOList = new ArrayList<>();
            planDetailDTOList.add(planDetailDTO);

            PlanGroupDTO planGroupDTO = PlanGroupDTO.builder()
                    .userId(123L)
                    .build();

            ArrayList<PlanGroupDTO> planGroupDTOList = new ArrayList<>();
            planGroupDTOList.add(planGroupDTO);

            PlanDTO testPlanDTO = PlanDTO.builder()
                    .title("${TITLE}" + i)
                    .planDetailDTOList(planDetailDTOList)
                    .planGroupDTOList(planGroupDTOList)
                    .build();

            planService.createPlan(1L, testPlanDTO);
        }

        System.out.println("-----------------------------------------");

        List<PlanDTO> planDTOList = planService.getPlanByUid(1L);

        System.out.println("planDTOList = " + planDTOList);
    }
}
