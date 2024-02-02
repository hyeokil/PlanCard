<template>
    <div>
        <KaKaoMap 
        :card-places="cardList"
        :detail-places="detailPlanList"
        />
    </div>
</template>

<script setup>
import KaKaoMap from "@/components/common/map/KaKaoMap.vue";
import { computed } from "vue";

const props = defineProps({
    cardList: Array,
    detailPlanList: Array,
})

// props 변경 감지
const cardList = computed(() => {
    return props.cardList
})
let detailPlanList = computed(() => {
    return props.detailPlanList
})

</script>

<style scoped>
div{
    width: 100%;
    height: 100%;
}
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