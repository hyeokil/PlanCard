<template>
    <div id="detail" class="card p-fluid p-3">
        <h4>상세 계획</h4>
        <div class="box">
            <div class="wrap-vertical">
                <div class="d-flex">
                    <ItemDetailPlan 
                    v-for="day in days" :key="day"
                    :props="plan_detail[day]"
                    :day="day"
                    />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import ItemDetailPlan from "./ItemDetailPlan.vue";
import { ref, onMounted, watch } from 'vue'
import { usePlanStore } from "@/stores/planStore";
const planStore = usePlanStore()

const plan_detail = ref({}) // key: 일차 value: 카드list
const days = ref([])

watch(
    () => planStore.dateDiff,
    (newDiff) => {
        listsort();
}, {deep:true})

const listsort = () => {
    const plan_detail_list = planStore.plan_detail_list
    console.log(plan_detail_list)
    days.value = []
    plan_detail.value = {}
    // days 숫자 추가
    plan_detail_list.sort((a, b)=> a.orderNumber-b.orderNumber);
    for (let i = 1; i <= planStore.dateDiff; i++){
        days.value.push(i) 
    }

    // 상세계획을 최대 30일로 임의 지정함
    for (let i = 1; i <= 30; i++){ 
        plan_detail.value.hasOwnProperty(i) ? plan_detail.value[i] : (plan_detail.value[i] = []);
    }

    // 상세계획을 집어넣기
    plan_detail_list.forEach((item) => {
        plan_detail.value[item.day].push(item);
    })

    console.log('상세계획리스트는 변했나?',days.value)
}

onMounted(() => {
    listsort()
})

</script>

<style scoped>
.card{
    position: relative;
}

.box{
    position:absolute;
    top: 20%;
    left: 2.5%;
    width: 95%;
    height: 160px;
    /* overflow: hidden; */
    /* background-color: aqua; */
}
.wrap-vertical{
    width: 100%;
    height: 90%;
    color: #112031;
    /* background: #F0D9FF; */
    border: 1px solid #000;
    /* 가로 스크롤 */
    overflow-y: hidden ;
    overflow-x: scroll;
    white-space: nowrap;
}
/* .wrap-vertical::-webkit-scrollbar{
    display: none; 
} */
</style>