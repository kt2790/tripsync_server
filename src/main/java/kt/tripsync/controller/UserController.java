package kt.tripsync.controller;

import kt.tripsync.dto.request.*;
import kt.tripsync.dto.response.LoginResponseDTO;
import kt.tripsync.dto.response.RegisterResultResponseDTO;
import kt.tripsync.exception.SessionNotExistsException;
import kt.tripsync.exception.UnauthorizedAccessException;
import kt.tripsync.service.UserService;
import kt.tripsync.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SessionManager sessionManager;

    @PostMapping("/user")
    ResponseEntity<Object> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        Long id = userService.register(registerRequestDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(RegisterResultResponseDTO.builder()
                        .id(id)
                        .build());
    }

    @RequestMapping("/login")
    ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequestDTO) {

        Long id = userService.login(loginRequestDTO.getUserId(), loginRequestDTO.getPassword());
        String sessionId = sessionManager.createSession(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(LoginResponseDTO
                        .builder()
                        .sessionId(sessionId)
                        .build());
    }

    @RequestMapping("/logout")
    ResponseEntity<Object> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        sessionManager.expireSession(logoutRequestDTO.getSessionId());
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/user/friend")
    ResponseEntity<Object> addFriend(@RequestBody AddFriendRequestDTO addFriendRequestDTO) {

        Long id = sessionManager.getUidBySessionId(addFriendRequestDTO.getSessionId()).orElseThrow(SessionNotExistsException::new);

        userService.addFriend(id, addFriendRequestDTO.getFriendId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);

    }

    @DeleteMapping("/user/friend")
    ResponseEntity<Object> deleteFriend(@RequestBody DeleteFriendRequestDTO deleteFriendRequestDTO) {

        Long id = sessionManager.getUidBySessionId(deleteFriendRequestDTO.getSessionId()).orElseThrow(SessionNotExistsException::new);

        userService.deleteFriend(id, deleteFriendRequestDTO.getFriendId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }
}