package kt.tripsync.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kt.tripsync.domain.*;
import kt.tripsync.exception.DuplicateBookmarkException;
import kt.tripsync.exception.UserNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kt.tripsync.domain.QTravel.travel;
import static kt.tripsync.domain.QUserTravel.userTravel;

@Repository
public class BookmarkRepositoryImpl implements BookmarkRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public BookmarkRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long createBookmark(Long userId, Travel targetTravel) {

        User user = em.find(User.class, userId);

        if (user == null) {
            throw new UserNotFoundException();
        }

        Travel findTravel = queryFactory.select(travel)
                .from(travel)
                .where(travel.name.eq(targetTravel.getName()))
                .fetchFirst();

        UserTravel userTravelEntity = UserTravel.builder().build();
        userTravelEntity.setUser(user);

        if (findTravel != null) {

            UserTravel dup = queryFactory.select(userTravel)
                    .from(userTravel)
                    .where(userTravel.user.id.eq(userId), userTravel.travel.id.eq(findTravel.getId()))
                    .fetchFirst();

            if (dup != null) {
                throw new DuplicateBookmarkException();
            }

            userTravelEntity.setTravel(findTravel);
            em.persist(userTravelEntity);

            return findTravel.getId();
        } else {
            em.persist(targetTravel);
            userTravelEntity.setTravel(targetTravel);
            em.persist(userTravelEntity);

            return targetTravel.getId();
        }

    }

    @Override
    public void deleteBookmark(Long userId, Long travelId) {
        queryFactory.delete(userTravel)
                .where(userTravel.user.id.eq(userId), userTravel.travel.id.eq(travelId))
                .execute();
    }

    @Override
    public List<Travel> getBookmark(Long userId) {
        List<UserTravel> result = queryFactory.select(userTravel)
                .from(userTravel)
                .join(userTravel.travel, travel)
                .fetchJoin()
                .where(userTravel.user.id.eq(userId))
                .fetch();

        return result.stream()
                .map(UserTravel::getTravel)
                .toList();
    }


}
