<template>
    <div>
        <div class="col-12 mt-0 pt-0">
            <ItemTitle class="title"/>
        </div>
        <div class="grid">
            <div class="col-4 pb-0">
                <ItemCardList class="card-list" 
                @card-update="cardUpdate"
                />
                <ItemFace class="face" />
            </div>
            <!-- <div class="col-8 pt-0">
                <ItemDetailPlanList class="plan mb-0" 
                @detail-plan-update="detailPlanUpdate" 
                />
                <ItemMap 
                :card-list="cardList"
                :detail-plan-list="detailPlanList"
                class="map pt-5"/>
            </div> -->
        </div>
        <!-- 기존 코드 유지 -->
        <div class="tab-container">
            <button @click="toggleTab('chat')">채팅</button>
        </div>
        <!-- 조건부 렌더링으로 채팅 컴포넌트 표시 -->
        <Chat v-if="activeTab === 'chat'"></Chat>
    </div>
</template>



<script setup>
import ItemCardList from '@/components/meeting/items/card/ItemCardList.vue'
import ItemDetailPlanList from '@/components/meeting/items/detailplan/ItemDetailPlanList.vue'
import ItemFace from '@/components/meeting/items/ItemFace.vue'
import ItemTitle from '@/components/meeting/items/ItemTitle.vue'
// import ItemMap from '@/components/meeting/items/ItemMap.vue'
import Chat from '@/components/meeting/items/Chat.vue'
import { ref } from "vue"
const cardList = ref([])
const detailPlanList = ref([])

const cardUpdate = (emitCardList) => {
    cardList.value = emitCardList
}

const detailPlanUpdate = (emitdetailPlanList) => {
    detailPlanList.value = emitdetailPlanList
}

// 탭 상태 추가
const activeTab = ref('');

// 탭 토글 기능
const toggleTab = (tabName) => {
    activeTab.value = activeTab.value === tabName ? '' : tabName;
};
</script>

<style scoped>
.meeting{
    width: 100vw;
    height: 100vh;
}
.title{
    width: 100vw;
    height: 5vh;
}
.card-list{
    height: 60vh;
}
.face{
    height: 15vh;
}
.plan{
    /* height: 30vh; */
    height:75vh;
}
/* .map{
    height: 45vh;
} */

.tab-container button {
    padding: 10px;
    border: none;
    background-color: #f0f0f0;
    cursor: pointer;
}
.tab-container button.active {
    background-color: #ddd;
}
/* 기존 스타일 유지 */
</style>