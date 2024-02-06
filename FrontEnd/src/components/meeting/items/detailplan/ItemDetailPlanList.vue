<template>
    <div>
        <div id="detail" class="card p-fluid p-3">
            <h4>상세 계획</h4>
            <div class="box">
                <div class="wrap-vertical">
                    <div class="d-flex">
                        <ItemDetailPlan 
                        v-for="day in days" :key="day"
                        :props="planDetail[day]"
                        :day="day"
                        @check-day="dateChange(day)"
                        />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import ItemDetailPlan from "./ItemDetailPlan.vue";
import { ref, onMounted } from 'vue'
import { planDetailGetApi } from '@/api/planApi'
import { useRoute } from "vue-router";

const route = useRoute()

const detailPlanList = ref([])
const emit = defineEmits(['detailPlanUpdate'])
const detailPlanUpdate = function () {
    emit('detailPlanUpdate', detailPlanList.value)
}


const activeDate = ref(1)
const dateChange = (date) => {
    activeDate.value = date
    console.log(date)
}


// 마운트될 때 상세계획 조회 요청
function detailPlanGet() {
    planDetailGetApi(route.params.id, (response) => {
            if (response.data.dataHeader.successCode === 1) {
                let msg = "상세계획 조회 중 문제가 발생했습니다.";
                alert(msg);
            } else {
                console.log("상세계획 조회 성공");
                if (response.data.dataBody.length > 0) {
                    // 상세계획의 개수가 0개 이상이라면 (이미 상세계획을 만들어뒀다면 emit)
                    detailPlanList.value = response.data.dataBody
                    detailPlanUpdate()    
                }
            }
        },
        (error) => {
            console.log(error);
        }
        );
} 

onMounted(() => {
    detailPlanGet()
})


</script>

<style scoped>
.card{
    position: relative;
}
#detail{
    height: 30vh;
}

#map{
    height: 45vh;
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