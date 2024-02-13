import { localAxios } from "@/util/http-commons";

const local = localAxios();

async function friendListGetApi(page, size) {
    return await local.get(`/friends?page=${page}&size=${size}`);
}


export {
    friendListGetApi
};