<template>
    <div 
    draggable="true"
    class="card p-3 mb-2">
        {{ detail.orderNumber }}
        {{ detail.placeName }}
        <div id="memo" :class="[detail.memo ==='' ? 'hidden':'active']">
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
            <span>{{ detail.memo }}</span>
            </v-tooltip>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
const item_props = defineProps({
  detail: Object
})

const show = ref(false)

// 이 페이지에서 해야하는 일: props로 plan_detail_list 하나씩 받아서 해당하는 카드를 가져와 보여주기
// detailplan에 place name, place address까지 가져옴 -> 위의 과정 생략가능


</script>

<style scoped>
.card{
    width: 150px;
    height: 50px;
    position: relative;
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