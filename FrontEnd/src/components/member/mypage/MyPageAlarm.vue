<template>
    <div>
        <v-card>
            <v-card-title>모달 제목</v-card-title>
            <v-card-text>
                <h1>MyPageAlarm.vue</h1>
            </v-card-text>
            <v-card-actions>
                <v-btn @click="memberStore.closeMyPageAlarm()">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </div>
</template>


<script setup>
    import { useMemberStore } from '@/stores/memberStore';

    const memberStore = useMemberStore();


</script>

<style scoped>

</style>