package kt.tripsync.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Travel extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String address;

    private String thumbnail;

    private Double mapX;

    private Double mapY;

    @OneToMany(mappedBy = "travel", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<UserTravel> userTravelList = new ArrayList<>();

    @OneToMany(mappedBy = "travel", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<PlanTravel> planTravelList = new ArrayList<>();

    @Builder
    public Travel(Long id, String name, String address, String thumbnail, Double mapX, Double mapY) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.thumbnail = thumbnail;
        this.mapX = mapX;
        this.mapY = mapY;
    }
}
