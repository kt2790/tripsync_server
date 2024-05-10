package kt.tripsync.controller;

import kt.tripsync.dto.response.ErrorResponseDTO;
import kt.tripsync.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FriendAlreadyExistsException.class)
    ResponseEntity<Object> friendAlreadyExistsExceptionHandler() {
        return ResponseEntity.status(UserErrorCode.FRIEND_ALREADY_EXISTS.getHttpStatus())
                .body(ErrorResponseDTO.builder()
                        .code(UserErrorCode.FRIEND_ALREADY_EXISTS.getHttpStatus().value())
                        .message(UserErrorCode.FRIEND_ALREADY_EXISTS.getMessage())
                        .build());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    ResponseEntity<Object> userAlreadyExistsExceptionHandler() {
        return ResponseEntity.status(UserErrorCode.USER_ALREADY_EXISTS.getHttpStatus())
                .body(ErrorResponseDTO.builder()
                        .code(UserErrorCode.FRIEND_ALREADY_EXISTS.getHttpStatus().value())
                        .message(UserErrorCode.FRIEND_ALREADY_EXISTS.getMessage())
                        .build());
    }

    @ExceptionHandler(LoginFailedException.class)
    ResponseEntity<Object> loginFailedExceptionHandler() {
        return ResponseEntity.status(UserErrorCode.LOGIN_FAILED.getHttpStatus())
                .body(ErrorResponseDTO.builder()
                        .code(UserErrorCode.LOGIN_FAILED.getHttpStatus().value())
                        .message(UserErrorCode.LOGIN_FAILED.getMessage())
                        .build());
    }

    @ExceptionHandler(SessionNotExistsException.class)
    ResponseEntity<Object> SessionNotExistExceptionHandler() {
        return ResponseEntity.status(UserErrorCode.SESSION_NOT_EXISTS_EXCEPTION.getHttpStatus().value())
                .body(ErrorResponseDTO.builder()
                        .code(UserErrorCode.SESSION_NOT_EXISTS_EXCEPTION.getHttpStatus().value())
                        .message(UserErrorCode.SESSION_NOT_EXISTS_EXCEPTION.getMessage())
                        .build());
    }

    @ExceptionHandler(DuplicateBookmarkException.class)
    ResponseEntity<Object> DuplicateBookmarkExceptionHandler() {
        return ResponseEntity.status(UserErrorCode.DUPLICATE_BOOKMARK_EXCEPTION.getHttpStatus().value())
                .body(ErrorResponseDTO.builder()
                        .code(UserErrorCode.DUPLICATE_BOOKMARK_EXCEPTION.getHttpStatus().value())
                        .message(UserErrorCode.DUPLICATE_BOOKMARK_EXCEPTION.getMessage())
                        .build());
    }


}
