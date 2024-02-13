import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 일반 로그인
async function memberLoginApi(param, success, fail) {
    await local.post(`/member/login`, param).then(success).catch(fail);
}

// 로그아웃
async function memberLogoutApi(success, fail) {
    await local.post(`/member/logout`).then(success).catch(fail);
}

// 소셜 통해 회원 정보 가져오기
async function startSocialLoginApi(oAuthDomain) {
    return await local.get(`/oauth/${oAuthDomain}`);
}

// 소셜 로그인 (소셜 통해 가져온 회원 정보를 토대로 회원가입 및 로그인 진행)
async function finishSocialLoginApi(oAuthDomain, code) {
    return await local.get(`/oauth/${oAuthDomain}/login`, { params: { code } });
}

// 일반 회원가입
async function memberSignUpApi(param, success, fail) {
    await local.post(`/member/signup`, param).then(success).catch(fail);
}


async function passwordResettingApi(param, success, fail) {
    await local.patch(`/member/update/password`, param).then(success).catch(fail);
}

// 회원 정보 수정하기 (닉네임, 프로필 이미지)
async function memberUpdateApi(param, success, fail) {
    await local.patch(`/member/update/image/nickname`, param).then(success).catch(fail);
}

// 회원 정보 불러오기
async function memberGetApi() {
    return await local.get(`/member/get`);
}

// 이메일 인증코드 전송 
async function sendEmailCodeApi(memberEmail) {
    return await local.post(`/member/email/send/${memberEmail}`);
}

// 이메일 인증코드 검증
async function verifyEmailCodeApi(memberEmail, emailCode) {
    return await local.post(`/member/email/verify/${memberEmail}/${emailCode}`);
}


export {
    memberLoginApi,
    startSocialLoginApi,
    finishSocialLoginApi,
    memberSignUpApi,
    memberLogoutApi,
    passwordResettingApi,
    memberUpdateApi,
    memberGetApi,
    sendEmailCodeApi,
    verifyEmailCodeApi,
};