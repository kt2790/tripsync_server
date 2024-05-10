package kt.tripsync.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private Integer code;
}
