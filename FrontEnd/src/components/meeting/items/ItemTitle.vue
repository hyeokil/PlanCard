<template>
    <div class="title">
        <div class="title-name">
            <div :class="[checkName=== '0' ? 'active' : 'hidden']" @click="goNameUpdate()">{{ planName }}</div>
            <form :class="[checkName=== '0' ? 'hidden' : 'active']" @submit.prevent="goNameUpdate()">
                <input type="text" v-model="planName" autofocus>
                <input class="primary" type="submit" value="확인">
            </form>
        </div>
        <div class="title-date">
            <div :class="[checkDate=== '0' ? 'active' : 'hidden']"  @click="goDateUpdate()" >{{ startYear }}년 {{ startMonth }}월 {{ startDate }}일({{ startDay }}) ~ {{ endYear }}년 {{ endMonth }}월 {{ endDate }}일({{ endDay }})</div>
            <form :class="[checkDate=== '0' ? 'hidden' : 'active']" @submit.prevent="goDateUpdate()">
                <input type="number" step="1" v-model="startYear" id="startYear" style="width: 45px;">년 
                <input type="number" step=1 v-model="startMonth" min="1" max="12" id="startMonth" style="width: 45px;">월
                <input type="number" step=1 v-model="startDate" min="1" max="31"  id="startDate" style="width: 45px;">일
                ~
                <input type="number" step="1" v-model="endYear" id="endYear" style="width: 45px;">년 
                <input type="number" step=1 v-model="endMonth" min="1" max="12" id="endMonth" style="width: 45px;">월
                <input type="number" step=1 v-model="endDate" min="1" max="31" id="endDate" style="width: 45px;">일
                <input class="primary" type="submit" value="확인">
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRoute } from "vue-router";
import { planNameUpdateApi, planDateUpdateApi } from "@/api/planApi"
import { usePlanStore } from "@/stores/planStore";

// 여행계획 단일조회 추가
const planStore = usePlanStore()
const checkName = ref('0') // 클래스 체크용
const checkDate = ref('0')  // 클래스 체크용
const planName = ref('텐텐여행1')

const route = useRoute()

// 시작날짜
const startYear = ref(planStore.plan.startDate.getYear()+1900)
const startMonth = ref(planStore.plan.startDate.getMonth()+1)
const startDate = ref(planStore.plan.startDate.getDate())
const startDay = computed(() => {
    let startDayRaw = new Date(startYear.value, startMonth.value-1, startDate.value).getDay()
    console.log('날짜', new Date(startYear.value, startMonth.value-1, startDate.value), startDayRaw)
    if (startDayRaw === 0){
        return '일'
    } else if (startDayRaw === 1){
        return '월'
    } else if (startDayRaw === 2){
        return '화'
    } else if (startDayRaw === 3){
        return '수'
    } else if (startDayRaw === 4){
        return '목'
    } else if (startDayRaw === 5){
        return '금'
    } else if (startDayRaw === 6){
        return '토'
    } else{
        return 'error'
    }
})

// 끝난날짜
const endYear = ref(planStore.plan.endDate.getYear()+1900)
// ref(planStore.plan.endDate.getYear())
const endMonth = ref(planStore.plan.endDate.getMonth()+1)
const endDate = ref(planStore.plan.endDate.getDate())
const endDay = computed(() => {
    let endDayRaw = new Date(endYear.value, endMonth.value-1, endDate.value).getDay()
    console.log('날짜', new Date(endYear.value, endMonth.value-1, endDate.value), endDayRaw)
    if (endDayRaw === 0){
        return '일'
    } else if (endDayRaw === 1){
        return '월'
    } else if (endDayRaw === 2){
        return '화'
    } else if (endDayRaw === 3){
        return '수'
    } else if (endDayRaw === 4){
        return '목'
    } else if (endDayRaw === 5){
        return '금'
    } else if (endDayRaw === 6){
        return '토'
    } else{
        return 'error'
    }
})


const goNameUpdate = () => {
    if (checkName.value === '0') {
        checkName.value = '1'
    } else {
        checkName.value = '0'
        const payload = {
            name: planName.value,
        }
        planNameUpdateApi(route.params.id, payload, (response) => {
            if (response.data.dataHeader.successCode === 1) {
                let msg = "계획 이름 변경 중 문제가 발생했습니다.";
                alert(msg);
            } else {
                console.log("계획 이름 변경 성공");
            }
        }, (error) => {
            console.log(error)
        }
        )
    }
}

const goDateUpdate = () => {
    if (checkDate.value === '0') {
        checkDate.value = '1'
    } else {
        checkDate.value ='0'
        const payload = {
            "startDate": `${startYear.value}` + '-' + `${startMonth.value - 1}` - `${startDate.value}`,
            "endDate":`${endYear.value}` + '-' + `${endMonth.value - 1}` - `${endDate.value}`
        }

        planDateUpdateApi(route.params.id, payload, (response) => {
            if (response.data.dataHeader.successCode === 1) {
                let msg = "계획 날짜 변경 중 문제가 발생했습니다.";
                alert(msg);
            } else {
                console.log("계획 날짜 변경 성공");
            }
        }, (error) => {
            console.log(error)
        }
        )
    }
}

</script>

<style scoped>
.title{
    display: flex;
    font-size: 30px;
    gap: 5%;
}
.active{
    display: block;
}

.hidden{
    display: none;
}

</style>
