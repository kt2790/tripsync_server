package kt.tripsync.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AddFriendRequestDTO {
    private String sessionId;

    private Long friendId;
}
