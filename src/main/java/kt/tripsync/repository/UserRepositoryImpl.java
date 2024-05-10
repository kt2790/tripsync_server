package kt.tripsync.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kt.tripsync.domain.Friend;
import kt.tripsync.domain.QUser;
import kt.tripsync.domain.User;
import kt.tripsync.exception.FriendAlreadyExistsException;
import kt.tripsync.exception.UserAlreadyExistsException;
import kt.tripsync.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static kt.tripsync.domain.QFriend.friend;
import static kt.tripsync.domain.QUser.user;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Long register(String userId, String password, String nickname, String profileImg, String email) {
        User result = queryFactory.select(user)
                .from(user)
                .where(user.userId.eq(userId).or(user.nickname.eq(nickname)))
                .fetchFirst();

        if (result == null) {

            User user = User.builder()
                    .userId(userId)
                    .password(password)
                    .nickname(nickname)
                    .profileImg(profileImg)
                    .email(email)
                    .build();

            em.persist(user);

            return user.getId();
        }

        throw new UserAlreadyExistsException();
    }

    @Override
    public void unregister(Long id) {
        User user = em.find(User.class, id);
        em.remove(user);
    }

    @Override
    public void addFriend(Long curUserId, Long targetUserId) {
        Friend result = queryFactory.select(friend)
                .from(friend)
                .where(friend.user.id.eq(curUserId), friend.friendId.eq(targetUserId))
                .fetchFirst();

        if (result == null) {
            User curUser = em.find(User.class, curUserId);

            Friend friend = Friend.builder()
                    .friendId(targetUserId)
                    .build();

            friend.setUser(curUser);

            em.persist(friend);
        } else {
            throw new FriendAlreadyExistsException();
        }

    }


    @Override
    public void deleteFriend(Long curUserId, Long targetUserId) {
        queryFactory.delete(friend)
                .where(friend.user.id.eq(curUserId), friend.friendId.eq(targetUserId))
                .execute();
    }

    @Override
    public Optional<User> findUserById(Long id) {

        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findUserByUserId(String userId) {

        User findUser = queryFactory.select(user)
                .from(user)
                .where(user.userId.eq(userId))
                .fetchOne();

        return Optional.ofNullable(findUser);
    }

    @Override
    public Optional<User> findUserByNickname(String nickname) {

        User findUser = queryFactory.select(user)
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchOne();

        return Optional.ofNullable(findUser);
    }

}
