package kt.tripsync.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import kt.tripsync.domain.Plan;
import kt.tripsync.domain.PlanDetail;
import kt.tripsync.domain.PlanTravel;
import kt.tripsync.domain.Travel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PlanDetailDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime planDate;

    private String planContent;

    private List<TravelDTO> travelDTOList;

    public PlanDetail toEntity() {
        return PlanDetail.builder()
                .planDate(planDate)
                .planContent(planContent)
                .build();
    }

    @JsonIgnore
    public List<Travel> getTravelEntityList() {
        return travelDTOList.stream()
                .map(TravelDTO::toEntity)
                .toList();
    }

    static public PlanDetailDTO from(PlanDetail planDetail) {
        return PlanDetailDTO.builder()
                .planDate(planDetail.getPlanDate())
                .planContent(planDetail.getPlanContent())
                .travelDTOList(
                        planDetail.getPlanTravelList().stream()
                                .map((planTravel) -> TravelDTO.from(planTravel.getTravel()))
                                .toList()
                ).build();
    }
    
}

