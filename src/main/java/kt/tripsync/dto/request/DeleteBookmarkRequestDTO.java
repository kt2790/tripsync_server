package kt.tripsync.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DeleteBookmarkRequestDTO {

    private String sessionId;
    private Long travelId;
}
