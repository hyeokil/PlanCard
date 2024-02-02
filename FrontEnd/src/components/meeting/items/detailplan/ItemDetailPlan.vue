<template>
    <div class="drag-container" @dragenter="DragEnter($event)">
        <div class="p-2">
            <div class="box-1">
                <h5 @click="$emit('checkDay')">{{ day }}일차</h5>
                <div class="box-2">
                    <div class="box-3">
                        <!-- 플랜 아이템 부분 -->
                            <div 
                            v-for="detail in props.props" :key="detail.orderNumber"
                            >
                            <div
                            @dragstart="isDrag=true"
                            @dragend="isDrag=false"
                            draggable="true"
                            :class="{'card': true, 'p-3':true, 'mb-2':true, 'draggable':true,'dragging': isDrag}"
                            >
                                {{ detail.orderNumber }} )          
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
                            </div>
                        <!-- <ItemDetailPlanItem 
                        v-for="item in props.props" :key="item.orderNumber"
                        :detail="item" />
                        <div style="height: 20px;"></div> -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
// import ItemDetailPlanItem from "./ItemDetailPlanItem.vue";
import { ref, onMounted } from "vue";
const props = defineProps({
    props: Array,
    day:Number
})
const isDrag = ref(false)

// function getDragAfterElement(container, y) {
//     const draggableElements = [...container.querySelectorAll('.draggable:not(.dragging)')]

// }

onMounted(() => {   
})

// 하위 컴포넌트로 둔거 다 가져오는 공사중

</script>

<style scoped>
h5{
    cursor: pointer;
}
.draggable{
    cursor: pointer;
}
.box-1{
    width: 250px;
    height: 100px;
    position: relative;
    background-color: bisque;
}
.box-2{
    position: absolute;
    background-color: aqua;
    top: 30px;
    width: 250px;
    height: 100px;
    overflow: scroll;
    overflow-x: hidden;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}
.box-3{
    height: 50px;
}
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