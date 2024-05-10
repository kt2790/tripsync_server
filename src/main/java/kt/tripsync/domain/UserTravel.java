package kt.tripsync.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTravel extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @Builder
    public UserTravel(Long id) {
        this.id = id;
    }

    public void setUser(User user) {

        if (this.user != null) {
            this.user.getUserTravelList().remove(this);
        }

        this.user = user;

        if (user != null) {
            user.getUserTravelList().add(this);
        }
    }

    public void setTravel(Travel travel) {

        if (this.travel != null) {
            this.travel.getUserTravelList().remove(this);
        }

        this.travel = travel;

        if (travel != null) {
            travel.getUserTravelList().add(this);
        }
    }
}
