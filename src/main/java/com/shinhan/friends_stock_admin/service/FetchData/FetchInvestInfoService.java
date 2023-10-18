package com.shinhan.friends_stock_admin.service.FetchData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shinhan.friends_stock_admin.DTO.investGame.ResponseInvestOpenApiDTO;
import com.shinhan.friends_stock_admin.DTO.investGame.ResponseStockOpenApiDTO;
import com.shinhan.friends_stock_admin.DTO.termGame.ResponseFromOpenAPIDTO;
import com.shinhan.friends_stock_admin.domain.entity.InvestItem;
import com.shinhan.friends_stock_admin.repository.InvestItemRepository;
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

    private final S3UploadUtility s3UploadUtility;

    public void getCompanyStock(String stockId, String startTime, String endTime) throws JsonProcessingException {
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
        ResponseStockOpenApiDTO json = mapper.readValue(jsonObject.toString(), ResponseStockOpenApiDTO.class);
        final int[] currentDate = {Integer.parseInt(startTime.substring(0, 4)) + 1};

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
                            s3uploadData.setList(list);
                            list.clear();
                            currentDate[0]++;
                        }
                        list.add(new S3uploadData.Data(date,price));
                        s3UploadUtility.upload(stockId + "-"+ Arrays.toString(new int[]{currentDate[0] - 1}),list.toString());

                        //date price s3에 올리기
                    } else {
                        System.out.println("파싱할 데이터가 충분하지 않습니다.");
                    }
                });
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
