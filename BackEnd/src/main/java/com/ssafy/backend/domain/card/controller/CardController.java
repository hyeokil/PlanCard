package com.ssafy.backend.domain.card.controller;

import com.ssafy.backend.domain.card.dto.CardCreateRequestDto;
import com.ssafy.backend.domain.card.dto.CardResponseDto;
import com.ssafy.backend.domain.card.dto.CardUpdateMemoDto;
import com.ssafy.backend.domain.card.entity.Card;
import com.ssafy.backend.domain.card.service.CardService;
import com.ssafy.backend.global.common.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    //stt로 입력 받은 후 카드 생성, placeId받아서 처리하는 형식으로 해놓음
    @PostMapping("/{planId}/card/{placeId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> createCard(@PathVariable Long planId, @PathVariable Long placeId, @RequestBody CardCreateRequestDto cardCreateRequestDto) {
        cardService.createCard(planId, placeId, cardCreateRequestDto);
        return ResponseEntity.ok().body(Message.success());
    }

    //카드 전체조회
    @GetMapping("/{planId}/card")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<CardResponseDto>> getCards(@PathVariable Long planId) {
        List<Card> cards = cardService.getCardsByPlanId(planId);

        List<CardResponseDto> rslt = new ArrayList<>();
        for (Card card : cards) {
            rslt.add(new CardResponseDto(card.getId(), card.getPlace().getName(), card.getPlace().getAddress(), card.getMemo()));
        }
        return ResponseEntity.ok().body(rslt);
    }


    //카드 상세조회
    @GetMapping("/card/{cardId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<CardResponseDto> getCard(@PathVariable Long cardId) {
        //카드 db에 저장된 카드 아이디에 해당하는 카드 조회
        Card card = cardService.findById(cardId);
        return ResponseEntity.ok().body(new CardResponseDto(card.getId(), card.getPlace().getName(), card.getPlace().getAddress(), card.getMemo()));
    }

    @DeleteMapping("/card/{cardId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> deleteCard(@PathVariable Long cardId) {
        cardService.deleteById(cardId);
        return ResponseEntity.ok().body(Message.success());
    }

    @PatchMapping("/{planId}/card/{cardId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<String> updateMemo(@PathVariable Long planId, @PathVariable Long cardId, @RequestBody String updateMemo, CardUpdateMemoDto cardUpdateMemoDto) {

        cardService.updateMemo(planId, cardId, updateMemo, cardUpdateMemoDto);
        return ResponseEntity.ok().body("메모 수정 완료");
    }
}
