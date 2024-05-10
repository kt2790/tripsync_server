package kt.tripsync.repository;

import kt.tripsync.domain.Travel;

import java.util.List;

public interface BookmarkRepository {

    Long createBookmark(Long userId, Travel targetTravel);

    void deleteBookmark(Long userId, Long travelId);

    List<Travel> getBookmark(Long userId);
}
