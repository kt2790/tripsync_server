package kt.tripsync.service;

import kt.tripsync.dto.TravelDTO;
import kt.tripsync.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    @Override
    @Transactional
    public void createBookmark(Long userId, TravelDTO targetTravel) {
        bookmarkRepository.createBookmark(userId, targetTravel.toEntity());
    }

    @Override
    @Transactional
    public void deleteBookmark(Long userId, Long travelId) {
        bookmarkRepository.deleteBookmark(userId, travelId);
    }

    @Override
    public List<TravelDTO> getBookmark(Long userId) {
        return bookmarkRepository.getBookmark(userId)
                .stream()
                .map(TravelDTO::from)
                .toList();
    }
}
