package kt.tripsync.dto.request;

import kt.tripsync.dto.PlanDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreatePlanRequestDTO {
    private String sessionId;

    private PlanDTO planDTO;
}
