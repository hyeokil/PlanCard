<template>
    <div>
        <div class="mt-0 pt-0">
            <ItemDrag2></ItemDrag2>
            <div class="chat-tab">
                <div class="chat-container">
                    <button @click="activeTab = !activeTab" class="chat-btn" id="chat-btn">
                        <i class="pi pi-comments" style="font-size: 23px;"></i> 채 팅
                    </button>
                    <div :class="{ 'sidebar-active': activeTab, 'sidebar-hidden': !activeTab }" style="height: 100vh; display: flex; flex-direction: column;">
                        <ItemFace class="itemFace"></ItemFace>
                        <Chat class="chat" v-if="activeTab"></Chat>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>



<script setup>
import ItemFace from '@/components/meeting/items/ItemFace.vue'
import Chat from '@/components/meeting/items/Chat.vue'
import ItemDrag2 from "@/components/meeting/items/ItemDrag2.vue"
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

</script>

<style scoped>
.meeting {
    width: 100vw;
    height: 100vh;
}

.card-list {
    height: 60vh;
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

.plan {
    height: 30vh;
}

.chat-tab {
    position: absolute;
    top: 0px;
    right: 0px;
    height: 100vh;
    background-color: #ddd;
    z-index: 10;
}

.chat-container {
    position: relative;
    height: 100vh;
}

.chat-btn {
    position: absolute;
    top: 2rem;
    left: -30px;
    z-index: 5;
    background-color: #3498DB;
    width: 30px;
    height: 100px;
    border-radius: 5px 0px 0px 5px;
    font-size: 15px;
    color: #fff;
}

.itemFace {
  height: 30%;
}
.chat {
    position: absolute;
    background-color: #dfecf7 ;
    height: 70%;
    width: 100%;
    bottom: 0px;
    left: 0px;
}
</style>