package kt.tripsync.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kt.tripsync.domain.*;
import kt.tripsync.exception.UserNotFoundException;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.util.List;


import static kt.tripsync.domain.QPlan.plan;
import static kt.tripsync.domain.QPlanDetail.planDetail;
import static kt.tripsync.domain.QPlanGroup.planGroup;
import static kt.tripsync.domain.QPlanTravel.planTravel;
import static kt.tripsync.domain.QTravel.travel;
import static kt.tripsync.domain.QUser.user;

@Repository
public class PlanRepositoryImpl implements PlanRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public PlanRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * @param userId
     * @param plan
     * @param planGroupList
     * @param planDetailList
     * @param travelList
     * @return 생성된 계획의 id
     */
    @Override
    public Long createPlan(Long userId, Plan plan, List<PlanGroup> planGroupList, List<PlanDetail> planDetailList, List<List<Travel>> travelList) {

        createUserPlan(userId, plan);

        for (int i = 0; i < planDetailList.size(); i++) {
            PlanDetail planDetail = planDetailList.get(i);
            List<Travel> travels = travelList.get(i);

            planDetail.setPlan(plan);
            em.persist(planDetail);

            for (int j = 0; j < travels.size(); j++) {
                Travel item = travels.get(j);

                Travel result = queryFactory.select(travel)
                        .from(travel)
                        .where(travel.name.eq(item.getName()))
                        .fetchFirst();

                if (result == null) {
                    em.persist(item);
                    PlanTravel planTravel = generatePlanTravel(planDetail, j, item);

                    em.persist(planTravel);
                } else {
                    PlanTravel planTravel = generatePlanTravel(planDetail, j, result);
                    em.persist(planTravel);
                }
            }
        }

        createPlanGroup(plan, planGroupList);

        return plan.getId();

    }

    @Override
    public void createPlanGroup(Plan plan, List<PlanGroup> planGroupList) {
        for (PlanGroup planGroup : planGroupList) {
            planGroup.setPlan(plan);
            em.persist(planGroup);
        }
    }

    @Override
    public Plan getPlanById(Long planId) {
        return queryFactory.select(plan)
                .from(plan)
                .where(plan.id.eq(planId))
                .fetchOne();
    }

    @Override
    public List<Plan> getPlanByUid(Long id) {
        return queryFactory.select(plan)
                .from(plan)
                .where(plan.user.id.eq(id))
                .fetch();
    }

    @Override
    public void createUserPlan(Long userId, Plan plan) {
        User targetUser = em.find(User.class, userId);

        if (targetUser == null) {
            throw new UserNotFoundException();
        }

        plan.setUser(targetUser);
        em.persist(plan);
    }

    @Override
    public Long updatePlan(Long userId, Long planId, Plan plan, List<PlanGroup> planGroupList, List<PlanDetail> planDetailList, List<List<Travel>> travelList) {
        deletePlan(planId);
        return createPlan(userId, plan, planGroupList, planDetailList, travelList);
    }

    @Override
    public void deletePlan(Long planId) {

        Plan targetPlan = em.find(Plan.class, planId);

        if (targetPlan != null) {
            em.remove(targetPlan);
        }
    }

    private static PlanTravel generatePlanTravel(PlanDetail planDetail, int j, Travel item) {
        PlanTravel planTravel = PlanTravel.builder()
                .travelOrder(j)
                .build();

        planTravel.setPlanDetail(planDetail);
        planTravel.setTravel(item);
        return planTravel;
    }
}
