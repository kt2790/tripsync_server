package kt.tripsync.controller;

import kt.tripsync.dto.PlanDTO;
import kt.tripsync.dto.request.CreatePlanRequestDTO;
import kt.tripsync.dto.request.DeletePlanRequestDTO;
import kt.tripsync.dto.request.GetPlanRequestDTO;
import kt.tripsync.dto.request.UpdatePlanRequestDTO;
import kt.tripsync.dto.response.CreatePlanResponseDTO;
import kt.tripsync.dto.response.GetPlanResponseDTO;
import kt.tripsync.exception.SessionNotExistsException;
import kt.tripsync.exception.UnauthorizedAccessException;
import kt.tripsync.service.PlanService;
import kt.tripsync.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PlanController {

    private final SessionManager sessionManager;
    private final PlanService planService;

    @PostMapping("/user/plan")
    ResponseEntity<Object> createPlan(@RequestBody CreatePlanRequestDTO createPlanRequestDTO) {

        String sessionId = createPlanRequestDTO.getSessionId();
        Long id = sessionManager.getUidBySessionId(sessionId).orElseThrow(SessionNotExistsException::new);


        Long planId = planService.createPlan(id, createPlanRequestDTO.getPlanDTO());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CreatePlanResponseDTO.builder()
                        .id(planId)
                        .build());
    }

    @PutMapping("/user/plan")
    ResponseEntity<Object> updatePlan(@RequestBody UpdatePlanRequestDTO updatePlanRequestDTO) {
        String sessionId = updatePlanRequestDTO.getSessionId();
        Long id = sessionManager.getUidBySessionId(sessionId).orElseThrow(SessionNotExistsException::new);

        planService.updatePlan(id, updatePlanRequestDTO.getPlanDTO());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

    @GetMapping("/user/plan")
    ResponseEntity<Object> getPlanList(@RequestBody GetPlanRequestDTO getPlanRequestDTO) {

        String sessionId = getPlanRequestDTO.getSessionId();
        Long id = sessionManager.getUidBySessionId(sessionId).orElseThrow(SessionNotExistsException::new);

        List<PlanDTO> planDTOList = planService.getPlanByUid(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GetPlanResponseDTO.builder()
                        .planDTOList(planDTOList)
                        .build());
    }

    @DeleteMapping("/user/plan")
    ResponseEntity<Object> deletePlan(@RequestBody DeletePlanRequestDTO deletePlanRequestDTO) {

        String sessionId = deletePlanRequestDTO.getSessionId();
        Long id = sessionManager.getUidBySessionId(sessionId).orElseThrow(SessionNotExistsException::new);

        planService.deletePlan(id, deletePlanRequestDTO.getPlanId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }
}
