import { localAxios } from "@/util/http-commons";

const local = localAxios();

async function cardCreateApi(param, success, fail) {
    console.log("param", param);
    await local.post(`/card`, param).then(success).catch(fail);
}



export {
    cardCreateApi,
};