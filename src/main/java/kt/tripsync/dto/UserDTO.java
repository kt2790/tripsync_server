package kt.tripsync.dto;

import kt.tripsync.domain.Friend;
import kt.tripsync.domain.Plan;
import kt.tripsync.domain.UserTravel;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String userId;

    private String password;

    private String nickname;

    private String profileImg;

    private String email;

    private List<Friend> friendList;

    private List<Plan> planList;

    private List<UserTravel> userTravelList;

}
