package kt.tripsync.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RegisterRequestDTO {

    private String userId;

    private String password;

    private String nickname;

    private String profileImg;

    private String email;
}
