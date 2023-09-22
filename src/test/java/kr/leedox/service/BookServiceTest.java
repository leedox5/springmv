package kr.leedox.service;

import kr.leedox.book.Book;
import kr.leedox.book.BookService;
import kr.leedox.entity.WordMeaning;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    BookService bookService;

    @Mock
    WordMeaningService wordMeaningService;

    @Test
    void getBooksTest() {
        given(wordMeaningService.getWordMeanings(null)).willReturn(getStubWordMeanings());
        List<Book> books = bookService.getBooks(null, 1);
        System.out.println(books);
        assertEquals(1, books.size());
    }

    private List<WordMeaning> getStubWordMeanings() {
        WordMeaning wordMeaning1 = WordMeaning.builder().meaning("10010,시스템 관리,2,2,D").build();
        WordMeaning wordMeaning2 = WordMeaning.builder().meaning("10,영한 기본,1|2,2|1,A").build();
        return List.of(wordMeaning1, wordMeaning2);
    }

}
