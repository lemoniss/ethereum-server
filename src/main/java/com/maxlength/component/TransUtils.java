package com.maxlength.component;

import com.maxlength.spec.common.BaseException;
import com.maxlength.spec.vo.Event;
import com.maxlength.spec.vo.Wallet;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthSign;
import org.web3j.protocol.infura.InfuraHttpService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;
import com.maxlength.spec.vo.Token;

@Component
public class TransUtils {

    @Value("${blockchain.contractAddress}")
    private String contractAddress;

    @Value("${blockchain.ethereum.eventContractAddress}")
    private String eventContractAddress;

    @Value("${blockchain.ethereum.endPointNode}")
    private String endPointNode;

    @Value("${blockchain.ethereum.corpPrivateKey}")
    private String corpPrivateKey;

    @Value("${blockchain.ethereum.decimals}")
    private int decimals;

    private DefaultGasProvider gasProvider = new DefaultGasProvider();


    /**
     * 공통 트랜잭션 필수인수
     * @param address
     * @return
     * @throws Exception
     */
    public Token.txCountResponse txCount(String address) throws Exception {

        EthGetTransactionCount ethGetTransactionCount = web3j().ethGetTransactionCount(
            address, DefaultBlockParameterName.LATEST).sendAsync().get();

        return Token.txCountResponse.builder()
            .nonce(ethGetTransactionCount.getTransactionCount())
            .gasPrice(gasProvider.getGasPrice())
            .gasLimit(gasProvider.getGasLimit())
            .build();
    }

    /**
     * 공통 트랜잭션 회사지갑의 nonce 구해오기
     * @return
     * @throws Exception
     */
    public Token.txCountResponse txCorpAddressCount() throws Exception {

        EthGetTransactionCount ethGetTransactionCount = web3j().ethGetTransactionCount(
            corpCredentials().getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();

        return Token.txCountResponse.builder()
            .nonce(ethGetTransactionCount.getTransactionCount())
            .gasPrice(gasProvider.getGasPrice())
            .gasLimit(gasProvider.getGasLimit())
            .build();
    }

    /**
     * 공통 트랜잭션 call
     * @param txData
     * @return
     * @throws Exception
     */
    public List<Type> ethCallTx(Token.txRequest txData) throws Exception {

        Function function = new Function(
            txData.getFunctionName(),
            txData.getInputParameters().size() > 0 ? txData.getInputParameters() : Collections.emptyList(),
            txData.getOutputParameters().size() > 0 ? txData.getOutputParameters() : Collections.emptyList()
        );

        String functionEncoder = FunctionEncoder.encode(function);

        Transaction transaction = Transaction.createFunctionCallTransaction(
            txData.getAddress(),
            txData.getNonce(),
            gasProvider.getGasPrice(),
            gasProvider.getGasLimit(),
            contractAddress,
            functionEncoder);

        EthCall ethCall = web3j().ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();

        if(ethCall.hasError())
            throw new BaseException(ethCall.getError().getMessage());

        return FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
    }

    /**
     * 공통 트랜잭션 send
     * @param txData
     * @return
     * @throws Exception
     */
    public Token.transactionReceipt ethSendTx(Token.txRequest txData) throws Exception {

        Function function = new Function(
            txData.getFunctionName(),
            txData.getInputParameters().size() > 0 ? txData.getInputParameters() : Collections.emptyList(),
            txData.getOutputParameters().size() > 0 ? txData.getOutputParameters() : Collections.emptyList()
        );

        String functionEncoder = FunctionEncoder.encode(function);

        Transaction transaction = Transaction.createFunctionCallTransaction(
            txData.getAddress(),    // 트랜잭션을 실행하는 지갑주소
            txData.getNonce(),
            gasProvider.getGasPrice(),
            gasProvider.getGasLimit(),
            contractAddress,
            functionEncoder);

        EthSendTransaction transactionResponse = web3j().ethSendTransaction(transaction).sendAsync().get();

        if(transactionResponse.hasError())
            throw new BaseException(transactionResponse.getError().getMessage());

        return txReceipt(transactionResponse.getTransactionHash());
    }

    /**
     * 공통 서명한 트랜잭션 send
     * @param txData
     * @return
     * @throws Exception
     */
    public Token.transactionHash ethSendSignedTx(Token.txRequest txData) throws Exception {

        Function function = new Function(
            txData.getFunctionName(),
            txData.getInputParameters().size() > 0 ? txData.getInputParameters() : Collections.emptyList(),
            txData.getOutputParameters().size() > 0 ? txData.getOutputParameters() : Collections.emptyList()
        );

        String functionEncoder = FunctionEncoder.encode(function);

        String signedData = signTransaction(
            txData.getNonce(),
            gasProvider.getGasPrice(),
            gasProvider.getGasLimit(),
            contractAddress,
            BigInteger.ZERO,
            functionEncoder,
            corpCredentials()
            );

        EthSendTransaction transactionResponse = web3j().ethSendRawTransaction(signedData).sendAsync().get();

        if(transactionResponse.hasError())
            throw new BaseException(transactionResponse.getError().getMessage());

        return Token.transactionHash.builder().txHash(transactionResponse.getTransactionHash()).build();
    }

    /**
     * 공통 ERC20 전송 트랜잭션 send
     * @param request
     * @return
     * @throws Exception
     */
    public Token.transactionHash erc20Transfer(Token.transferRequest request) throws Exception {

        Token.txCountResponse txCount = txCount(request.getFrom());

        BigInteger sendBalance = multiplyTokenDecimals(request.getAmount());

        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(request.getTo()));
        inputParameters.add(new Uint256(sendBalance));

        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<Bool> typeReferenceBool = new TypeReference<>() {};
        outputParameters.add(typeReferenceBool);

        String functionName = "transfer";

        Token.txRequest txData = Token.txRequest.builder()
            .address(request.getFrom())
            .nonce(txCount.getNonce())
            .functionName(functionName)
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        return ethSendSignedTx(txData);

    }

    /**
     * 공통 대납전송 트랜잭션 send
     * @param request
     * @return
     * @throws Exception
     */
    public Token.transactionReceipt erc20DelegateTransfer(Token.transferRequest request) throws Exception {

        Token.txCountResponse txCount = txCount(request.getFrom());

        BigInteger sendBalance = multiplyTokenDecimals(request.getAmount());

        /**
         * 1. getNonce (delegate Smart Contract 에 정의해 놓은 custom nonce)
         */
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new Address(request.getFrom()));

        List<TypeReference<?>> outputParameters = new ArrayList<>();
        TypeReference<Uint256> typeReferenceUint256 = new TypeReference<>() {};
        outputParameters.add(typeReferenceUint256);

        String functionName = "getNonce";

        Token.txRequest txData = Token.txRequest.builder()
            .address(request.getFrom())
            .nonce(txCount.getNonce())
            .functionName(functionName)
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        BigInteger fromNonce = (BigInteger) ethCallTx(txData).get(0).getValue();

        /**
         * 2. create Pre Signed Hash
         */
        inputParameters = new ArrayList<>();
        inputParameters.add(new Address(request.getTo()));
        inputParameters.add(new Uint256(sendBalance));
        inputParameters.add(new Uint256(fromNonce));

        outputParameters = new ArrayList<>();
        TypeReference<Bytes32> typeReferenceByte32 = new TypeReference<>() {};
        outputParameters.add(typeReferenceByte32);

        functionName = "recoverPreSignedHash";

        txData = Token.txRequest.builder()
            .address(request.getFrom())
            .nonce(txCount.getNonce())
            .functionName(functionName)
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        byte[] preSignedHash = (byte[]) ethCallTx(txData).get(0).getValue();

        /**
         * 3. preSignedHash Sign fromAddress (최초 전송요청자)
         */
        EthSign signedHash = ethSignTx(request.getFrom(), preSignedHash);

        /**
         * 4. delegator 의 Nonce 가져오기
         */
        Token.txCountResponse txDelegatorCount = txCorpAddressCount();

        /**
         * 5. signed Transfer (from: delegateAddress)
         */
        inputParameters = new ArrayList<>();
        inputParameters.add(new DynamicBytes(new BigInteger(signedHash.getSignature().substring(2), 16).toByteArray()));
        inputParameters.add(new Address(request.getTo()));
        inputParameters.add(new Uint256(sendBalance));
        inputParameters.add(new Uint256(fromNonce));

        outputParameters = new ArrayList<>();
        TypeReference<Bool> typeReferenceBool = new TypeReference<>() {};
        outputParameters.add(typeReferenceBool);

        functionName = "transferPreSigned";

        txData = Token.txRequest.builder()
            .address(corpCredentials().getAddress())
            .nonce(txDelegatorCount.getNonce())
            .functionName(functionName)
            .inputParameters(inputParameters)
            .outputParameters(outputParameters)
            .build();

        return ethSendTx(txData);
    }

    /**
     * 공통 서명
     * @param address
     * @param preSignedHash
     * @return
     * @throws Exception
     */
    public EthSign ethSignTx(String address, byte[] preSignedHash) throws Exception{
        return web3j().ethSign(address, Numeric.toHexString(preSignedHash)).sendAsync().get();
    }

    /**
     * 공통 트랜잭션 영수증 리턴
     * @param txHash
     * @return
     * @throws Exception
     */
    public Token.transactionReceipt txReceipt(String txHash) throws Exception {
        EthGetTransactionReceipt receipt = web3j().ethGetTransactionReceipt(txHash).sendAsync().get();

        return Token.transactionReceipt.builder()
            .from(receipt.getResult().getFrom())
            .to(receipt.getResult().getTo())
            .gasUsed(new BigDecimal(String.valueOf(receipt.getResult().getGasUsed())).divide(new BigDecimal(10).pow(decimals)).toPlainString())
            .logs(receipt.getResult().getLogs())
            .build();
    }

    /**
     * 공통 10^18 곱하기
     * @param token
     * @return
     */
    public BigInteger multiplyTokenDecimals(BigDecimal token) {
        BigDecimal tokenDecimal = new BigDecimal(10).pow(decimals);
        BigDecimal computeToken = token.multiply(tokenDecimal);
        return computeToken.toBigInteger();
    }

    /**
     * 공통 10^18 나누기 (소수점2자리 반올림)
     * @param token
     * @param scale (소수점 n 자리)
     * @return
     */
    public BigDecimal devideTokenDecimals(BigInteger token, int scale) {
        BigDecimal tokenDecimal = new BigDecimal(10).pow(decimals);
        BigDecimal computeToken = new BigDecimal(token).divide(tokenDecimal, scale, RoundingMode.HALF_UP);
        return computeToken;
    }

    public List<Event.getEventList> ethEventList(String eventName, String contractAddress, BigInteger blockNumber) {

        org.web3j.abi.datatypes.Event event = new org.web3j.abi.datatypes.Event(eventName,
            Arrays.asList(
                new TypeReference<Address>(true) {},
                new TypeReference<Utf8String>(false) {},
                new TypeReference<Uint256>(false) {}
            )
        );

        String eventHash = EventEncoder.encode(event);

        DefaultBlockParameter fromBlock;
        if(blockNumber == null) {
            fromBlock = DefaultBlockParameterName.EARLIEST;
        } else {
            fromBlock = DefaultBlockParameter.valueOf(blockNumber.add(BigInteger.valueOf(1)));
        }

        // Filter
        EthFilter filter = new EthFilter(
            fromBlock,
            DefaultBlockParameterName.LATEST,
            contractAddress)
            .addSingleTopic(eventHash)
            ;

        List<Event.getEventList> responseList = new ArrayList<>();

        // Pull all the events for this contract
         web3j().ethLogFlowable(filter).subscribe(log -> {
            //            String eventHash = log.getTopics().get(0); // Index 0 is the event definition hash

            Address fromAddress = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Address>() {});

            List<Type> nonIndexParams = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());

            Event.getEventList res = Event.getEventList.builder()
                .blockNumber(log.getBlockNumber())
                .transactionHash(log.getTransactionHash())
                .address(fromAddress.toString())
                .contents((String) nonIndexParams.get(0).getValue())
                .regDt(LocalDateTime.ofInstant(Instant.ofEpochSecond(new BigInteger(String.valueOf(nonIndexParams.get(1).getValue())).longValue()), TimeZone
                    .getTimeZone("Asia/Seoul").toZoneId()))
                .build();

            responseList.add(res);

        });

        return responseList;

    }

    public Event.getEventDetail ethEventDetail(Event.detailRequest request) {

        org.web3j.abi.datatypes.Event event = new org.web3j.abi.datatypes.Event(request.getEventName(),
            Arrays.asList(
                new TypeReference<Address>(true) {},
                new TypeReference<Utf8String>(false) {},
                new TypeReference<Uint256>(false) {}
            )
        );

        String eventHash = EventEncoder.encode(event);

        // Filter
        EthFilter filter = new EthFilter(
            DefaultBlockParameter.valueOf(request.getBlockNumber()),
            DefaultBlockParameter.valueOf(request.getBlockNumber()),
            eventContractAddress)
            .addSingleTopic(eventHash)
            ;

        AtomicReference<Event.getEventDetail> details = new AtomicReference<>();

        // Pull all the events for this contract
        web3j().ethLogFlowable(filter).subscribe(log -> {
            //            String eventHash = log.getTopics().get(0); // Index 0 is the event definition hash
            if(request.getTransactionHash().equals(log.getTransactionHash())) {
                Address fromAddress = (Address) FunctionReturnDecoder.decodeIndexedValue(log.getTopics().get(1), new TypeReference<Address>() {});

                List<Type> nonIndexParams = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());

                Event.getEventDetail res = Event.getEventDetail.builder()
                    .blockNumber(log.getBlockNumber())
                    .transactionHash(log.getTransactionHash())
                    .address(fromAddress.toString())
                    .contents((String) nonIndexParams.get(0).getValue())
                    .regDt(LocalDateTime.ofInstant(Instant.ofEpochSecond(new BigInteger(String.valueOf(nonIndexParams.get(1).getValue())).longValue()), TimeZone
                        .getTimeZone("Asia/Seoul").toZoneId()))
                    .build();

                details.set(res);
            }
        });

        return details.get();
    }

    /**
     *     public static class makeWallet {
     *         private String address;
     *         private String privateKey;
     * @return
     */

    public Wallet.makeWallet makeWallet() throws Exception {

        String password = "password";
        ECKeyPair keyPair = Keys.createEcKeyPair();
        WalletFile wallet = org.web3j.crypto.Wallet.createStandard(password, keyPair);

        return Wallet.makeWallet.builder()
            .address("0x"+wallet.getAddress())
            .privateKey(keyPair.getPrivateKey().toString(16))
            .build();
    }


    private Web3j web3j() {
        return Web3j.build(new InfuraHttpService(endPointNode));
    }

    public Credentials corpCredentials() {
        return Credentials.create(corpPrivateKey);
    }

    private String signTransaction(
                    BigInteger nonce,
                    BigInteger gasPrice,
                    BigInteger gasLimit,
                    String to,
                    BigInteger value,
                    String data,
                    Credentials credentials) {

        RawTransaction rawTransaction = RawTransaction.createTransaction(
            nonce,
            gasPrice,
            gasLimit,
            to,
            value,
            data);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }
}


