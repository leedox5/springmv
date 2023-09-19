package kr.leedox.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.leedox.common.ErrorResponse;
import kr.leedox.entity.WordMeaning;
import kr.leedox.entity.Wordbook;
import kr.leedox.service.WordMeaningService;
import kr.leedox.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/book")
public class BookRestController {

    private final ObjectMapper objectMapper;
    @Autowired
    WordService wordService;

    @Autowired
    WordMeaningService wordMeaningService;
    public BookRestController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping("/create/meaning")
    public ResponseEntity<?> createMeaning(@Valid @RequestBody WordMeaningDTO wordMeaningDTO, Errors errors) throws JsonProcessingException {
        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.ok(new ErrorResponse("404", "N", msg));
        }

        String requestJson = objectMapper.writeValueAsString(wordMeaningDTO);
        System.out.println(requestJson);

        Wordbook wordbook = wordService.getWordbook(wordMeaningDTO.getWordbookId());

        wordMeaningService.create(wordbook, wordMeaningDTO);
        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }

    @PostMapping("/save/meaning")
    public ResponseEntity<?> saveMeaning(@Valid @RequestBody WordMeaning wordMeaning, Errors errors) {
        if(errors.hasErrors()) {
            List<String> msg = errors.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.toList());
            return ResponseEntity.ok(new ErrorResponse("404", "N", msg));
        }
        wordMeaningService.save(wordMeaning);
        return ResponseEntity.ok(new ErrorResponse("200", "Y", "OK"));
    }
}
