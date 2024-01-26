<template>
    <div class="grid">
        <div class="col-12 md:col-6">
            <div class="card p-fluid">
                <h1>MeetingCreate.vue</h1>
            </div>
        </div>
        <div class="col-12 md:col-6">
            <div class="card p-fluid">    
                <form @submit.prevent="goMeeting">
                    <div class="field">
                        <label for="plan-name">여행계획이름</label>
                        <input type="text" name="plan-name" id="plan-name">
                    </div>
                    <div class="field">
                        <label for="plan-date"> 여행일자 </label>
                        <Calendar 
                        showIcon
                        v-model="dates" 
                        selectionMode="range"
                        :manual-input="false" 
                        id="plan-date"
                        />
                    </div>
                    <div class="field">
                        <label for="invite"> 초대할 사람 </label>
                        <div class="col-12 mb-2 lg:col-4 lg:mb-0">
                            <span class="p-input-icon-left p-input-icon-right">
                                <i class="pi pi-user" />
                                <InputText type="text" placeholder="Search" />
                                <i class="pi pi-search" />
                            </span>
                            <!-- <MultiSelect v-model="multiselectValue" :options="multiselectValues" optionLabel="name" placeholder="Select Countries" :filter="true">
                                <template #value="slotProps">
                                    <div class="inline-flex align-items-center py-1 px-2 bg-primary text-primary border-round mr-2" v-for="option of slotProps.value" :key="option.code">
                                        <span :class="'mr-2 flag flag-' + option.code.toLowerCase()" style="width: 18px; height: 12px" />
                                        <div>{{ option.name }}</div>
                                    </div>
                                    <template v-if="!slotProps.value || slotProps.value.length === 0">
                                        <div class="p-1">Select Countries</div>
                                    </template>
                                </template>
                                <template #option="slotProps">
                                    <div class="flex align-items-center">
                                        <span :class="'mr-2 flag flag-' + slotProps.option.code.toLowerCase()" style="width: 18px; height: 12px" />
                                        <div>{{ slotProps.option.name }}</div>
                                    </div>
                                </template>
                            </MultiSelect> -->
                        </div>
                    </div>
                    <Button type="submit"> 시작하기 </Button>
                    <!-- <Button label="Submit" class="mr-2 mb-2"></Button> -->

                </form>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue";
// import { useRouter } from "vue-router";
import Calendar from 'primevue/calendar'
const dates = ref(new Date())
// const router = useRouter()
const goMeeting = () => {
    // 화상회의 생성
    // 여행계획 생성
    // 여행 세부계획 생성
    // 사람들에게 초대 요청
    // 화상회의로 바로 이동
    // router.push({ name: 'meeting-detail', params: { id: 1 } })
    console.log(dates.value)
}


</script>

<style scoped>

</style>