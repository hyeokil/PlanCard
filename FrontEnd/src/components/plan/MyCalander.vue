<template>
    <div class="d-flex gap-5 container"> 
        <div class="font-content calendar">
            <VCalendar
            class="calendar-v"
            :attributes="attributes" 
            ></VCalendar>
        </div>
        
        <div class="font-content card detail">
            <div>
                <div v-for="plan in plans" :key="plan.index" class="planList">
                    <div class="p-3" style="position: relative;">
                        <div style="position: absolute;  top:0px; left: 0px; border-radius: 10px 10px 0px 0px; background-color: #3498DB; width:100%; height: 40px;"></div>
                        <div style="position: absolute; width:95%;">
                            <div class="d-flex justify-content-between font-title" style="color: #fff; font-size: 16px;">
                                <div>{{ truncateName(plan.name) }} ( <span>{{ plan.people }}명</span> )</div>
                                <div>{{ plan.startDate }} ~ {{ plan.EndDate }}</div>
                            </div>
                        </div>
                        <div style="width: 30px; height: 40px;"></div>
                        <div>{{ plan.planMembersName.join(' 님, ') }} 님이 함께 계획 중 {{ _.sample(emoji) }}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import { planListGetApi } from '@/api/planApi';
import _ from "lodash";
const colors = ref(['orange','yellow','green','teal','blue','indigo','purple','pink'])
const emoji = ref(['🥰','🤩','😏','🥳','😇','😆','😊'])
const plans = ref([])

const attributes = computed(() => [
    ...plans.value.map(plan => {
        const color = _.sample(colors.value)
        console.log(color)
        return (
            {
                key: plan.name,
                highlight: {
                    start: {
                        color: color,
                        fillMode: 'outline'
                    },
                    base: {
                        color: color,
                        fillMode: 'light'
                    },
                    end: {
                        color: color,
                        fillMode: 'outline'
                    }
                },
                dates: {
                    start: new Date(plan.startDate),
                    end: new Date(plan.endDate)
                },
                popover: {
                    label: plan.name,
                    visiblitiy: 'click'
                }
            }
        )
    })
    ])

// 여행 계획 목록을 불러오는 함수
const fetchPlans = async () => {

    try {
        const response = await planListGetApi();
        if (response.data.dataHeader.successCode === 0) {
        plans.value = response.data.dataBody.map(plan => ({
            ...plan,
            people: plan.planMemberCount, // API 응답과 컴포넌트 내 변수명 매핑
            startDate: plan.startDate.split('-').join('.'),
            EndDate: plan.endDate.split('-').join('.'),
        }))
        } else {
        alert(response.data.dataHeader.resultMessage);
        }


    } catch (error) {
        console.error(error);
        alert("여행 계획 리스트 불러오기 중 오류가 발생했습니다.");
    }
}

// 이름이 20글자를 넘어가면 요약 ("...")
const truncateName = (name) => {
    if (name.length > 20) {
        return name.slice(0, 20) + "...";
    } else {
        return name;
    }
}

onMounted(fetchPlans)
</script>
<style>
.calendar-v{
    width: 100%; 
    height: 477px; 
    padding: 1rem;
    padding-top: 2rem;
    padding-bottom: 2rem;
}
.vc-weeks{
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 400px;
}
.vc-weekday-1{
    color: red;
}
.vc-weekday-7{
    color: blue;
}
</style>
<style scoped>

.container{
    margin-bottom: 0;
    margin-top: 0;
    margin-left: 10%;
    margin-right: 10%;
    display: flex;
    justify-content: space-around;
}
.calendar{
    width: 400px;
    /* height: (100vh - 161.73px); */
    height: 477px;
}
.detail{
    width: calc(80vw - 400px);
    /* height: (100vh - 161.73px); */
    height: 477px;
    overflow-y: auto;

}
.planList{
    background-color: #EAF9FF;
    border-radius: 10px;
    margin-bottom: 1rem;
}

</style>