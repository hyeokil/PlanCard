<template>
    <div class="font-content card p-fluid">
        <VCalendar expanded 
        :attributes="attributes" 
        ></VCalendar>
    </div>
</template>

<script setup>
import { onMounted, ref, computed } from "vue";
import { planListGetApi } from '@/api/planApi';
import _ from "lodash";
const colors = ref(['orange','yellow','green','teal','blue','indigo','purple','pink'])
const plans = ref([
])
    
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

onMounted(fetchPlans)
</script>

<style scoped>
</style>