package kt.tripsync.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanDetail extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private LocalDateTime planDate;

    private String planContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @OneToMany(mappedBy = "planDetail", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<PlanTravel> planTravelList = new ArrayList<>();

    @Builder
    public PlanDetail(Long id, LocalDateTime planDate, String planContent) {
        this.id = id;
        this.planDate = planDate;
        this.planContent = planContent;
    }

    public void setPlan(Plan plan) {

        if (this.plan != null) {
            this.plan.getPlanDetailList().remove(this);
        }

        this.plan = plan;

        if (plan != null) {
            plan.getPlanDetailList().add(this);
        }
    }
}
