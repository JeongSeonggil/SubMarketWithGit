package com.submarket.userservice.controller;

import com.submarket.userservice.dto.SubDto;
import com.submarket.userservice.jpa.entity.SubEntity;
import com.submarket.userservice.mapper.SubMapper;
import com.submarket.userservice.service.impl.SubServiceImpl;
import com.submarket.userservice.util.TokenUtil;
import com.submarket.userservice.vo.RequestSub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@Slf4j
@RequiredArgsConstructor
public class SubController {
    private final SubServiceImpl subServiceImpl;
    private final TokenUtil tokenUtil;

    @GetMapping("/sub")
    public ResponseEntity<Map<String, Object>> findAllSub(@RequestHeader HttpHeaders headers) throws Exception {
        log.info(this.getClass().getName() + ".findSub Start!");

        Map<String, Object> rMap = new HashMap<>();

        String userId = tokenUtil.getUserIdByToken(headers);


        SubDto subDto = new SubDto();
        subDto.setUserId(userId);
        List<SubEntity> subEntityList = subServiceImpl.findAllSub(subDto);

        List<SubDto> subDtoList = new ArrayList<>();

        subEntityList.forEach(subEntity -> {
            subDtoList.add(SubMapper.INSTANCE.subEntityToSubDto(subEntity));
        });

        rMap.put("response", subDtoList);

        return ResponseEntity.ok().body(rMap);



    }

    @GetMapping("/sub/{subSeq}")
    public ResponseEntity<SubDto> findOneSub(@PathVariable int subSeq) throws Exception {
        log.info(this.getClass().getName() + ".findOneSub Start!");
        SubDto pDto = new SubDto();

        pDto.setSubSeq(subSeq);

        SubDto subDto = subServiceImpl.findOneSub(pDto);

        if (subDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        log.info(this.getClass().getName() + ".findOneSub Start!");

        return ResponseEntity.ok().body(subDto);
    }

    @PostMapping("/sub")
    public ResponseEntity<String> createNewSub(@RequestHeader HttpHeaders headers,
                                               @RequestBody SubDto subDto) throws Exception{
        log.info(this.getClass().getName() + ".createNewSub Start!");

        String userId = tokenUtil.getUserIdByToken(headers);

        subDto.setUserId(userId);


        int res = subServiceImpl.createNewSub(subDto);

        if (res == 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복된 구독");
        }

        if (res != 1) {
            return ResponseEntity.status(500).body("오류");
        }

        log.info(this.getClass().getName() + ".createNewSub End! ");

        return ResponseEntity.status(HttpStatus.CREATED).body("구독 성공");
    }

    @PostMapping("/sub/delete")
    public String cancelSub(@RequestBody RequestSub requestSub) throws Exception {
        log.info(this.getClass().getName() + "cancel Sub Start!");

        SubDto subDto = new SubDto();

        subDto.setSubSeq(requestSub.getSubSeq());

        int res = subServiceImpl.cancelSub(subDto);


        log.info(this.getClass().getName() + "cancel Sub End!");

        if (res != 1) {
            return "구독 취소 실패";
        }
        return "구독 취소 성공";
    }

    @PostMapping("/sub/update")
    public ResponseEntity<String> updateSub(@RequestBody RequestSub requestSub) throws Exception {
        log.info(this.getClass().getName() + ".updateSub Start!");
        SubDto subDto = new SubDto();
        subDto.setSubSeq(requestSub.getSubSeq());

        int res = subServiceImpl.updateSub(subDto);

        if (res != 1) {
            return ResponseEntity.ok("갱신 실패");
        }

        log.info(this.getClass().getName() + "updateSub End!");
        return ResponseEntity.ok("갱신 완료");


    }

    @GetMapping("/seller/sub")
    public ResponseEntity<Integer> findSubCount(@RequestBody Map<String, Object> request)  throws Exception {
        // Seller 가 보유하고 있는 상품의 SeqList 를 넘겨주면 총 구독 수를 표시
        log.info(this.getClass().getName() + "findSubCount");
        List<Integer> itemSeqList = new LinkedList<>();
        itemSeqList = (List<Integer>) request.get("itemSeqList");

        int count = subServiceImpl.findSubCount(itemSeqList);

        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("/seller/sub/{itemSeq}")
    public ResponseEntity<Integer> findOneSubCount(@PathVariable int itemSeq) throws Exception {
        log.info(this.getClass().getName() + "findOneSubCount Start!");

        int count = subServiceImpl.findOneSubCount(itemSeq);

        log.info(this.getClass().getName() + "findOneSubCount End!");

        return ResponseEntity.ok().body(count);
    }
}
