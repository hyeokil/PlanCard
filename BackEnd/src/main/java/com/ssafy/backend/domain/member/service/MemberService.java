package com.ssafy.backend.domain.member.service;

import com.ssafy.backend.domain.member.dto.*;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;

public interface MemberService {
    // 회원가입 기능
    void signUpMember(MemberSignUpRequestDto signUpRequestDto);

    // 로그인 기능
    TokenMemberInfoDto loginCheckMember(MemberLoginRequestDto loginRequestDto);

    // 로그아웃 기능
    void logoutMember(String email);

    // 비밀번호 변경 기능
    void updatePasswordMember(Long id, MemberPasswordUpdateDto passwordUpdateDto);

    // 프로필 이미지 및 닉네임 수정 기능
    void updateImageAndNicknameMember(Long id, MemberUpdateDto updateDto);

    // 나의 정보 가져오기 기능
    MemberGetResponseDto getMember(Long id);
}
