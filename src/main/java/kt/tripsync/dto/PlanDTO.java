package kt.tripsync.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import kt.tripsync.domain.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PlanDTO {

    private Long planId;

    private String title;

    private List<PlanDetailDTO> planDetailDTOList;

    private List<PlanGroupDTO> planGroupDTOList;

    @JsonIgnore
    public Plan getPlanEntity() {
        return Plan.builder()
                .title(title)
                .build();
    }

    @JsonIgnore
    public List<PlanDetail> getPlanDetailEntityList() {
        return planDetailDTOList.stream()
                .map(PlanDetailDTO::toEntity)
                .toList();
    }

    @JsonIgnore
    public List<PlanGroup> getPlanGroupEntityList() {
        return planGroupDTOList.stream()
                .map(PlanGroupDTO::toEntity)
                .toList();
    }

    @JsonIgnore
    public List<List<Travel>> getTravelEntityList() {
        return planDetailDTOList.stream()
                .map(PlanDetailDTO::getTravelEntityList)
                .toList();
    }

    static public PlanDTO from(Plan plan) {
        return PlanDTO.builder()
                .title(plan.getTitle())
                .planId(plan.getId())
                .planDetailDTOList(
                        plan.getPlanDetailList().stream()
                                .map(PlanDetailDTO::from)
                                .toList())
                .planGroupDTOList(
                        plan.getPlanGroupList().stream()
                                .map(PlanGroupDTO::from)
                                .toList()
                ).build();
    }


}
