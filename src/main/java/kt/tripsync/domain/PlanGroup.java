package kt.tripsync.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanGroup extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Builder
    public PlanGroup(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public void setPlan(Plan plan) {

        if (this.plan != null) {
            this.plan.getPlanGroupList().remove(this);
        }

        this.plan = plan;

        if (plan != null) {
            plan.getPlanGroupList().add(this);
        }
    }
}
