import { localAxios } from "@/util/http-commons";

const local = localAxios();

async function alarmGetListApi(lastAlarmId = null, limit = 4) {
    let endPoint = `/alarm/list?limit=${limit}`;
    
    if (lastAlarmId) {
        endPoint += `&lastAlarmId=${lastAlarmId}`;
    }
    
    return await local.get(endPoint);
}

async function alarmFriendRequestApi(param) {
    return await local.post(`/alarm/friend/request`, param);
}

async function alarmActionApi({alarmId, action}) {
    return await local.post(`/alarm/${alarmId}/${action}`);
}

export {
    alarmGetListApi,
    alarmFriendRequestApi,
    alarmActionApi,
};