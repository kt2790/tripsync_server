package kt.tripsync.controller;

import kt.tripsync.dto.TravelDTO;
import kt.tripsync.dto.request.CreateBookmarkRequestDTO;
import kt.tripsync.dto.request.DeleteBookmarkRequestDTO;
import kt.tripsync.dto.request.GetBookmarkRequestDTO;
import kt.tripsync.dto.response.GetBookmarkResponseDTO;
import kt.tripsync.exception.SessionNotExistsException;
import kt.tripsync.service.BookmarkService;
import kt.tripsync.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final SessionManager sessionManager;
    private final BookmarkService bookmarkService;

    @PostMapping("/user/bookmark")
    public ResponseEntity<Object> createBookmark(@RequestBody CreateBookmarkRequestDTO createBookmarkRequestDTO) {

        Long id = sessionManager.getUidBySessionId(createBookmarkRequestDTO.getSessionId()).orElseThrow(SessionNotExistsException::new);

        TravelDTO travelDTO = createBookmarkRequestDTO.getTravelDTO();
        travelDTO.setId(null);

        bookmarkService.createBookmark(id, travelDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @DeleteMapping("/user/bookmark")
    public ResponseEntity<Object> deleteBookmark(@RequestBody DeleteBookmarkRequestDTO deleteBookmarkRequestDTO) {

        Long id = sessionManager.getUidBySessionId(deleteBookmarkRequestDTO.getSessionId()).orElseThrow(SessionNotExistsException::new);

        bookmarkService.deleteBookmark(id, deleteBookmarkRequestDTO.getTravelId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }

    @GetMapping("/user/bookmark")
    public ResponseEntity<Object> getBookmark(@RequestBody GetBookmarkRequestDTO getBookmarkRequestDTO) {

        Long id = sessionManager.getUidBySessionId(getBookmarkRequestDTO.getSessionId()).orElseThrow(SessionNotExistsException::new);

        List<TravelDTO> bookmarkList = bookmarkService.getBookmark(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GetBookmarkResponseDTO.builder()
                        .bookmarkList(bookmarkList)
                        .build()
                );
    }
}
