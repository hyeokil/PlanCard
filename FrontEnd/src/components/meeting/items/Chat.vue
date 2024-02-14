<template>
    <div class="chat-container font-content" v-if="isConnected">
        <div class="chat-scroll">
            <div class="messages">
                <div v-for="chat in chats" :key="chat.id" class="message"
                    :class="{ 'mine': chat.memberId === accountStore.memberInfo.id, 'theirs': chat.memberId !== accountStore.memberInfo.id }">
                    <img :src="chat.image || '/로고 3.png'" alt="profile" class="thumbnail">
                    <div class="message-contents">
                        <div class="message-nickname">{{ chat.nickname }}</div>
                        <div>
                            <div class="message-content">
                                <div style="word-break: break-all; white-space: normal; max-width: 20vw;">
                                    {{ chat.message }}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="input-box">
            <div class="send-box">
                <textarea class="input-text" v-model="chatText" @keyup.enter="sendMessage" placeholder="메시지를 입력하세요"></textarea>
                <!-- <button class="send-btn" @click="sendMessage"><i class="pi pi-send" style="font-size: 25px;"></i></button> -->
            </div>
        </div>
    </div>
</template>


<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useCookies } from 'vue3-cookies';
import { useRoute } from 'vue-router';
import { localAxios } from '@/util/http-commons';
import { useAccountsStore } from '@/stores/accountsStore'; // accountsStore 가져오기
import { connectStompClient, getStompClient } from '@/util/stompConnection';


const { cookies } = useCookies();
const route = useRoute();
const roomId = route.params.id; // vue-router를 통해 roomId를 동적으로 받아옴
const accountStore = useAccountsStore(); // accountsStore 사용
const chats = ref([]);
const chatText = ref('');
const isConnected = ref(false);

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

    connectStompClient(serverURL, accessToken, async () => {
        isConnected.value = true;
        // 여기서 추가적인 연결 후 로직 (예: 구독 설정)을 구현할 수 있습니다.
        await fetchChatHistory();
        getStompClient().subscribe(`/topic/room.${roomId}`, message => {
            chats.value.push(JSON.parse(message.body));
        });
    }, error => {
        console.error("Connection error: ", error);
    });
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


        getStompClient().send(`/pub/api/v1/chat/${roomId}`, JSON.stringify(message), {
            Authorization: `Bearer ${cookies.get("accessToken")}`
        });

        chatText.value = '';
    }
};

onMounted(() => {
    connect();
});

onUnmounted(() => {
    if (getStompClient() && getStompClient().connected) {
        getStompClient().disconnect();
    }
});
</script>
  
<style scoped>

.chat-container{
    display: flex;
    flex-direction: column;
    justify-content: end;
}
.chat-scroll{
    padding-left: 10px;
    padding-right: 11px;
    height: 100%;
    overflow: auto;
    display: flex;
    flex-direction: column-reverse;
}
.chat-scroll::-webkit-scrollbar {
    width: 10px;
  }
  .chat-scroll::-webkit-scrollbar-thumb {
    background-color: #3498DB;
    border-radius: 10px;
  }
  .chat-scroll::-webkit-scrollbar-track {
    background-color: #f0f0f0;
    border-radius: 10px;
  }
/* 기본 메시지 스타일 */
.message {
    display: flex;
    margin-bottom: 10px;
    align-items: end;
}

/* 내 메시지 스타일 */
.mine {
    justify-content: flex-end;
    
    
}
.mine .message-content {
    background-color: #3498DB;
    color: #fff;
    z-index: 2;
    }
.mine .message-content::after {
    content: '';
    position: absolute;
    width: 0px;
    right: -6px;
    top: 0px;
    border-bottom: 6px solid transparent;
    border-top: 6px solid #3498DB;
    border-right: 6px solid transparent;
    border-left: 6px solid #3498DB;
    position: absolute;
    z-index: 1;
}
.mine .message-contents{
    display: flex;
    flex-direction: column;
    align-items: end;
    }
.mine >img{
    display: none;
}
/* 상대방 메시지 스타일 */
.theirs {
    justify-content: flex-start;
}

.thumbnail {
    width: 40px;
    height: 40px;
    border-radius: 20px;
    margin-right: 10px;
}
.message-nickname{
    font-size: 11px;
}

/* 메시지 텍스트 스타일링 */
.message-content {
    display: flex;
    align-items: center;
    /* display: inline-block; */
    max-width: calc(30vw-70px);
    padding: 0px 10px 0px 10px;
    border-radius: 8px;
    min-height: 30px;
    position: relative;
}

/* 내 메시지 배경색 */

/* 상대방 메시지 배경색 */
.theirs .message-content{    
    background-color: white;
    z-index: 2;

}
.theirs .message-contents{
    display: flex;
    flex-direction: column;
    align-items: start;
    }
    
.theirs .message-content::before{
    content: '';
    position: absolute;
    width: 0px;
    left: -10px;
    bottom: 0px;
    border-bottom: 6px solid #fff;
    border-top: 6px solid transparent;
    border-right: 8px solid #fff;
    border-left: 8px solid transparent;
    position: absolute;
    z-index: 1;
}
.input-box{
    margin: 0px;
    height: 100px;
    padding-top: 10px;
    padding-left: 10px;
    padding-right: 10px;
    position: relative;
}
.send-box{
    height: 50px;
    max-height: 60px;
    position: relative;
}
.input-text{
    background-color: #fff;
    width: 100%;
    height: 100%;
    resize: none;
    border-radius: 5px;
    padding: 10px;
}
.input-text:focus{
    outline: none;

}
.input-text::-webkit-scrollbar {
    width: 10px;
  }
  .input-text::-webkit-scrollbar-thumb {
    background-color: silver;
    border-radius: 10px;
  }
  .input-text::-webkit-scrollbar-track {
    background-color: #f0f0f0;
    border-radius: 10px;
  }
.send-btn{
    position: absolute;
    top: 0px;
    right: 0px;
    width: 50px;
    height: 100%;
    border-radius: 0px 5px 5px 0px;
    color: #f0f0f0;
    background-color: grey;
}
</style>
  