import { localAxios } from "@/util/http-commons";

const local = localAxios();

// 여행 계획 생성
async function planCreateApi(param, success, fail) {
  console.log("param", param);
  await local.post(`/plan`, param).then(success).catch(fail);
}

// 여행 계획 조회

// 여행 계획 수정 (계획명)
async function planNameUpdateApi(planId, param, success, fail) {
  console.log("param", param);
  await local.post(`/plan/${planId}`, param).then(success).catch(fail);
}

// 여행 계획 수정 (일정)
async function planDateUpdateApi(planId, param, success, fail) {
  console.log("param", param);
  await local.post(`/plan/${planId}`, param).then(success).catch(fail);
}

// 여행 세부 계획 생성
async function planDetailCreateApi(planId, success, fail) {
  console.log("planId", planId);
  await local.post(`/plan/${planId}/detail`, planId).then(success).catch(fail);
}

// 여행 세부 계획 조회
async function planDetailGetApi(planId, success, fail) {
  console.log("planId", planId);
  await local.get(`/plan/${planId}/detail`, planId).then(success).catch(fail);
}

// 여행 세부 계획 수정
async function planDetailUpdateApi(param, success, fail) {
  console.log("param", param);
  await local.put(`/plan/${param.planId}/detail`, param).then(success).catch(fail);
}

export {
  planCreateApi,
  planNameUpdateApi,
  planDateUpdateApi,
  planDetailCreateApi,
  planDetailGetApi,
  planDetailUpdateApi,
};
