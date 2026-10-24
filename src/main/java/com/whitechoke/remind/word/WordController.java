package com.whitechoke.remind.word;

import com.whitechoke.remind.word.dto.WordCreateRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/words")
public class WordController {
    private final WordService wordService;

    @PostMapping
    public ResponseEntity<?> createWord(
            @Valid @RequestPart(value = "payload") WordCreateRequestDto request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @RequestPart(value = "pronunciation", required = false) MultipartFile pronunciationFile
            ) {
        var response = wordService.createWord(
                request,
                imageFile,
                pronunciationFile
        );

        return ResponseEntity.ok(response);
    }
}
