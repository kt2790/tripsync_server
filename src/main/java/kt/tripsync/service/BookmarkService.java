package kt.tripsync.service;

import kt.tripsync.dto.TravelDTO;

import java.util.List;

public interface BookmarkService {

    void createBookmark(Long userId, TravelDTO targetTravel);

    void deleteBookmark(Long userId, Long travelId);

    List<TravelDTO> getBookmark(Long userId);
}
