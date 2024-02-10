<script setup>
import ItemTitle from '@/components/meeting/items/ItemTitle.vue'
import draggable from "@/vuedraggable";
import KaKaoMap from '@/components/common/map/KaKaoMap.vue'
import { ref, computed, onMounted, onUnmounted, watch } from "vue";
import { useRoute } from 'vue-router';
import { usePlanStore } from "@/stores/planStore";
import { WebsocketProvider } from 'y-websocket';
import * as Y from 'yjs';
import _ from 'lodash'
import { debounce } from 'lodash';
import { cardListGetApi } from '@/api/cardApi';

const planStore = usePlanStore();

const route = useRoute();
const planId = route.params.id; // URL에서 planId를 추출합니다.
const cardList = ref([]); // 카드 목록을 담을 반응형 변수를 선언합니다.


const roomId = route.params.id; // URL에서 roomId 추출
const wsUrl = `ws://localhost:1234`; // WebSocket 서버 URL

const doc = new Y.Doc();// Yjs 배열 초기화

const yArray = doc.getArray('travelPlan');

// WebSocket 프로바이더 초기화
const wsProvider = new WebsocketProvider(wsUrl, roomId, doc);
const visible = ref(false);


const planList = ref([
    {
        cardId: 100,
        placeName: "마루샤브",
        placeAddress: "장덕동 1634",
        Lat: 35.1905106,
        Lng: 126.8169632,
        image: "https://lh5.googleusercontent.com/p/AF1QipM2Bk8GPiHYC3Zms9ngsgr3X2MS3wvwJ-t1b-Zl=w260-h175-n-k-no",
        orderNumber: 2,
        day: 1,
        memo: "",
    },
    {
        cardId: 104,
        placeName: "24시 콩나물국밥",
        placeAddress: "장덕동 1302",
        Lat: 35.1911555,
        Lng: 126.8194957,
        image: "https://lh5.googleusercontent.com/p/AF1QipMb3Ne3u_yKvQvLzVgIetlDhZfJYU7i9giMvc2W=w426-h240-k-no",
        orderNumber: 3,
        day: 1,
        memo: "",
    },
    {
        cardId: 102,
        placeName: "장인족발",
        placeAddress: "장덕동 1574",
        Lat: 35.190427,
        Lng: 126.8125625,
        image: "https://lh5.googleusercontent.com/p/AF1QipNPSfW6JXjsjckMdkZAyejA0YmpQ3TZOTyFziK_=w408-h306-k-no",
        orderNumber: 1,
        day: 1,
        memo: "",
    },
    {
        cardId: 101,
        placeName: "안청근린공원",
        placeAddress: "안청동 736-1",
        Lat: 35.2129735,
        Lng: 126.8037158,
        image: "https://lh5.googleusercontent.com/p/AF1QipOjnZPOnGIUGtOkualkcMrX-gudl4hLP5vzfOug=w493-h240-k-no",
        orderNumber: 1,
        day: 2,
        memo: "",
    },
]);

const days = ref([]);

const day = computed(() => planStore.dateDiff)

const checkD = ref(1)

const countP = computed(() => days.value.reduce((acc, r) => acc + r.length, 0))
const filteredPlan = ref([])
const filteredCard = ref([])
const noneFixCards = ref(cardList.value)
const FixCards = ref(days.value);
const controlOnStart = ref(true);
const loadCards = () => {
    const storedCards1 = JSON.parse(localStorage.getItem("noneFixCards"));
    const storedCards2 = JSON.parse(localStorage.getItem("FixCards"));
    if (storedCards1) {
        noneFixCards.value = storedCards1;
        days.value = storedCards2;
        FixCards.value = _.flattenDeep(storedCards2)
    } else {
        FixCards.value = planList.value

    }
}

function clone({ name }) {
    return { name }
}

function pullFunction() {
    return controlOnStart.value ? "clone" : false;
}

function saveCards() {
    // 현재 카드 데이터를 로컬 스토리지에 저장
    // localStorage.setItem("noneFixCards", JSON.stringify(noneFixCards.value));
    // localStorage.setItem("FixCards", JSON.stringify(days.value));

    // 나중에 axios연결
}

function handleChange() {
    // localStorage.setItem('noneFixCards', JSON.stringify(noneFixCards.value));
    // localStorage.setItem("FixCards", JSON.stringify(days.value));
    // for (let index = 0; index < days.value.length; index++) {
    //     localStorage.setItem(`day${index + 1}`, JSON.stringify(days.value[index]));
    // }
}
// function onCardMove() {

//     handleChange()
//     filteredPlan.value = JSON.parse(localStorage.getItem(`day${checkD.value}`));
//     filteredCard.value = JSON.parse(localStorage.getItem('noneFixCards'));

//     saveCards()
//     loadCards()
// }

// 카드 이동 로직 업데이트
// const onCardMove = (element, fromDay) => {
//   // `element`는 이동된 카드 객체, `fromDay`는 이 카드가 이동한 날짜(예: 1, 2 등)
//   // 카드를 cardList에서 제거
//   const indexToRemove = cardList.value.findIndex(card => card.cardId === element.cardId);
//   if (indexToRemove !== -1) {
//     cardList.value.splice(indexToRemove, 1);
//   }
//   // 카드를 적절한 'day' 배열에 추가
//   if (!days.value[fromDay]) {
//     days.value[fromDay] = [];
//   }
//   days.value[fromDay].push(element);
//   // Vue 반응성을 유지하기 위해 새로운 배열 할당
//   days.value = [...days.value];
//   cardList.value = [...cardList.value];
// };

// const changeDate = (day) => {
//     checkD.value = day
//     filteredPlan.value = JSON.parse(localStorage.getItem(`day${day}`));
// }

// const newCenter = ref({ lat: 33.450701, lng: 126.570667 })
// const setCenter = (element) => {
//     console.log('클릭됨', element)
//     newCenter.value.lat = element.Lat
//     newCenter.value.lng = element.Lng
//     console.log(newCenter.value)
// };


// 카드 데이터를 가져오는 메서드
async function fetchCardList() {
    try {
        const response = await cardListGetApi(planId);
        if (response.data.dataHeader.successCode === 0) {
            cardList.value = response.data.dataBody;
            console.log(cardList.value);
        } else {
            alert(response.data.dataHeader.resultMessage);
        }
    } catch (error) {
        if (error.response) {
            console.error(error);
            const errorResponse = error.response.data;
            alert(errorResponse.dataHeader.resultMessage);
        } else if (error.message === 'Network Error' || error.code === 'ERR_NETWORK') {
            // 네트워크 에러 처리
            alert("서버에 연결할 수 없습니다. 네트워크 연결을 확인해주세요.");
        }
    }
}



onMounted(() => {
    if (days.value.length !== day.value) {
        days.value = []
        for (let i = 1; i <= day.value; i++) {
            const dayPlan = planList.value.filter((item) => item.day === i).sort((a, b) => a.orderNumber - b.orderNumber)
            days.value.push(dayPlan)
        }
    }
    loadCards()
    console.log('days', days.value);

    fetchCardList(); // 컴포넌트가 마운트되면 카드 데이터를 가져옵니다.
})


</script>

<template>
    <div class="drag-container">
        <div class="row" style="display: flex;">
            <div class="drag-list">
                <ItemTitle class="title" />
                <h6 style="margin-left: 11%;">카드 목록</h6>
                <div>
                    <draggable class="DragArea list-group" :list="cardList" :group="{ name: 'card', pull: 'clone', put: false }"
                        item-key="id" @change="onCardMove">
                        <template #item="{ element }">
                            <div
                                :class="[FixCards.reduce((acc, item) => acc || (item.cardId === element.cardId), false) ? 'hidden' : 'active']">
                                <div class="list-group-item font-content">
                                    <div class="d-flex align-items-center gap-3 justify-content-center">
                                        <div class="card-card-list d-flex justify-content-start gap-3 align-items-center">
                                            <img class="card-image" :src="element.placeImage" alt="">
                                            <div style="display: flex; flex-direction: column;">
                                                <div style="font-weight: bold;">{{ element.placeName }}</div>
                                                <div>{{ element.placeAddress }}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </draggable>
                </div>

            </div>
            <div class="drag-list">
                <div style="height: 80px; padding: 1rem ; display:flex; align-items: end;">
                    <div class="d-flex align-items-center">
                        <div style="font-size: 28px;">
                            {{ countP }}</div>
                        <div class="font-content" style="font-size: 15px;">개의 장소</div>
                    </div>
                </div>
                <div>
                    <div class="plan-list-margin">
                        <div class="drag-plan-list" v-for="(fixCard, index) in days" :key="index">
                            <h6 style="cursor: pointer; margin-left: 6%;" @click="changeDate(index + 1)">Day {{ index + 1 }}
                            </h6>
                            <div>
                                <draggable class="DragArea list-group" :list="fixCard" :group="{ name: 'card', put: true }"
                                    item-key="id" @change="onCardMove">
                                    <template #item="{ element, index }">
                                        <div @click="setCenter(element)" class="list-group-item font-content">
                                            <div class="d-flex align-items-center gap-3 justify-content-center"
                                                style="position: relative; margin-bottom: 10px;">
                                                <div class="orderNumber">{{ index + 1 }}</div>
                                                <div
                                                    class="card-detail d-flex justify-content-start gap-3 align-items-center">
                                                    <img class="card-image" :src="element.image" alt="">
                                                    <div>{{ element.placeName }}</div>
                                                </div>
                                            </div>
                                        </div>
                                    </template>
                                </draggable>
                            </div>
                        </div>
                    </div>
                    <div class="btns">
                        <div class="btns-box">
                            <button class="btn quit-btn" @click="goMain">
                                <i class="pi pi-times"></i> 끝내기
                            </button>
                            <button class="btn save-btn" @click="saveCards">
                                <i class="pi pi-check"></i> 저장
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="map">
                <KaKaoMap :card-list="filteredCard" :detail-list="filteredPlan" />
            </div>
        </div>
        <!-- </div> -->
    </div>
</template>
    
<style scoped>
.map {
    position: relative;
}

.title {
    height: 130px;
}

.card-detail {
    width: 280px;
    border-radius: 5px;
    padding: 0.4rem;
    position: relative;
    box-shadow: 0px 3px 5px gainsboro;
}

.card-card-list {
    width: 300px;
    height: 70px;
    border-radius: 5px;
    padding: 0.4rem;
    position: relative;
    box-shadow: 0px 3px 5px gainsboro;
}

.card-image {
    width: 3rem;
    height: 3rem;
    border-radius: 5px;
}

.sidebar-active {
    display: block;
    width: 50vw;
    transition: width 1s;
    transition-timing-function: cubic-bezier(0, 0, 0.2, 1);
}

.sidebar-active {
    display: block;
    width: 0vw;
    transition: width 1s;
    transition-timing-function: cubic-bezier(0, 0, 0.2, 1);
}

.tab {
    position: fixed;
    right: 0px;
    top: 5rem;
}

.drag-container {
    width: 100vw;
    height: 100vh;
}

.drag-list {
    height: 100vh;
    min-width: 380px;
    border-left: rgba(0, 0, 0, 0.1) 1px solid;
    background-color: #fff;
}

.plan-list-margin {
    position: absolute;
    top: 80px;
    overflow: auto;
    width: 380px;
    height: calc(100vh - 120px);
    margin-top: 1rem;
    padding-top: 1rem;
}

.btns {
    background-color: #fff;
    position: absolute;
    bottom: 0px;
    width: 380px;
    height: 50px;
}

.btns-box {
    position: relative;
}

.btn {
    position: absolute;
    bottom: 2rem;
    width: 70px;
    height: 30px;
    text-align: start;
    padding-left: 8px;
    padding-top: 3px;
    z-index: 10;
}

.quit-btn {
    top: 10px;
    right: 10px;
    border: red 1px solid;
    border-radius: 5px;
    background-color: #fff;
    color: red;

}

.save-btn {
    top: 10px;
    right: 90px;
    border: #3498DB 1px solid;
    border-radius: 5px;
    background-color: #3498DB;
    color: #fff;
}

.drag-plan-list {
    margin-bottom: 15%;
    z-index: 2;
}

.orderNumber {
    background-color: #3498DB;
    color: #fff;
    width: 20px;
    height: 20px;
    font-size: 15px;
    text-align: center;
    font-weight: bold;
    border-radius: 50%;
}
</style>
    
<style>
.overlay {
    background-color: white;
    border-radius: 5px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.8);
}

.marker-number {
    position: relative;
    color: #fff;
    left: -1px;
    top: 2px;
    width: 28px;
    height: 28px;
    background-color: #3498DB;
    /* border: black solid 1px; */
    border-radius: 50%;
    font-size: 20px;
    font: bolder;
    display: inline-block;
    text-align: center;
}</style>