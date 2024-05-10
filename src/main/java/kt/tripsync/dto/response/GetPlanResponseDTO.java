package kt.tripsync.dto.response;

import kt.tripsync.dto.PlanDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetPlanResponseDTO {
    List<PlanDTO> planDTOList;
}
