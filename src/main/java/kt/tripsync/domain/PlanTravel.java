package kt.tripsync.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanTravel extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_detail_id")
    private PlanDetail planDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    private Integer travelOrder;

    @Builder
    public PlanTravel(Long id, Integer travelOrder) {
        this.id = id;
        this.travelOrder = travelOrder;
    }

    public void setPlanDetail(PlanDetail planDetail) {

        if (this.planDetail != null) {
            this.planDetail.getPlanTravelList().remove(this);
        }

        this.planDetail = planDetail;

        if (planDetail != null) {
            planDetail.getPlanTravelList().add(this);
        }
    }

    public void setTravel(Travel travel) {

        if (this.travel != null) {
            this.travel.getPlanTravelList().remove(this);
        }

        this.travel = travel;

        if (travel != null) {
            travel.getPlanTravelList().add(this);
        }
    }
}
