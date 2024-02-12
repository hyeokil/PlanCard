<template>
    <div class="d-flex gap-5 container"> 
        <div class="font-content calendar">
            <VCalendar
            style="width: 100%; height: 50vh; padding-top: 2rem ;"
            :attributes="attributes" 
            ></VCalendar>
        </div>
        <div class="font-content card detail">
            <div v-for="plan in plans" :key="plan.index" class="card">
                <div id="planName">
                    <p>{{ truncateName(plan.name) }} ( <span>{{ plan.people }}명</span> )</p>
                    <p class="cardp-2" id="planDate">{{ plan.startDate }} ~ {{ plan.EndDate }}</p>
                </div>
                <p class="p-1" id="planPeople"> 함께하는 사람: {{ plan.planMembersName.join(', ') }}</p>
    </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import { planListGetApi } from '@/api/planApi';
import _ from "lodash";
const colors = ref(['orange','yellow','green','teal','blue','indigo','purple','pink'])
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

<style scoped>
.container{
    margin-left: 10%;
    margin-right: 10%;
    display: flex;
    justify-content: space-around;
}
.calendar{
    width: 130%;
}
.detail{
    width: 100%;
}
</style>