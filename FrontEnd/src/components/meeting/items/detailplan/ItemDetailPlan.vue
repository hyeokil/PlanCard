<template>
    <div class="p-2">
        <div class="box-1">
            <h5>{{ day }}일차</h5>
            <div class="box-2">
                <div class="box-3">
                    <ItemDetailPlanItem 
                    v-for="item in props.props" :key="item.orderNumber"
                    :detail="item" />
                    <div style="height: 20px;"></div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import ItemDetailPlanItem from "./ItemDetailPlanItem.vue";
const props = defineProps({
    props: Array,
    day:Number
})

</script>

<style scoped>
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
</style>