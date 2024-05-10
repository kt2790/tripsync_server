package kt.tripsync.dto.request;

import kt.tripsync.dto.TravelDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateBookmarkRequestDTO {

    private String sessionId;
    private TravelDTO travelDTO;
}
