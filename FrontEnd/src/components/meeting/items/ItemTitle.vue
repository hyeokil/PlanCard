<template>
    <div>
        <h1>title</h1>
        <div :class="[titlecheck=== '0' ? 'active' : 'hidden']" @click="goTitleUpdate()">{{ planTitle }}</div>
        <form :class="[titlecheck=== '0' ? 'hidden' : 'active']" @submit.prevent="goTitleUpdate()">
            <input type="text" v-model="planTitle" >
        </form>
        <div @click="goDateUpdate()">{{ startDate }} ~ {{ endDate }}</div>
        <form :class="[datecheck=== '0' ? 'hidden' : 'active']" @submit.prevent="goDateUpdate()">
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

const titlecheck = ref('0')
const datecheck = ref('0')
const planTitle = ref('여행 계획')
const range = ref({
start: new Date(),
end: new Date(),
});
const startDate = ref('')
const endDate = ref('')

const goTitleUpdate = () => {
    if (titlecheck.value === '0') {
        titlecheck.value = '1'
    } else {
        titlecheck.value ='0'
    }
    console.log(titlecheck.value)
    goCheck()
}

const goDateUpdate = () => {
    if (datecheck.value === '0') {
        datecheck.value = '1'
    } else {
        datecheck.value ='0'
    }
    console.log(datecheck.value)
    goCheck()
}

const goCheck = () => {
    startDate.value = range.value.start
    endDate.value = range.value.end
    const payload = {
        'planTitle': planTitle.value,
        'Date': range.value, 
    }
    console.log(payload)    
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
