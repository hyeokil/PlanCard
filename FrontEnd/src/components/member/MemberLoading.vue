<template>
    <div>
        
    </div>
</template>
<script setup>
import { useRoute, useRouter } from 'vue-router';
import { onMounted } from 'vue';
import { finishSocialLoginApi } from "@/api/memberApi";

const route = useRoute();
const router = useRouter();

const oAuthDomain = route.params.oAuthDomain;
const code = route.query.code;

onMounted(async () => {
    try {
        const response = await finishSocialLoginApi(oAuthDomain, code);
        console.log(response.data);
        if (response.data.dataHeader.successCode === 0) {
            router.push({ name: 'main' });
        } else {
            alert(response.data.dataHeader.resultMessage);
            router.push({ name: 'main' });
        }
    } catch (error) {
        console.error(error);
        alert("소셜 로그인 완료 과정 중 문제가 발생했습니다.");
    }
});
</script>
<style>
    
</style>