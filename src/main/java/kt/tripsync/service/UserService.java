package kt.tripsync.service;

import kt.tripsync.dto.request.RegisterRequestDTO;

public interface UserService {

    Long register(RegisterRequestDTO registerRequestDTO);

    void unregister(Long id);

    void addFriend(Long curUserId, Long targetUserId);

    void deleteFriend(Long curUserId, Long targetUserId);

    Long login(String userId, String password);

}
