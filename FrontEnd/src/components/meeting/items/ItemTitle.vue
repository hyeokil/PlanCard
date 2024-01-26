<template>
    <div>
        <div :class="[checkName=== '0' ? 'active' : 'hidden']" @click="goNameUpdate()">{{ planName }}</div>
        <form :class="[checkName=== '0' ? 'hidden' : 'active']" @submit.prevent="goNameUpdate()">
            <input type="text" v-model="planName" autofocus>
        </form>
        <div @click="goDateUpdate()">{{ startDate }} ~ {{ endDate }}</div>
        <form :class="[checkDate=== '0' ? 'hidden' : 'active']" @submit.prevent="goDateUpdate()">
            <VDatePicker v-model.range="range" mode="date"/>
            <input class="primary" type="submit" value="확인">
            <button data-v-d3f5b421="" class="p-button p-component p-button-rounded p-button-info mr-2 mb-2" type="button" aria-label="Info" data-pc-name="button" data-pc-section="root" data-pd-ripple="true">
                <!---->
                <span class="p-button-label" data-pc-section="label">Info</span>
                <!---->
                <span role="presentation" aria-hidden="true" data-p-ink="true" data-p-ink-active="false" class="p-ink" data-pc-name="ripple" data-pc-section="root"></span>
            </button>
        </form>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { usePlanStore } from "@/stores/planStore";

const planStore = usePlanStore()
const checkName = ref('0') // 체크용
const checkDate = ref('0')  // 체크용
const planName = ref('여행 계획')
const range = ref({
startDate: planStore.plan.startDate,
endDate: planStore.plan.endDate,
});
const startDate = ref(range.value.startDate)
const endDate = ref(range.value.endDate)

const goNameUpdate = () => {
    if (checkName.value === '0') {
        checkName.value = '1'
    } else {
        checkName.value ='0'
    }
    // console.log(checkName.value)
    const payload = {
        name: planName,
        startDate: '',
        endDate: '',
    }
    planStore.goCheck(payload)
}

const goDateUpdate = () => {
    if (checkDate.value === '0') {
        checkDate.value = '1'
    } else {
        checkDate.value ='0'
    }
    const payload = {
        name: '',
        startDate: range.value.startDate,
        endDate: range.value.endDate,
    }
    planStore.goCheck(payload)
}

</script>

<style scoped>
.active{
    display: block;
}

.hidden{
    display: none;
}

</style>
