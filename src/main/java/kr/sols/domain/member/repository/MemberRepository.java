package kr.sols.domain.member.repository;

import kr.sols.domain.member.dto.MemberListDto;
import kr.sols.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberKey(String memberKey);

    Page<Member> findAllByOrderByCreatedDateDesc(Pageable pageable);

    void deleteByMemberKey(String memberKey);
}