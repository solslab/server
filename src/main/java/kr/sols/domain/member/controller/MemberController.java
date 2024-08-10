package kr.sols.domain.member.controller;
import jakarta.validation.Valid;
import kr.sols.auth.annotation.RoleUser;
import kr.sols.domain.member.dto.MemberDto;
import kr.sols.domain.member.dto.MemberEditRequest;
import kr.sols.domain.member.dto.MemberListDto;
import kr.sols.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    // 회원 조회 (마이페이지)
    @RoleUser
    @GetMapping
    public ResponseEntity<MemberDto> getMyPageInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(memberService.memberInfo(userDetails.getUsername()));
    }

    // 추가정보 입력여부 확인
    @RoleUser
    @GetMapping("/info-check")
    public ResponseEntity<Map<String, String>> additionalInfoCheck(
            @AuthenticationPrincipal UserDetails userDetails) {

        String memberKey = userDetails.getUsername();
        Map<String, String> response = memberService.additionalInfoCheck(memberKey);

        return ResponseEntity.ok(response);
    }



    // 회원가입 추가정보 입력 및 회원수정
    @RoleUser
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MemberDto> memberEdit(
            @RequestBody @Valid MemberEditRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(memberService.memberEdit(request, userDetails.getUsername()));
    }

    // 모든 회원 조회 (관리자)
    @GetMapping("/list")
    public List<MemberListDto> getMemberList() {
        return memberService.getAllMember();
    }

    // 회원 상세 조회 (관리자)
    @GetMapping("/{memberKey}")
    public ResponseEntity<MemberDto> getMember(
            @PathVariable String memberKey
    ) {
        return ResponseEntity.ok(memberService.memberInfo(memberKey));
    }
}
