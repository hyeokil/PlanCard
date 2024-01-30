<template>
    <!-- <div class="grid meeting"> -->
        <div class="col-12 mt-0 pt-0">
            <ItemTitle class="title"/>
            <!-- <div class="card card-w-title">
            </div> -->
        </div>
        <div class="grid">
            <div class="col-4 pb-0">
                <ItemCardList class="card-list" />
                <ItemFace class="face" />
            </div>
            <div class="col-8 pt-0">
                <ItemDetailPlanList class="plan mb-0"/>
                <ItemMap class="map pt-5"/>
            </div>
        </div>
    <!-- </div> -->
</template>



<script setup>
import ItemCardList from '@/components/meeting/items/card/ItemCardList.vue'
import ItemDetailPlanList from '@/components/meeting/items/detailplan/ItemDetailPlanList.vue'
import ItemFace from '@/components/meeting/items/ItemFace.vue'
import ItemTitle from '@/components/meeting/items/ItemTitle.vue'
import ItemMap from '@/components/meeting/items/ItemMap.vue'

    // 여행 계획 이름 바꾸기
    // 일정 바꾸기 따로 가능하게 만들기
    // 사람을 추가로 초대할 수 있게 만들기
</script>



<style scoped>
.meeting{
    width: 100vw;
    height: 100vh;
}
.title{
    width: 100vw;
    height: 5vh;
}
.card-list{
    height: 60vh;
}
.face{
    height: 15vh;
}
.plan{
    height: 30vh;
}
.map{
    height: 45vh;
}
</style>