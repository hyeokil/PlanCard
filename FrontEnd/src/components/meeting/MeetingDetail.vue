<script setup>
import ItemCardList from '@/components/meeting/items/ItemCardList.vue'
import ItemDetailPlanList from '@/components/meeting/items/ItemDetailPlanList.vue'
import ItemFace from '@/components/meeting/items/ItemFace.vue'
import ItemTitle from '@/components/meeting/items/ItemTitle.vue'


    // 여행 계획 이름 바꾸기
    // 일정 바꾸기 따로 가능하게 만들기
    // 사람을 추가로 초대할 수 있게 만들기
</script>

<template>
    <div>
        <h1>MeetingDetail.vue</h1>
        <ItemTitle />
        <div>
            <ItemCardList />
            <ItemFace />
        </div>
        <div>
            <ItemDetailPlanList />
        </div>
        <!-- 제목  일정달력 -->
        <!-- 카드 리스트 -->
        <!-- 사람얼굴 -->
        <!-- 여행 세부계획 -->
        <!-- 맵 -->

    </div>
</template>

<style scoped>
.map{
    width: 500px;
    height: 400px;
}
</style>