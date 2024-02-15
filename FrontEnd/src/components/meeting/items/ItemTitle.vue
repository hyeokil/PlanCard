<template>
    <div class="title" v-if="planDetail">
        <div class="title-name font-title">
            <div :class="[checkName === '0' ? 'active' : 'hidden']" @click="fetchPlanNameUpdate()">{{ planDetail.name }}
            </div>
            <form :class="[checkName === '0' ? 'hidden' : 'active']" @submit.prevent="fetchPlanNameUpdate()">
                <input type="text" v-model="planDetail.name" style="width: 250px; color: gray;">
                <input type="submit" id="titleSubmit" value="확인">
                <!-- <input class="primary" type="submit" value="확인"> -->
            </form>
        </div>
        <div class="title-date font-content" style="color:silver;">
            <div :class="[checkDate === '0' ? 'active' : 'hidden']" @click="fetchPlanDateUpdate()">{{ parsedStartDate.year
            }}년 {{
    parsedStartDate.month
}}월 {{ parsedStartDate.date }}일({{ parsedStartDate.day }}) ~ {{ parsedEndDate.year }}년 {{
    parsedEndDate.month }}월 {{ parsedEndDate.date }}일({{ parsedEndDate.day }})</div>
            <form :class="[checkDate === '0' ? 'hidden' : 'active']" @submit.prevent="fetchPlanDateUpdate()">
                <input type="number" step="1" v-model="parsedStartDate.year" id="startYear" style="width: 45px;">년
                <input type="number" step=1 v-model="parsedStartDate.month" min="1" max="12" id="startMonth"
                    style="width: 45px;">월
                <input type="number" step=1 v-model="parsedStartDate.date" min="1" max="31" id="startDate"
                    style="width: 45px;">일
                ~
                <input type="number" step="1" v-model="parsedEndDate.year" id="endYear" style="width: 45px;">년
                <input type="number" step=1 v-model="parsedEndDate.month" min="1" max="12" id="endMonth"
                    style="width: 45px;">월
                <input type="number" step=1 v-model="parsedEndDate.date" min="1" max="31" id="endDate"
                    style="width: 45px;">일
                <input class="primary" type="submit" value="확인">
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoute } from "vue-router";
import { planGetApi, planNameUpdateApi, planDateUpdateApi } from "@/api/planApi"
import { usePlanStore } from "@/stores/planStore";
import { defineEmits } from 'vue';

const emit = defineEmits(['update-dates']);

// 여행계획 단일조회 추가
const planStore = usePlanStore()
const checkName = ref('0') // 클래스 체크용
const checkDate = ref('0')  // 클래스 체크용

const route = useRoute()
const planId = route.params.id;

const planDetail = ref(null);


// 여행 계획을 가져오는 메서드
const fetchPlanDetail = async () => {
    try {
        const response = await planGetApi(planId);
        if (response.data.dataHeader.successCode === 0) {
            const fetchedPlan = response.data.dataBody;
            planDetail.value = fetchedPlan;

            emit('update-dates', { startDate: fetchedPlan.startDate, endDate: fetchedPlan.endDate });
            // console.log(planDetail.value);
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
};

// startDate와 endDate의 요일을 계산하는 computed 속성
const getDayOfWeek = (date) => {
    const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
    return daysOfWeek[new Date(date).getDay()];
};

// 시작 날짜를 년, 월, 일로 파싱하는 computed 속성
const parsedStartDate = computed(() => {
    if (planDetail.value && planDetail.value.startDate) {
        const date = new Date(planDetail.value.startDate);
        return {
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            date: date.getDate(),
            day: getDayOfWeek(planDetail.value.startDate)
        };
    }
    return { year: '', month: '', date: '', day: '' };
});

// 종료 날짜를 년, 월, 일로 파싱하는 computed 속성
const parsedEndDate = computed(() => {
    if (planDetail.value && planDetail.value.endDate) {
        const date = new Date(planDetail.value.endDate);
        return {
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            date: date.getDate(),
            day: getDayOfWeek(planDetail.value.endDate)
        };
    }
    return { year: '', month: '', date: '', day: '' };
});

// 여행 계획명 수정 메서드
const fetchPlanNameUpdate = async () => {
    if (checkName.value === '0') {
        checkName.value = '1';
    } else {
        try {
            const param = {
                name: planDetail.value.name
            };
            const response = await planNameUpdateApi(planId, param);
            if (response.data.dataHeader.successCode === 0) {
                // 성공 시, UI 상에서 변경된 이름 반영
                planDetail.value.name = param.name;

                // planStore에 저장
                planStore.plan.name = param.name;
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

        checkName.value = '0';
    }
};

// 날짜 포맷을 'YYYY-MM-DD' 형식으로 변경
const formatDateString = (year, month, day) => {
    const formattedMonth = month.toString().padStart(2, '0');
    const formattedDay = day.toString().padStart(2, '0');
    return `${year}-${formattedMonth}-${formattedDay}`;
};

// 여행 일자 수정 메서드
const fetchPlanDateUpdate = async () => {
    if (checkDate.value === '0') {
        checkDate.value = '1';
    } else {
        try {
            const startDate = formatDateString(parsedStartDate.value.year, parsedStartDate.value.month, parsedStartDate.value.date);
            const endDate = formatDateString(parsedEndDate.value.year, parsedEndDate.value.month, parsedEndDate.value.date);

            const param = {
                startDate: startDate,
                endDate: endDate
            };
            const response = await planDateUpdateApi(planId, param);
            if (response.data.dataHeader.successCode === 0) {
                // 성공 시, UI 상에서 변경된 날짜 반영
                planDetail.value.startDate = param.startDate;
                planDetail.value.endDate = param.endDate;

                emit('update-dates', { startDate: param.startDate, endDate: param.endDate });
                // console.log(planDetail.value);
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

        checkDate.value = '0';
    }
}

onMounted(fetchPlanDetail);
</script>

<style scoped>
.title {
    display: flex;
    flex-direction: column;
    gap: 1%;
    padding-left: 10%;
    align-items: start;
    justify-content: end;
}

#titleSubmit {
    border: 2px solid rgba(0, 0, 0, 0.5);
    border-radius: 5cm;
    padding: 5px;
    padding-bottom: 1px;
    margin-bottom: 3px;
}
.title-name {
    font-size: 20px;
}

.title-date {
    font-size: 14px;
}

.active {
    display: block;
}

.hidden {
    display: none;
}
</style>
