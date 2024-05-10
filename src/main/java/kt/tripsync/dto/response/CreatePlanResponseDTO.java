package kt.tripsync.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreatePlanResponseDTO {
    private Long id;
}
