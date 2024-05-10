package kt.tripsync.service;

import kt.tripsync.domain.User;
import kt.tripsync.dto.request.RegisterRequestDTO;
import kt.tripsync.exception.LoginFailedException;
import kt.tripsync.exception.UserNotFoundException;
import kt.tripsync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    @Transactional
    public Long register(RegisterRequestDTO registerRequestDTO) {

        return userRepository.register(registerRequestDTO.getUserId(),
                registerRequestDTO.getPassword(),
                registerRequestDTO.getNickname(),
                registerRequestDTO.getProfileImg(),
                registerRequestDTO.getEmail());
    }

    @Override
    @Transactional
    public void unregister(Long id) {
        userRepository.unregister(id);
    }

    @Override
    @Transactional
    public void addFriend(Long curUserId, Long targetUserId) {
        userRepository.addFriend(curUserId, targetUserId);
    }

    @Override
    @Transactional
    public void deleteFriend(Long curUserId, Long targetUserId) {
        userRepository.deleteFriend(curUserId, targetUserId);
    }

    @Override
    public Long login(String userId, String password) {
        User user = userRepository.findUserByUserId(userId).orElseThrow(UserNotFoundException::new);

        if (user.getPassword().equals(password)) {
            return user.getId();
        } else {
            throw new LoginFailedException();
        }
    }


}
