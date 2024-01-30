<template>
    <div>
        <KaKaoMap 
        :places="places"
        />
    </div>
</template>

<script setup>
import KaKaoMap from "@/components/common/map/KaKaoMap.vue";
// import { ref, onMounted } from "vue";
import { usePlanStore } from "@/stores/planStore";
const planStore = usePlanStore()

const places = planStore.card_list


// latLng 추가할 수 있는 공간 만들기 => position으로 만듦
// 카카오맵에 가야하는 것
// 1. 전체 카드의 주소
// 2. 상세계획 카드
// 3. 각 일자별로 분류한 상세계획 보내주기
// => 근데 여기서 for문으로 position필드 추가한 데이터 만들어 보내려고하니까
//    뭔가.... 오래걸려서 그른가 안가네...
// plan_list에서 정보 받아올 때 address말고 빈 필드 하나 추가해달라고 할까...
// (수정) 이 페이지에서 해야하는일: 여기서 position필드 추가하지 말고...
// kakaomap에서 추가해야될 거 같다!!

</script>

<style scoped>
div{
    width: 100%;
    height: 100%;
}
</style>