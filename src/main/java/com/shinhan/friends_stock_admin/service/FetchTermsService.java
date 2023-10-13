package com.shinhan.friends_stock_admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shinhan.friends_stock_admin.DTO.termGame.ResponseFromOpenAPIDTO;
import com.shinhan.friends_stock_admin.DTO.termGame.ResponseTermInfoDTO;
import com.shinhan.friends_stock_admin.domain.entity.TermQuizQuestion;
import com.shinhan.friends_stock_admin.repository.TermQuizItemRepository;
import com.shinhan.friends_stock_admin.repository.TermQuizQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class FetchTermsService {
    @Value("${GET_TERM_API_KEY}")
    private String serviceKey;
    private final TermQuizQuestionRepository termQuizQuestionRepository;
    private final TermQuizItemRepository termQuizItemRepository;

    //@Scheduled(cron = "0 12 * * *")
    public void getTermInfoFromOpenAPI(int value) throws JsonProcessingException {
        final String url = String.valueOf(UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.seibro.or.kr")
                .path("/openapi/service/FnTermSvc/getFinancialTermMeaning")
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows",value)
                .build());

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(url);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        try {
            final WebClient webClient = WebClient.builder()
                    .uriBuilderFactory(factory)
                    .baseUrl(url)
                    .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                    .build();

            final String response = webClient.get()
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JSONObject jsonObject = XML.toJSONObject(response);
            ObjectMapper mapper = new ObjectMapper();

            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            ResponseFromOpenAPIDTO json = mapper.readValue(jsonObject.toString(), ResponseFromOpenAPIDTO.class);

            termQuizQuestionRepository.saveAll(TermQuizQuestion.convertToTermquizList(json));
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
}
