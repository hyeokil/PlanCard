<template>
    <div class="card col-12 p-fluid">
        {{ place.name }}
        {{ place.category }}
        {{ place.address }}
    </div>
</template>

<script setup>
import { ref, onBeforeMount } from "vue";
import { usePlanStore } from "@/stores/planStore";
const detail_props = defineProps({
    detail: Object
})

const planStore = usePlanStore()
const place = ref({
    place_id: '',
    name: '',
    address: '',
    category: '',
    image: '',
    latitude:'',
    longitude:'',
})
const getPlace = () => {
    planStore.card_list.forEach(card => {
        console.log( detail_props.detail.card_id, 'detail_props.detail.card_id')
        console.log( card.card_id, 'card.cardid')
        if (card.card_id === detail_props.detail.card_id) {
            planStore.place_list.forEach(pl => {
                console.log(pl.place_id, 'pl.placeid')
                if (pl.place_id === card.place_id) {
                    place.value.place_id = pl.place_id
                    place.value.name = pl.name
                    place.value.address = pl.address
                    place.value.category = pl.category
                    place.value.image = pl.image                   
                }
            })
        }
    });
    console.log(place.value)
}

onBeforeMount(() => {
    getPlace()
})

</script>

<style lang="scss" scoped>

</style>