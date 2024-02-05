<template>
    <div class="chat-container" v-if="isConnected">
        <h3>채팅</h3>
        <div class="messages">
            <div v-for="chat in chats" :key="chat.id" class="message"
                :class="{ 'mine': chat.memberId === accountStore.memberInfo.id, 'theirs': chat.memberId !== accountStore.memberInfo.id }">
                <img :src="chat.image || '/로고 3.png'" alt="profile" class="thumbnail">
                <span>{{ chat.nickname }}: {{ chat.message }}</span>
            </div>
        </div>
        <input v-model="chatText" @keyup.enter="sendMessage" placeholder="메시지를 입력하세요" />
    </div>
</template>


<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue';
import { useCookies } from 'vue3-cookies';
import { useRoute } from 'vue-router';
import Stomp from 'webstomp-client';
import { localAxios } from '@/util/http-commons';
import { useAccountsStore } from '@/stores/accountsStore'; // accountsStore 가져오기


const { cookies } = useCookies();
const route = useRoute();
const roomId = route.params.id; // vue-router를 통해 roomId를 동적으로 받아옴
const accountStore = useAccountsStore(); // accountsStore 사용
const chats = ref([]);
const chatText = ref('');
const isConnected = ref(false);
let stompClient = reactive(null);

// 채팅방 내역 불러오기
const fetchChatHistory = async () => {
    try {
        const response = await localAxios().get(`/history/${roomId}`);
        console.log(response.data.dataBody);
        chats.value = response.data.dataBody; // 서버에서 채팅 내역을 받아옴
    } catch (error) {
        console.error("Chat history fetch error: ", error);
    }
};

const connect = () => {
    const serverURL = `${import.meta.env.VITE_VUE_WS_URL}`;
    const accessToken = cookies.get("accessToken");
    const socket = new WebSocket(serverURL);
    stompClient = Stomp.over(socket);

    stompClient.connect(
        { Authorization: `Bearer ${accessToken}` },
        async () => {
            isConnected.value = true;
            await fetchChatHistory();
            stompClient.subscribe(`/topic/room.${roomId}`, message => {
                chats.value.push(JSON.parse(message.body));
            });
        },
        error => {
            console.error("Connection error: ", error);
        }
    );
};

const sendMessage = () => {
    if (chatText.value.trim() !== '') {
        const message = {
            memberId: accountStore.memberInfo.id, // 사용자 ID 설정
            message: chatText.value,
            nickname: accountStore.memberInfo.nickname, // accountsStore에서 닉네임 가져오기
            image: accountStore.memberInfo.image, // accountsStore에서 프로필 이미지 URL 가져오기
            // memberId 설정은 서버에서 처리하거나 필요에 따라 추가
        };

        // 메시지 전송 전에 로컬 채팅 목록에 추가
        //   chats.value.push(message);

        stompClient.send(`/pub/api/v1/chat/${roomId}`, JSON.stringify(message), {
            Authorization: `Bearer ${cookies.get("accessToken")}`
        });
        chatText.value = '';
    }
};

onMounted(() => {
    connect();
});

onUnmounted(() => {
    if (stompClient && stompClient.connected) {
        stompClient.disconnect();
    }
});
</script>
  
<style scoped>
/* 기본 메시지 스타일 */
.message {
    display: flex;
    margin-bottom: 10px;
}

/* 내 메시지 스타일 */
.mine {
    justify-content: flex-end;
}

/* 상대방 메시지 스타일 */
.theirs {
    justify-content: flex-start;
}

.thumbnail {
    width: 40px;
    height: 40px;
    border-radius: 20px;
}

/* 메시지 텍스트 스타일링 */
.message span {
    padding: 5px 10px;
    border-radius: 20px;
    background-color: #f0f0f0;
    margin: 0 10px;
}

/* 내 메시지 배경색 */
.mine span {
    background-color: #blue;
    /* 자신의 메시지 배경색 */
}

/* 상대방 메시지 배경색 */
.theirs span {
    background-color: #grey;
    /* 상대방 메시지 배경색 */
}
</style>
  