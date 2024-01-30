<template>
    <div class="card">
        {{ card.placeName }}
        <div id="memo" :class="[card.memo ==='' ? 'hidden':'active']">
            <v-tooltip
            v-model="show"
            location="right"
            >
            <template v-slot:activator="{ props }">
                <v-btn
                icon
                v-bind="props"
                size="x-small"
                >
                <v-icon color="grey-lighten-1" size="medium">
                    mdi-text-box
                </v-icon>
                </v-btn>
            </template>
            <span>{{ card.memo }}</span>
            </v-tooltip>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
defineProps({
    card: Object
})

const show = ref(false)
// const checkMemo = ref(0)

</script>

<style scoped>
.card{
    width: 150px;
    height: 50px;
    position: relative;
    margin: 0%;
    margin-left: 5%;
    padding: 15px;
}
.active{
    display: block;
}
.hidden{
    display: none;
}

#memo{
    position: absolute;
    top: 10px;
    right: 10px;
}
</style>