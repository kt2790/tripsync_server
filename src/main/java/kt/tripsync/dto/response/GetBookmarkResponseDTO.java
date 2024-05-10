package kt.tripsync.dto.response;

import kt.tripsync.dto.TravelDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetBookmarkResponseDTO {
    List<TravelDTO> bookmarkList;
}
