package kt.tripsync.repository;

import kt.tripsync.domain.Plan;
import kt.tripsync.domain.PlanDetail;
import kt.tripsync.domain.PlanGroup;
import kt.tripsync.domain.Travel;

import java.util.List;

public interface PlanRepository {

    Long createPlan(Long userId, Plan plan, List<PlanGroup> planGroupList, List<PlanDetail> planDetailList, List<List<Travel>> travelList);

    Long updatePlan(Long userId, Long planId, Plan plan, List<PlanGroup> planGroupList, List<PlanDetail> planDetailList, List<List<Travel>> travelList);
    void deletePlan(Long planId);

    void createUserPlan(Long userId, Plan plan);

    void createPlanGroup(Plan plan, List<PlanGroup> planGroupList);

    Plan getPlanById(Long planId);

    List<Plan> getPlanByUid(Long id);
}
