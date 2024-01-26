<script setup>
import ItemCardList from '@/components/meeting/items/ItemCardList.vue'
import ItemDetailPlanList from '@/components/meeting/items/ItemDetailPlanList.vue'
import ItemFace from '@/components/meeting/items/ItemFace.vue'
import ItemTitle from '@/components/meeting/items/ItemTitle.vue'
import ItemMap from '@/components/meeting/items/ItemMap.vue'

    // 여행 계획 이름 바꾸기
    // 일정 바꾸기 따로 가능하게 만들기
    // 사람을 추가로 초대할 수 있게 만들기
</script>

<template>
    <div class="grid p-fluid">
        <div class="col-12">
            <div class="card card-w-title">
                <h1>MeetingDetail.vue</h1>
                <ItemTitle />
            </div>
        </div>
        <div class="col-6">
            <ItemCardList />
            <ItemFace />
        </div>
        <div class="col-6">
            <ItemDetailPlanList />
            <ItemMap />
        </div>
    </div>
            
            <div>
            </div>
            <!-- 제목  일정달력 -->
            <!-- 카드 리스트 -->
            <!-- 사람얼굴 -->
            <!-- 여행 세부계획 -->
            <!-- 맵 -->
</template>

<style scoped>

</style>