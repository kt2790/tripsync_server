package kt.tripsync.service;

import kt.tripsync.domain.Plan;
import kt.tripsync.domain.PlanDetail;
import kt.tripsync.domain.PlanGroup;
import kt.tripsync.domain.Travel;
import kt.tripsync.dto.PlanDTO;

import java.util.List;

public interface PlanService {

    Long createPlan(Long userId, PlanDTO planDTO);

    Long updatePlan(Long userId, PlanDTO planDTO);

    void deletePlan(Long userId, Long planId);

    List<PlanDTO> getPlanByUid(Long id);
}
