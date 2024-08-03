package kr.sols.member.controller;
import jakarta.validation.Valid;
import kr.sols.auth.annotation.RoleUser;
import kr.sols.member.dto.MemberDto;
import kr.sols.member.dto.MemberEditRequest;
import kr.sols.member.dto.MemberListDto;
import kr.sols.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("list")
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
