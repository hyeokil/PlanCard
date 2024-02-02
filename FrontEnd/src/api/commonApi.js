import { localAxios } from "@/util/http-commons";

const local = localAxios();

function fileUploadApi(param, success, fail) {
  // Axios 인스턴스의 기본 Content-Type 헤더를 무시
  return local
    .post(`/firebase/upload`, param, {
      headers: {
        // Content-Type 헤더를 설정하지 않거나 명시적으로 undefined로 설정
        "Content-Type": undefined,
      },
    })
    .then(success)
    .catch(fail);
}

export { fileUploadApi };