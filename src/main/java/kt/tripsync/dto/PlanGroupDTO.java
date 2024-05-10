package kt.tripsync.dto;

import jakarta.persistence.*;
import kt.tripsync.domain.Plan;
import kt.tripsync.domain.PlanGroup;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PlanGroupDTO {

    private Long userId;

    public PlanGroup toEntity() {
        return PlanGroup.builder()
                .userId(userId)
                .build();
    }

    static public PlanGroupDTO from(PlanGroup planGroup) {
        return PlanGroupDTO.builder()
                .userId(planGroup.getUserId())
                .build();
    }
}
