package com.shinhan.friends_stock_admin.service.FetchData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shinhan.friends_stock_admin.DTO.investGame.ResponseInvestOpenApiDTO;
import com.shinhan.friends_stock_admin.DTO.investGame.ResponseStockOpenApiDTO;
import com.shinhan.friends_stock_admin.DTO.termGame.ResponseFromOpenAPIDTO;
import com.shinhan.friends_stock_admin.controller.StockNotFoundException;
import com.shinhan.friends_stock_admin.domain.entity.InvestItem;
import com.shinhan.friends_stock_admin.domain.entity.StockReturnRate;
import com.shinhan.friends_stock_admin.repository.InvestItemRepository;
import com.shinhan.friends_stock_admin.repository.StockReturnRateRepository;
import com.shinhan.friends_stock_admin.utilty.S3UploadUtility;
import com.shinhan.friends_stock_admin.utilty.S3uploadData;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class FetchInvestInfoService {
    @Value("${GET_INVEST_API_KEY}")
    private String serviceKey;
    private final InvestItemRepository investItemRepository;
    private final StockReturnRateRepository stockReturnRateRepository;

    private final S3UploadUtility s3UploadUtility;

    public void getCompanyStock(InvestItem investItem, String startTime, String endTime) throws JsonProcessingException {
        String stockId = String.valueOf(investItem.getStockCode());
        final String url = String.valueOf(UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("fchart.stock.naver.com")
                .path("/sise.nhn")
                .queryParam("symbol",stockId)
                .queryParam("timeframe","day")
                .queryParam("requestType",1)
                .queryParam("startTime",startTime)
                .queryParam("endTime",endTime)
                .build());
        System.out.println(url);
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(url);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        final WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(url)
                .build();

        String response = webClient.get()
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject jsonObject = XML.toJSONObject(response);
        ObjectMapper mapper = new ObjectMapper();

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        ResponseStockOpenApiDTO json = mapper.readValue(jsonObject.toString(), ResponseStockOpenApiDTO.class);
        if (json.getProtocol() == null) {
            throw new StockNotFoundException("주가 정보가 없습니다.");
        }
        final int[] currentDate = {Integer.parseInt(startTime.substring(0, 4)) + 1};

        // 수익률
        List<StockReturnRate> stockReturnRates = new ArrayList<>(6);

        S3uploadData s3uploadData = new S3uploadData();
        List<S3uploadData.Data> list = new ArrayList<>();
        json.getProtocol().getChartdata().getItem().stream()
                .map(ResponseStockOpenApiDTO.Item::getData)
                .map(data -> data.split("\\|"))
                .forEach(parts -> {
                    if (parts.length >= 5) {
                        String date = parts[0];
                        String price = parts[4];
                        if(Integer.parseInt((date.substring(0,4))) >= currentDate[0]){
                            // 수익률 계산
                            int idx = stockReturnRates.size();
                            double rate = 0.0;
                            int lastPrice = Integer.parseInt(list.get(list.size() - 1).getPrice());
                            if (idx > 0) {
                                rate = calcRate(stockReturnRates.get(idx - 1).getLastPrice(), lastPrice);
                            }
                            StockReturnRate stockReturnRate = new StockReturnRate(
                                    investItem,
                                    Integer.parseInt(list.get(list.size() - 1).getDate().substring(0,4)),
                                    BigDecimal.valueOf(rate).setScale(2, BigDecimal.ROUND_HALF_UP),
                                    lastPrice
                            );
                            stockReturnRates.add(stockReturnRate);
                            // !수익률 계산

                            // S3 에 올리기
                            s3UploadUtility.upload(stockId + "-"+ Arrays.toString(new int[]{currentDate[0] - 1}),list.toString());

                            s3uploadData.setList(list);
                            list.clear();
                            currentDate[0]++;
                        }
                        list.add(new S3uploadData.Data(date,price));

                    } else {
                        System.out.println("파싱할 데이터가 충분하지 않습니다.");
                    }
                });

        // 수익률 계산
        int idx = stockReturnRates.size();
        double rate = 0.0;
        int lastPrice = Integer.parseInt(list.get(list.size() - 1).getPrice());
        if (idx > 0) {
            rate = calcRate(stockReturnRates.get(idx - 1).getLastPrice(), lastPrice);
        }
        StockReturnRate stockReturnRate = new StockReturnRate(
                investItem,
                Integer.parseInt(list.get(list.size() - 1).getDate().substring(0,4)),
                BigDecimal.valueOf(rate).setScale(2, BigDecimal.ROUND_HALF_UP),
                lastPrice
        );
        stockReturnRates.add(stockReturnRate);
        // !수익률 계산

        // S3 에 올리기
        s3UploadUtility.upload(stockId + "-"+ Arrays.toString(new int[]{currentDate[0] - 1}),list.toString());

        // 수익률 저장
        stockReturnRateRepository.saveAll(stockReturnRates);

    }

    private double calcRate(int prev, int cur) {
        return (cur - prev) / (double)prev * 100.0;
    }


    @Scheduled(cron = "0 0 12 * * *")
    public void getStockInfoFromOpenAPI() throws Exception {
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        try {
            fetchAndProcessData(1, sslContext);
            fetchAndProcessData(2, sslContext);
        }catch (Exception e){
            throw new Exception("api를 사용할 수 없습니다.");
        }
    }


    private void fetchAndProcessData(int queryType, SslContext sslContext) {
        String url = buildApiUrl(queryType);
        WebClient webClient = createWebClient(url, sslContext);
        ResponseInvestOpenApiDTO response = fetchData(webClient);
        if(!Objects.equals(response.getDataHeader().getSuccessCode(), "0")) {
            return;
        }
        for(ResponseInvestOpenApiDTO.DataBodyItem dataBodyItem: response.getDataBody()){
            if(!investItemRepository.existsByStockCode(Integer.parseInt(dataBodyItem.getStockCode())))
                investItemRepository.save(InvestItem.of(dataBodyItem));
        }

    }

    private String buildApiUrl(int queryType) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("gapi.shinhansec.com")
                .port(8443)
                .path("/openapi/v1.0/ranking/issue")
                .queryParam("query_type", queryType)
                .toUriString();
    }

    private WebClient createWebClient(String url, SslContext sslContext) {
        HttpClient httpClient = HttpClient.create().secure(provider -> provider.sslContext(sslContext));
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(url);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        return WebClient.builder()
                .uriBuilderFactory(factory)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("apiKey", serviceKey)
                .baseUrl(url)
                .build();
    }

    private ResponseInvestOpenApiDTO fetchData(WebClient webClient) {
        return webClient.get()
                .retrieve()
                .bodyToMono(ResponseInvestOpenApiDTO.class)
                .block();
    }


}
