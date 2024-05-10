package kt.tripsync.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "user_id", unique = true)
    private String userId;

    private String password;

    @Column(name = "nickname", unique = true)
    private String nickname;

    private String profileImg;

    @Column(name = "email", unique = true)
    private String email;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Friend> friendList = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Plan> planList = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<UserTravel> userTravelList = new ArrayList<>();

    @Builder
    public User(Long id, String userId, String password, String nickname, String profileImg, String email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.email = email;
    }

}
