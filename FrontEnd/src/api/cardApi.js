import { localAxios } from "@/util/http-commons";

const local = localAxios();
// 카드 전체 조회 -> = 생성
async function cardGetApi(planId, success, fail) {
  await local.get(`/plan/${planId}/card`).then(success).catch(fail);
}

// 카드 카테고리별 조회

// // 카드 단일 조회
// async function cardMemoDetailApi(cardId, success, fail) {
//   await local.get(`/card/${cardId}`).then(success).catch(fail);
// }

// 카드 메모 작성
async function cardMemoUpdateApi(param, success, fail) {
  console.log("param", param);
  await local.patch(`/plan/card/${param.cardId}`, param).then(success).catch(fail);
}

// 카드 삭제
async function cardDeleteApi(cardId, success, fail) {
  await local.delete(`/plan/card/${cardId}`).then(success).catch(fail);
}

// // 관광지 조회
// async function attractionDetailApi(attractionId, success, fail) {
//   await local.get(`/card/${attractionId}`).then(success).catch(fail);
// }

export { cardGetApi, cardMemoUpdateApi, cardDeleteApi };
