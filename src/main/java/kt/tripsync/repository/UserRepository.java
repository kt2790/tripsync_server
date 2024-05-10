package kt.tripsync.repository;

import kt.tripsync.domain.User;

import java.util.Optional;

public interface UserRepository {

    Long register(String userId, String password, String nickname, String profileImg, String email);

    void unregister(Long id);

    void addFriend(Long curUserId, Long targetUserId);

    void deleteFriend(Long curUserId, Long targetUserId);

    Optional<User> findUserById(Long id);

    Optional<User> findUserByUserId(String userId);

    Optional<User> findUserByNickname(String nickname);


}
