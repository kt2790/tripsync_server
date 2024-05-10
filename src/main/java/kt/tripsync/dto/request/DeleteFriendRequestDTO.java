package kt.tripsync.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DeleteFriendRequestDTO {
    private String sessionId;
    private Long friendId;
}
