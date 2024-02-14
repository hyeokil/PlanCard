import { localAxios } from "@/util/http-commons";

const local = localAxios();
// 카드 전체 조회 -> = 생성
async function cardListGetApi(planId) {
  return await local.get(`/plan/${planId}/card`);
}

// 카드 메모 작성
async function cardMemoUpdateApi(param, success, fail) {
  console.log("param", param);
  await local.patch(`/plan/card/${param.cardId}`, param).then(success).catch(fail);
}

// 카드 사용한 것 삭제
async function cardDeleteApi(cardId) {
  return await local.delete(`/plan/card/${cardId}`);
}

// 여행지 검색
async function placeSearchApi(param) {
  return await local.get(`/place/search?name=${param}`);
}

// 카드 생성
async function cardCreateApi(planId, placeId) {
  const param = { memo: "" };
  return await local.post(`plan/${planId}/card/${placeId}`, param);
}

export { cardListGetApi, cardMemoUpdateApi, cardDeleteApi, placeSearchApi, cardCreateApi };
