<template>
    <div>
        <div class="mt-0 pt-0">
            <ItemDrag2></ItemDrag2>
            <!-- 채팅창 -->
            <div class="chat-tab">
                <div class="chat-container">
                    <button @click="whatIsActive('채팅')" class="chat-btn btn" id="chat-btn">
                        <i class="pi pi-comments" style="font-size: 23px;"></i> 채 팅
                    </button>
                    <button @click="whatIsActive('검색')" class="search-btn btn" id="search-btn">
                        <i class="pi pi-search" style="font-size: 23px;"></i> 검 색
                    </button>
                    <div :class="{ 'sidebar-active': activeTab !=='', 'sidebar-hidden': activeTab ==='' }" style="height: 100vh;">
                        <div
                        :class="{'active':activeTab==='채팅', 'hidden':activeTab!=='채팅'}"
                        id="chat" style="width: 100%; height: 100vh; display: flex; flex-direction: column;">
                            <ItemFace class="itemFace"></ItemFace>
                            <ItemChat class="chat"></ItemChat>
                        </div>
                        <div
                        :class="{'active':activeTab==='검색', 'hidden':activeTab!=='검색'}"
                        id="search" style="width: 100%; height: 100%; background-color: #dfecf7; border-radius: 10px 0px 0px 0px;">
                            <ItemSearch class="search"></ItemSearch>
                        </div>
                        <div
                        :class="{'active':activeTab!=='', 'hidden':activeTab!==''}"
                        id="else" style="height: 100%; background-color: #dfecf7; border-radius: 10px 0px 0px 0px;"></div>
                    </div>
                </div>
            </div>
            <!-- 채팅창 -->

        </div>
    </div>
</template>



<script setup>
import ItemFace from '@/components/meeting/items/ItemFace.vue'
import ItemChat from '@/components/meeting/items/ItemChat.vue'
import ItemDrag2 from "@/components/meeting/items/ItemDrag2.vue"
import ItemSearch from '@/components/meeting/items/ItemSearch.vue'
import { onBeforeMount, onBeforeUnmount, ref } from "vue"
import { usePlanStore } from "@/stores/planStore";
const planStore = usePlanStore()

onBeforeMount(() => {
    planStore.isMeetingView = true
})

onBeforeUnmount(() => {
    planStore.isMeetingView = false
})

// 탭 상태 추가
const activeTab = ref('');

// 어느 탭이 활성화되었는지 체크
const whatIsActive = (tab) => {
    if (activeTab.value !== '' && activeTab.value === tab) {
            activeTab.value = ''
    } else {
        activeTab.value = tab
    }
}



</script>

<style scoped>
.meeting {
    width: 100vw;
    height: 100vh;
}

.card-list {
    height: 60vh;
}

.active{
    display: block;
}
.hidden{
    display: none;
}
.plan {
    height: 30vh;
}

.chat-tab {
    position: absolute;
    top: 0px;
    right: 0px;
    height: 100vh;
    background-color: transparent;
    z-index: 10;
}

.chat-container {
    position: relative;
    height: 100vh;
}
.btn{
    position: absolute;
    left: -30px;
    z-index: 5;
    background-color: #3498DB;
    width: 30px;
    height: 100px;
    border-radius: 5px 0px 0px 5px;
    font-size: 15px;
    color: #fff;
}
.chat-btn {
    top: 2rem;
}
.search-btn {
    top: 9.5rem;
}
.sidebar-active {
    display: block;
    width: 30vw;
    transition: width 1s;
    transition-timing-function: cubic-bezier(0, 0, 0.2, 1);
    
}

.sidebar-hidden {
    display: block;
    width: 0vw;
    transition: width 1s;
    transition-timing-function: cubic-bezier(0, 0, 0.2, 1);
}

.itemFace {
    height: 30vh;
    width: 30vw;
}
.chat {
    position: absolute;
    background-color: #dfecf7 ;
    height: 70vh;
    bottom: 0px;
    width: 30vw;
    left: 0px;
}
.search{
    position: absolute;
    background-color: #dfecf7 ;
    height: 100%;
    bottom: 0px;
    left: 0px;
}
</style>