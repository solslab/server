package kr.sols.domain.feedback.service;

import kr.sols.domain.feedback.dto.FeedbackCreatedRes;
import kr.sols.domain.feedback.dto.FeedbackDto;
import kr.sols.domain.feedback.dto.FeedbackPageDto;
import kr.sols.domain.feedback.entity.Feedback;
import kr.sols.domain.feedback.exception.FeedbackException;
import kr.sols.domain.feedback.repository.FeedbackRepository;
import kr.sols.domain.member.dto.MemberListDto;
import kr.sols.domain.member.dto.MemberPageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kr.sols.exception.ErrorCode.PAGE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    @Transactional
    public FeedbackCreatedRes createFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback(feedbackDto.getRating(), feedbackDto.getFeedbackContent());
        feedback = feedbackRepository.save(feedback);
        return new FeedbackCreatedRes(feedback.getId());
    }

    @Transactional(readOnly = true)
    public FeedbackPageDto getAllFeedbacks(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Feedback> feedbackPage = feedbackRepository.findAllByOrderByCreatedDateDesc(pageable);

        int totalPageNum = feedbackPage.getTotalPages();
        int currentPageNum = feedbackPage.getNumber() + 1;

        if (currentPageNum > totalPageNum || currentPageNum < 1) throw new FeedbackException(PAGE_NOT_FOUND);

        List<FeedbackDto> feedbacks = feedbackPage.getContent()
                .stream()
                .map(FeedbackDto::fromEntity)
                .toList();

        return FeedbackPageDto.builder()
                .feedbacks(feedbacks)
                .totalElements((int) feedbackPage.getTotalElements())
                .totalPages(totalPageNum)
                .currentPage(currentPageNum)
                .pageSize(pageSize)
                .build();
    }
}
