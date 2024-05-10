package kt.tripsync.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode{
    FRIEND_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 친구입니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "로그인에 실패 하였습니다."),
    SESSION_NOT_EXISTS_EXCEPTION(HttpStatus.BAD_REQUEST, "세션이 만료 되었습니다."),
    DUPLICATE_BOOKMARK_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 북마크에 추가된 여행지입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
