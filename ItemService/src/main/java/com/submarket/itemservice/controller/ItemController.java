package com.submarket.itemservice.controller;

import com.submarket.itemservice.dto.ItemDto;
import com.submarket.itemservice.service.impl.ItemServiceImpl;
import com.submarket.itemservice.util.CmmUtil;
import com.submarket.itemservice.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl itemService;
    private final TokenUtil tokenUtil;

    @PostMapping(value = "/items", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> saveItem(@RequestHeader HttpHeaders headers, ItemDto itemDto) throws Exception {
        log.info(this.getClass().getName() + ".saveItem Start!");

        String sellerId = CmmUtil.nvl(tokenUtil.getUserIdByToken(headers));
        itemDto.setSellerId(sellerId);

        if (itemDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 정보 오류");
        }
        itemService.saveItem(itemDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("상품 등록 완료");
    }

    @GetMapping("/items")
    public ResponseEntity<Map<String, Object>> findAllItem() throws Exception {
        log.info(this.getClass().getName() + ".findAllItem Start");
        Map<String, Object> rMap = new HashMap<>();

        List<ItemDto> itemDtoList = itemService.findAllItem();

        rMap.put("response", itemDtoList);

        log.info(this.getClass().getName() + ".findAllItem End");
        return ResponseEntity.ok().body(rMap);
    }

    @GetMapping("/items/{itemSeq}")
    public ResponseEntity<ItemDto> findOneItem(@PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".findOneItem Start! (itemSeq : " + itemSeq + ")");

        ItemDto pDto = new ItemDto();
        pDto.setItemSeq(itemSeq);

        // 상품 정보 가져오기
        ItemDto itemDto = itemService.findItemInfo(pDto);

        log.info(this.getClass().getName() + ".findOneItem End!");
        return ResponseEntity.ok().body(itemDto);
    }

    @PostMapping("/items/{itemSeq}/off")
    public ResponseEntity<String> offItem(@PathVariable int itemSeq) throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemSeq(itemSeq);

        itemService.offItem(itemDto);
        return ResponseEntity.ok().body("비활성화 완료");
    }

    @PostMapping("/items/{itemSeq}/on")
    public ResponseEntity<String> onItem(@PathVariable int itemSeq) throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemSeq(itemSeq);

        itemService.onItem(itemDto);
        return ResponseEntity.ok().body("활성화 완료");
    }

    @PostMapping(value = "/items/modify/{itemSeq}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<String> modifyItem(ItemDto itemDto, @PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + ".modifyItem Start!");
        itemDto.setItemSeq(itemSeq);

        itemService.modifyItem(itemDto);

        return ResponseEntity.ok().body("상품 수정 완료");
    }

    @PostMapping("/items/{itemSeq}/up")
    public void upCount(@PathVariable int itemSeq, @RequestBody Map<String, Object> request) throws Exception {
        int userAge = Integer.parseInt(String.valueOf(request.get("userAge")));
        log.info(this.getClass().getName() + ".upCount Start!");
        itemService.upReadCount(itemSeq, userAge);

        log.info(this.getClass().getName() + ".upCount End!");
    }
}
