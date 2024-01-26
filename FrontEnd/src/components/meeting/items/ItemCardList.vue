<template>
    <div class="grid p-fluid  gap-5">
        <div class="col-12">
            <h1>itemcardlist</h1>
        </div>
            <ItemCard
            v-for="card in planStore.card_list"
            :key="card.card_id"
            :card = card
            class="col-3"
            />
    </div>
</template>

<script setup>
import ItemCard from './ItemCard.vue'
import { usePlanStore } from "@/stores/planStore";

const planStore = usePlanStore()

</script>

<style scoped>

</style>