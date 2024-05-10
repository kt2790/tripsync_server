package kt.tripsync.dto;

import kt.tripsync.domain.Friend;
import kt.tripsync.domain.User;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class FriendDTO {

    private Long id;

    private User user;

    private Long friendId;

    public Friend toEntity(User user) {
        Friend friend = Friend.builder()
                .id(id)
                .friendId(friendId)
                .build();

        friend.setUser(user);

        return friend;
    }
}
