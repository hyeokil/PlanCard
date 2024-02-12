import { localAxios } from "@/util/http-commons";

const local = localAxios();

async function createSessionApi(sessionId) {
    return await local.post(`/sessions`, { customSessionId: sessionId, userNo: 53, endHour: 1, endMinute: 30, quota: 16, isPrivacy: false });
}

async function connectionSessionApi(sessionId) {
    return await local.post(`/sessions/${sessionId}/connections`);
}

export {
    createSessionApi,
    connectionSessionApi,
};
