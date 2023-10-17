package com.shinhan.friends_stock_admin.service.Admin;

import com.shinhan.friends_stock_admin.DTO.auth.Response;
import com.shinhan.friends_stock_admin.DTO.investGame.PostInvestQuestionDTO;
import com.shinhan.friends_stock_admin.DTO.termGame.PostTermQuestionDTO;
import com.shinhan.friends_stock_admin.DTO.termGame.PostTermQuestionOptionDTO;
import com.shinhan.friends_stock_admin.DTO.termGame.ResponseTermInfoDTO;
import com.shinhan.friends_stock_admin.domain.entity.InvestItem;
import com.shinhan.friends_stock_admin.domain.entity.StockNewsYear;
import com.shinhan.friends_stock_admin.domain.entity.TermQuizItem;
import com.shinhan.friends_stock_admin.domain.entity.TermQuizQuestion;
import com.shinhan.friends_stock_admin.repository.InvestItemNewsRepository;
import com.shinhan.friends_stock_admin.repository.InvestItemRepository;
import com.shinhan.friends_stock_admin.repository.TermQuizItemRepository;
import com.shinhan.friends_stock_admin.repository.TermQuizQuestionRepository;
import com.shinhan.friends_stock_admin.service.FetchData.FetchInvestInfoService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final TermQuizQuestionRepository termQuizQuestionRepository;
    private final TermQuizItemRepository termQuizItemRepository;

    private final InvestItemRepository investItemRepository;
    private final InvestItemNewsRepository investItemNewsRepository;

    private final FetchInvestInfoService fetchInvestInfoService;

    public Response<List<ResponseTermInfoDTO>> getTermAll() throws Exception {
        try {
            return Response.success(termQuizQuestionRepository.findByIsPublished(false).stream().map(ResponseTermInfoDTO::of).toList());
        }
        catch (Exception e){
            throw new Exception("주식 용어 목록 전체를 가져오는 데 실패하였습니다.");
        }
    }

    public Response<ResponseTermInfoDTO> getTerm(long id) throws Exception {
        try {
            return Response.success(ResponseTermInfoDTO.of(termQuizQuestionRepository.findById(id).orElseThrow()));
        }
        catch (Exception e){
            throw new Exception("주식 용어를 가져오는데 실패하였습니다.");
        }
    }

    @Transactional
    public Response<String> registerTermQuestion(PostTermQuestionDTO postTermQuestionDTO, Principal principal) throws Exception {
        try{
            //quiz 부분 업데이트
            TermQuizQuestion termQuizQuestion = termQuizQuestionRepository.findById(postTermQuestionDTO.getTermId()).orElseThrow();
            termQuizQuestion.updateExplanation(postTermQuestionDTO.getExplanation(), true, postTermQuestionDTO.getPlusPoint(), postTermQuestionDTO.getMinusPoint());

            for(PostTermQuestionOptionDTO dto: postTermQuestionDTO.getQuestionItem()){
                TermQuizItem termQuizItem = termQuizItemRepository.save(
                        TermQuizItem.builder()
                                .quizId(termQuizQuestion)
                                .content(dto.getQuestionOption())
                                .writerId(principal.getName())
                                .build());
                if(dto.getIsAnswer()){
                    termQuizQuestion.updateAnswerItemId(termQuizItem.getItemId());
                }
            }
        }
        catch (Exception e){
            throw new Exception("주식 용어 게임을 만드는데 실패하였습니다.");
        }
        return Response.success("요청 성공");
    }

    @Transactional
    public Response<String> registerInvestQuestion(PostInvestQuestionDTO postInvestQuestionDTO) throws Exception {
//        try {
            InvestItem investItem = investItemRepository.findById(postInvestQuestionDTO.getItemId()).orElseThrow();

            postInvestQuestionDTO.getNewList().forEach(newsInfo ->
                    newsInfo.getNews().forEach(news ->
                            investItemNewsRepository.save(StockNewsYear.of(investItem, newsInfo.getYear(), news.getTitle(), news.getUrl()))
                    )
            );
            investItem.updateInfo(postInvestQuestionDTO.getCompanyInfo(), postInvestQuestionDTO.getQuizStartYear(), true);

            //InvestItemNews에 추가하고, InvestItem에 내용 post한 내용 update

            //s3에 종가 upload
            fetchInvestInfoService.getCompanyStock(String.valueOf(investItem.getStockCode()), String.valueOf(postInvestQuestionDTO.getQuizStartYear()), String.valueOf(postInvestQuestionDTO.getQuizStartYear() + 6));
            return Response.success("요청 성공");
//        }catch (Exception e){
//            throw new Exception("문제 제출에 실패하였습니다.");
//        }
    }
}
