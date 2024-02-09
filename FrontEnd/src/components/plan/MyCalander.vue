<template>
    <div>
        <VCalendar expanded :attributes="attributes" />
    </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
const attributes = ref([
    {
        key: 'today',
        highlight: true,
        dates: new Date(),
    },
    {
        key: '여행2',
        highlight: {
            start: {
                color:'red',
                fillMode: 'outline'
            },
            base: {
                color:'red',
                fillMode: 'light'
            },
            end: {
                color:'red',
                fillMode: 'outline'
            }
        },
        dates: {
            start: new Date('2024-02-08'),
            end: new Date('2024-02-13')
        }
    },
    
])

const loadPlans = () => {
    //plan 전체 조회
}

const events = ref([])
const colors = ref([])
onMounted(() => {
    
})
</script>

<style scoped>

</style>