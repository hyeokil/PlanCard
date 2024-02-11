<template>
    <div v-if="accountsStore.isLogin">
        <MyCalander></MyCalander>
    </div>
    <div v-else>
        <MyPlanList></MyPlanList>
    </div>
</template>

<script setup>
import MyPlanList from './MyPlanList.vue'
import MyCalander from "./MyCalander.vue";
import { useAccountsStore } from "@/stores/accountsStore";
import MyPlanListVue from "./MyPlanList.vue";
const accountsStore = useAccountsStore()
</script>

<style scoped>

</style>