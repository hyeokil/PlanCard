import { localAxios } from "@/util/http-commons";

const local = localAxios();

async function createSessionApi() {
    return await local.post(`/sessions`);
}

async function connectionSessionApi(sessionId) {
    return await local.post(`/sessions/${sessionId}/connections`);
}

export {
    createSessionApi,
    connectionSessionApi,
};
