package kr.sols.member.controller;
import jakarta.validation.Valid;
import kr.sols.auth.annotation.RoleUser;
import kr.sols.member.dto.MemberDto;
import kr.sols.member.dto.MemberEditRequest;
import kr.sols.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @RoleUser
    @GetMapping
    public ResponseEntity<MemberDto> memberInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(memberService.memberInfo(userDetails.getUsername()));
    }

    @RoleUser
    @PatchMapping
    public ResponseEntity<MemberDto> memberEdit(
            @RequestBody @Valid MemberEditRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(memberService.memberEdit(request, userDetails.getUsername()));
    }
}
