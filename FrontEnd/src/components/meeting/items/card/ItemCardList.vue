<template>
    <div class="grid p-fluid card p-2">
        <div class="col-12">
            <h4>카드</h4>
        </div>
        <div class="box d-flex">
            <ItemCard
            v-for="card in planStore.card_list"
            :key="card.card_id"
            :card = card
            />
        </div>
    </div>
</template>

<script setup>
import ItemCard from './ItemCard.vue'
import { usePlanStore } from "@/stores/planStore";

const planStore = usePlanStore()

</script>

<style scoped>
.card{
    position: relative;
}
.box{
    position:absolute;
    width: 100%;
    height: 75%;
    /* background-color: aqua; */
    overflow: auto;
    top: 20%;
    left: 10%;
    right: 10%;
}

.d-flex{
    flex-wrap: wrap;
}
</style>