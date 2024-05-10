package kt.tripsync.service;

import kt.tripsync.domain.Travel;
import kt.tripsync.dto.TravelDTO;
import kt.tripsync.exception.DuplicateBookmarkException;
import kt.tripsync.repository.BookmarkRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    BookmarkRepository bookmarkRepository;

    @InjectMocks
    BookmarkServiceImpl bookmarkService;

    @Test
    @DisplayName("북마크 추가 요청 시, 정상적으로 북마크 추가가 완료된다.")
    void createBookmarkSuccessTest() {
        //given
        Long userId = 1L;

        TravelDTO mockTravelDTO = TravelDTO.builder()
                .name("name")
                .address("address")
                .thumbnail("thumbnail")
                .mapX(0.0)
                .mapY(0.0)
                .build();

        //when, then
        Assertions.assertThatCode(() -> bookmarkService.createBookmark(userId, mockTravelDTO)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("북마크 중복 추가 요청 시, DuplicateBookmarkException 예외가 발생한다.")
    void createBookmarkFailTest() {
        //given
        Long userId = 1L;

        TravelDTO mockTravelDTO = TravelDTO.builder()
                .name("name")
                .address("address")
                .thumbnail("thumbnail")
                .mapX(0.0)
                .mapY(0.0)
                .build();

        doThrow(DuplicateBookmarkException.class).when(bookmarkRepository).createBookmark(anyLong(), any());

        //when, then
        Assertions.assertThatThrownBy(() -> bookmarkService.createBookmark(userId, mockTravelDTO)).isInstanceOf(DuplicateBookmarkException.class);
    }



    @Test
    @DisplayName("북마크 삭제 요청 시, 예외 없이 북마크 삭제가 완료된다.")
    void deleteBookmarkSuccessTest() {
        //given
        Long userId = 1L;
        Long travelId = 1L;

        //when, then
        Assertions.assertThatCode(() -> bookmarkService.deleteBookmark(userId, travelId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("북마크 조회 요청 시, 정상적으로 북마크 조회가 성공한다.")
    void getBookmarkSuccessTest() {
        //given
        Long userId = 1L;
        List<Travel> travelList = new ArrayList<>();

        for (long i = 0L; i < 3L; i++) {
            Travel travel = Travel.builder()
                    .address("address" + i)
                    .id(i)
                    .name("name" + i)
                    .mapX(0.0)
                    .mapY(0.0)
                    .thumbnail("thumbnail" + i)
                    .build();

            travelList.add(travel);
        }

        when(bookmarkRepository.getBookmark(anyLong())).thenReturn(travelList);

        //when
        List<TravelDTO> bookmarkList = bookmarkService.getBookmark(userId);

        //then
        Assertions.assertThat(bookmarkList).extracting("name").containsExactlyElementsOf(travelList.stream().map(Travel::getName).toList());
    }
}