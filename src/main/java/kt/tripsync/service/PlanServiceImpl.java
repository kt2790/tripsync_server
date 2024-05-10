package kt.tripsync.service;

import kt.tripsync.domain.Plan;
import kt.tripsync.domain.PlanDetail;
import kt.tripsync.domain.PlanGroup;
import kt.tripsync.domain.Travel;
import kt.tripsync.dto.PlanDTO;
import kt.tripsync.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@CacheConfig(cacheNames = "plan")
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    @Override
    @Transactional
    @CacheEvict(key = "#userId")
    public Long createPlan(Long userId, PlanDTO planDTO) {
        return planRepository.createPlan(userId,
                planDTO.getPlanEntity(),
                planDTO.getPlanGroupEntityList(),
                planDTO.getPlanDetailEntityList(),
                planDTO.getTravelEntityList());
    }

    @Override
    @Transactional
    @CacheEvict(key = "#userId")
    public Long updatePlan(Long userId, PlanDTO planDTO) {
        return planRepository.updatePlan(userId,
                planDTO.getPlanId(),
                planDTO.getPlanEntity(),
                planDTO.getPlanGroupEntityList(),
                planDTO.getPlanDetailEntityList(),
                planDTO.getTravelEntityList());
    }

    @Override
    @Transactional
    @CacheEvict(key = "#userId")
    public void deletePlan(Long userId, Long planId) {
        planRepository.deletePlan(planId);
    }

    @Override
    @Cacheable(key = "#id")
    public List<PlanDTO> getPlanByUid(Long id) {
        return planRepository.getPlanByUid(id)
                .stream()
                .map(PlanDTO::from)
                .toList();
    }
}
