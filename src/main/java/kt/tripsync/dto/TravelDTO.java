package kt.tripsync.dto;

import kt.tripsync.domain.Travel;
import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TravelDTO {

    private Long id;

    private String name;

    private String address;

    private String thumbnail;

    private Double mapX;

    private Double mapY;

    public Travel toEntity() {
        return Travel.builder()
                .id(id)
                .name(name)
                .address(address)
                .thumbnail(thumbnail)
                .mapX(mapX)
                .mapY(mapY)
                .build();
    }

    static public TravelDTO from(Travel travel) {
        return TravelDTO.builder()
                .id(travel.getId())
                .name(travel.getName())
                .address(travel.getAddress())
                .mapX(travel.getMapX())
                .mapY(travel.getMapY())
                .thumbnail(travel.getThumbnail())
                .build();
    }

}
