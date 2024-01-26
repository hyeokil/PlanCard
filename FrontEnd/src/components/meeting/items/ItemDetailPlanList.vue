<template>
    <div id="detail">
        <h1>itemdetailplanlist</h1>
        <div v-for="day in days" :key="day" class="col-12">
            <h1>{{ day }}</h1>
            <ItemDetailPlan v-for="detail in plan_detail[day]" :key="detail.order" 
            :detail="detail"
            />
        </div>
    </div>
</template>

<script setup>
import ItemDetailPlan from "./ItemDetailPlan.vue";
import { ref, onMounted } from 'vue'
import { usePlanStore } from "@/stores/planStore";
const planStore = usePlanStore()
console.log(planStore.plan_detail_list)

const plan_detail = ref({}) // 일차:key 카드list: value
const days = ref([])

const listsort = () => {
    const plan_detail_list = planStore.plan_detail_list

    console.log(plan_detail_list);
    console.log(planStore.plan_detail_list)

    plan_detail_list.sort((a, b)=> a.orderNumber-b.orderNumber);
    for (let i = 1; i <= planStore.dateDiff; i++){
        plan_detail.value.hasOwnProperty(i) ? plan_detail.value[i] : (plan_detail.value[i.toString()] = []);
        days.value.push(i)
        console.log(plan_detail);
    }
    plan_detail_list.forEach((item) => {
        plan_detail.value[item.day].push(item);
    })

    console.log(plan_detail);

    // 모르겠는점: object depth가 깊어지면 store에서 가져올 수 없음
    // 그러므로 for을 돌려서 전부 cardlist에서 찾고 placelsit에서 찾아야하나?
    // 어떻게하면 card와 place를 찾아올 수 있을까
    // itemdetailplan에서 for문으로 돌리면 해결~~^^* (foreach 두번돌리고 beforeMount함~~)

}

// plan detail key마다 하나씩 draggable list에 넣어줘야함....
// document에 cretat....르르 써야하나...
// vue3 draggable.next 활용하기 + itemlist

onMounted(() => {
    listsort()
})


</script>

<style scoped>

</style>