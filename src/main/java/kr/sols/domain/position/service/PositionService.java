package kr.sols.domain.position.service;

import kr.sols.common.TypeValidator;
import kr.sols.domain.company.entity.Company;
import kr.sols.domain.company.exception.CompanyException;
import kr.sols.domain.company.repository.CompanyRepository;
import kr.sols.domain.company.service.CompanyService;
import kr.sols.domain.member.dto.MemberDto;
import kr.sols.domain.member.entity.Member;
import kr.sols.domain.position.Exception.PositionException;
import kr.sols.domain.position.dto.*;
import kr.sols.domain.position.entity.Position;
import kr.sols.domain.position.repository.PositionRepository;
import kr.sols.domain.position.repository.projection.SupportLanguagesProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static kr.sols.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PositionService {
    private final CompanyRepository companyRepository;
    private final PositionRepository positionRepository;
    private final CompanyService companyService;

    @Transactional
    public PositionCreatedResponse createPosition(UUID companyId, PositionReq request) {
        Company targetCompany = companyRepository.findById(companyId).orElseThrow(() -> new CompanyException(COMPANY_NOT_FOUND));
        if (positionRepository.existsByCompanyIdAndPositionName(companyId, request.getPositionName())) {
            throw new PositionException(DUPLICATED_POSITION_NAME);
        }

        if (!TypeValidator.isValidSupportLanguagesTypeList(request.getSupportLanguages())) {
            throw new PositionException(INVALID_LANGUAGE_TYPE);
        }

        Position position = Position.builder()
                .company(targetCompany)
                .positionName(request.getPositionName())
                .isOfficial(request.getIsOfficial())
                .supportLanguages(request.getSupportLanguages())
                .testTime(request.getTestTime())
                .problemInfo(request.getProblemInfo())
                .permitIde(request.getPermitIde())
                .permitSearch(request.getPermitSearch())
                .hiddenCase(request.getHiddenCase())
                .examMode(request.getExamMode())
                .testPlace(request.getTestPlace())
                .note(request.getNote())
                .build();

        Position savedPosition = positionRepository.save(position);
        companyService.updateCompanyStatus(companyId);

        return new PositionCreatedResponse(savedPosition.getId());
    }


    @Transactional(readOnly = true)
    public PositionDto getPosition(UUID positionId) {
        Optional<Position> position = positionRepository.findById(positionId);
        return position.map(PositionDto::fromEntity).orElseThrow(() -> new PositionException(POSITION_NOT_FOUND));
    }


    @Transactional
    public PositionDto updatePosition(UUID positionId, PositionReq request) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new PositionException(POSITION_NOT_FOUND));
        position.updatePosition(request);
        return PositionDto.fromEntity(position);
    }

    @Transactional
    public void deletePosition(UUID positionId) {
        Position position = positionRepository.findById(positionId).orElseThrow(()-> new PositionException(POSITION_NOT_FOUND));

        positionRepository.deleteById(positionId);
        companyService.updateCompanyStatus(position.getCompany().getId());
    }

    @Transactional
    public SupportLanguagesProjection getSupportLanguages(UUID positionId) {
        return positionRepository.findSupportLanguagesById(positionId)
                .orElseThrow(() -> new PositionException(POSITION_NOT_FOUND));
    }

}
