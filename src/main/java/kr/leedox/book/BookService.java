package kr.leedox.book;

import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.service.WordMeaningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BookService {

    @Autowired
    WordMeaningService wordMeaningService;
    public List<Book> findAll() {
        return null;
    }

    public List<Book> getBooks(Wordbook wordbook, int id) {
        List<WordMeaning> wordMeanings = wordMeaningService.getWordMeanings(wordbook);

        List<Book> books = new ArrayList<>();
        for(WordMeaning wordMeaning : wordMeanings) {
            String[] str = wordMeaning.getMeaning().split(",");
            String[] auth = str[2].split("|");
            String[] active = str[3].split("|");
            if(Arrays.asList(auth).contains(Integer.toString(id))) {
                Book book = Book.builder().code(str[0]).name(str[1]).active(getActive (active, id)).build();
                books.add(book);
            }
        }
        return books;
    }

    private String getActive(String[] active, int id) {
        if(Arrays.asList(active).contains(Integer.toString(id))) {
            return "Y";
        } else {
            return "N";
        }
    }
}
