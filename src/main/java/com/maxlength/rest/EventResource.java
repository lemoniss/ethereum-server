package com.maxlength.rest;

import com.maxlength.aggregate.service.EventService;
import com.maxlength.spec.common.BaseResponse;
import com.maxlength.spec.vo.Event;
import com.maxlength.spec.vo.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.math.BigInteger;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ethereum/contracts/logs")
@Api(tags = "[블록체인] - 기록/열람 API", protocols = "http", produces = "application/json", consumes = "appliction/json")
public class EventResource {

    private final EventService eventService;

    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * 기록하기
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/{contractAddress}")
    @ApiOperation(value = "기록하기", notes = "기록하기")
    public ResponseEntity<Mono<Token.transactionHash>> write(
        @PathVariable("contractAddress") String contractAddress,
        @RequestBody @Valid Event.writeRequest request) throws Exception {
        return BaseResponse.ok(eventService.write(request, contractAddress));
    }

    /**
     * 영수증조회
     * @param transactionHash
     * @return
     * @throws Exception
     */
    @GetMapping("/receipt/{transactionHash}")
    @ApiOperation(value = "영수증조회", notes = "영수증조회")
    public ResponseEntity<Token.transactionReceipt> receipt(
        @PathVariable("transactionHash") String transactionHash) throws Exception {
        return BaseResponse.ok(eventService.receipt(transactionHash));
    }

    /**
     * 목록
     * @return
     * @throws Exception
     */
    @GetMapping("/{eventName}/{contractAddress}")
    @ApiOperation(value = "읽기", notes = "읽기")
    public ResponseEntity<List<Event.getEventList>> list(
        @PathVariable("eventName") String eventName,
        @PathVariable("contractAddress") String contractAddress,
        @ApiParam(value = "블록번호") @RequestParam(name= "blockNumber", required = false) BigInteger blockNumber) throws Exception {

        return BaseResponse.ok(eventService.list(eventName, contractAddress, blockNumber));
    }

    /**
     * 상세
     * @param blockNumber
     * @param transactionHash
     * @param eventName
     * @return
     */
    @Deprecated
    @GetMapping("/detail")
    @ApiOperation(value = "상세조회", notes = "상세조회")
    public ResponseEntity<Event.getEventDetail> detail(
        @ApiParam(value = "블록번호", required = true) @RequestParam(name= "blockNumber") BigInteger blockNumber,
        @ApiParam(value = "트랜잭션해시", required = true) @RequestParam(name= "transactionHash") String transactionHash,
        @ApiParam(value = "이벤트명", required = true) @RequestParam(name= "eventName") String eventName) {

        return BaseResponse.ok(eventService.detail(Event.detailRequest.builder()
            .blockNumber(blockNumber)
            .transactionHash(transactionHash)
            .eventName(eventName)
            .build()));
    }
}
