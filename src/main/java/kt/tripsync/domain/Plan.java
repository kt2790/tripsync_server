package kt.tripsync.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    private String title;

    @Builder
    public Plan(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PlanDetail> planDetailList = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PlanGroup> planGroupList = new ArrayList<>();


    public void setUser(User user) {
        if (this.user != null) {
            this.user.getPlanList().remove(this);
        }

        this.user = user;

        if (user != null) {
            user.getPlanList().add(this);
        }
    }
}
