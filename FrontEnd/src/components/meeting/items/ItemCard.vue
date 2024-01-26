<template>

    <div class="card card-w-title">
        <!-- {{ card.card_id }} -->
        {{ card.place.name }}
    </div>
</template>

<script setup>
defineProps({
    card: Object
})

</script>

<style scoped>
.card{
    width: 100px;
    height: 50px;
}
</style>