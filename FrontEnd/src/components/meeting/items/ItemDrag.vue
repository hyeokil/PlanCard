<template>
    <div>
        <div class="row" style="display: flex;">
            <div class="col-3 card" style="margin-right: 20px;">
                <h6>카드 목록</h6>
                <div style="height: 300px; overflow: auto;">
                    <draggable
                    class="DragArea list-group"
                    :list="cardList"
                    :group="{name: 'card', pull:'clone', put:false}"
                    item-key="id"
                    @change="onCardMove"
                    >
                        <template #item="{ element }">
                            <div :class="[FixCards.reduce((acc,item)=> acc||(item.cardId===element.cardId),false)? 'hidden':'active' ]">
                                <div  class="list-group-item card">
                                    {{ element.placeName }}
                                </div>
                            </div>
                        </template>
                    </draggable>
                </div>
            </div>
        
            <div class="col-3 card" style="margin-right: 20px;" v-for="(fixCard, index) in days" :key="index">
                <h6 style="cursor: pointer;" @click="changeDate(index+1)">Day {{ index + 1 }}</h6>
                <div style="height: 300px; overflow: auto;">
                    <draggable
                    class="DragArea list-group"
                    :list="fixCard"
                    :group="{name: 'card', put:true}"
                    item-key="id"
                    @change="onCardMove"
                    style="height: 300px;"
                    >
                    <template #item="{ element, index }">
                        <div @click="setCenter(element)" class="list-group-item card">
                                {{index+1}} {{ element.placeName }}
                            </div>
                    </template>
                    </draggable>
                </div>
            </div>
            <div class="col-3">
                <KaKaoMap
                :card-list="filteredCard"
                :detail-list="filteredPlan"
                />
            </div>
        </div>
    </div>
</template>
    


<script setup>
import draggable from "@/vuedraggable";
import KaKaoMap from '@/components/common/map/KaKaoMap.vue'
import { isProxy, toRaw, ref, computed, onMounted } from "vue";
import { usePlanStore } from "@/stores/planStore";
import _ from 'lodash'
const planStore = usePlanStore()
// let idGlobal = computed(()=>cardList.length);

const cardList = ref([
    {
        cardId: 2,
        placeName: "장인족발",
        placeAddress: "장덕동 1574",
        Lat: 35.190427,
        Lng:126.8125625,
        memo: "",
    },
    {
        cardId: 3,
        placeName: "마루샤브",
        placeAddress: "장덕동 1634",
        Lat:35.1905106,
        Lng:126.8169632,
        memo: "",
    },
    {
        cardId: 4,
        placeName: "24시 콩나물국밥",
        placeAddress: "장덕동 1302",
        Lat:35.1911555,
        Lng:126.8194957,
        memo: "",
    },
    {
        cardId: 1,
        placeName: "안청근린공원",
        placeAddress: "안청동 736-1",
        Lat:35.2129735,
        Lng:126.8037158,
        memo: "",
    },
    {
        cardId: 5,
        placeName: "삼성전자 광주사업장",
        placeAddress: "하남산단6번로 107",
        Lat:35.2040949,
        Lng:126.8071876,
        memo: "",
    },
]);

const planList = ref([
    {
        cardId: 3,
        placeName: "마루샤브",
        placeAddress: "장덕동 1634",
        Lat:35.1905106,
        Lng:126.8169632,
        orderNumber: 2,
        day: 1,
        memo: "",
    },
    {
        cardId: 4,
        placeName: "24시 콩나물국밥",
        placeAddress: "장덕동 1302",
        Lat:35.1911555,
        Lng:126.8194957,
        orderNumber: 3,
        day: 1,
        memo: "",
    },
    {
        cardId: 2,
        placeName: "장인족발",
        placeAddress: "장덕동 1574",
        Lat: 35.190427,
        Lng:126.8125625,
        orderNumber: 1,
        day: 1,
        memo: "",
    },
    {
        cardId: 1,
        placeName: "안청근린공원",
        placeAddress: "안청동 736-1",
        Lat:35.2129735,
        Lng:126.8037158,
        orderNumber: 1,
        day: 2,
        memo: "",
    },
]);

const days = ref([])
const day = computed(() => planStore.dateDiff)

const checkD = ref(1)

const filteredPlan = ref([])
const filteredCard = ref([])
const noneFixCards = ref(cardList.value)
const FixCards = ref(days.value)
const controlOnStart = ref(true)
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

function clone ({ name }){
    return {name}
}

function pullFunction() {
    return controlOnStart.value ? "clone" : false;
}

function saveCards() {
    // 현재 카드 데이터를 로컬 스토리지에 저장
    localStorage.setItem("noneFixCards", JSON.stringify(noneFixCards.value));
    localStorage.setItem("FixCards", JSON.stringify(days.value));
}

function handleChange(){
    localStorage.setItem('noneFixCards', JSON.stringify(noneFixCards.value));
    localStorage.setItem("FixCards", JSON.stringify(days.value));
    for (let index = 0; index < days.value.length; index++) {
        localStorage.setItem(`day${index + 1}`, JSON.stringify(days.value[index]));
    }
}
function onCardMove() {
    handleChange()
    filteredPlan.value = JSON.parse(localStorage.getItem(`day${checkD.value}`));
    filteredCard.value = JSON.parse(localStorage.getItem('noneFixCards'));

    // saveCards()
}

const changeDate = (day)=> {
    checkD.value = day
    filteredPlan.value = JSON.parse(localStorage.getItem(`day${day}`));
}

const newCenter = ref({lat:33.450701, lng:126.570667 })
const setCenter = (element) => {
    console.log('클릭됨',element)
    newCenter.value.lat = element.Lat
    newCenter.value.lng = element.Lng
    console.log(newCenter.value)
}

onMounted(() => {
    if (days.value.length !== day.value) {
        days.value = []
        for (let i = 1; i <= day.value; i++){
            const dayPlan = planList.value.filter((item)=>item.day===i).sort((a,b)=> a.orderNumber - b.orderNumber)
            days.value.push(dayPlan)
        }
    }   
    loadCards()
    console.log('days', days.value)
})

    
</script>
<style scoped>
.hidden{
    display: block;
}
.active{
    display: block;
}
</style>
    
<style>
.overlay{
    background-color: white;
    border-radius: 5px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.8);
}
.marker-number{
    position: relative;
    top: 5px;
    left: 10px;
    color: black;
    width: 14px;
    height: 14px;
    background-color: #fff;
    border: black solid 1px;
    border-radius: 50%;
    font-size: 10px;
    font: bolder;
    display: inline-block;
    text-align: center;
}
</style>