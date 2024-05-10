package kt.tripsync.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long friendId;

    @Builder
    public Friend(Long id, Long friendId) {
        this.id = id;
        this.friendId = friendId;
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getFriendList().remove(this);
        }

        this.user = user;

        if (user != null) {
            user.getFriendList().add(this);
        }
    }
}
