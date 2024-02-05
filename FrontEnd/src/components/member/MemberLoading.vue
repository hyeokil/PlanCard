<template>
    <div>
        
    </div>
</template>
<script setup>
import { useRoute, useRouter } from 'vue-router';
import { onMounted } from 'vue';
import { useAccountsStore } from '@/stores/accountsStore';
import { finishSocialLoginApi } from "@/api/memberApi";


const route = useRoute();
const router = useRouter();

const oAuthDomain = route.params.oAuthDomain;
const code = route.query.code;

const accountsStore = useAccountsStore();

onMounted(async () => {
    try {
        const response = await finishSocialLoginApi(oAuthDomain, code);
        console.log(response.data);
        if (response.data.dataHeader.successCode === 0) {
            // 로그인 상태와 사용자 정보를 accountsStore에 저장
            accountsStore.setLogin(true);
            accountsStore.setMemberInfo(response.data.dataBody.memberInfo);
            router.push({ name: 'main' });  // 로그인 성공 후 메인 페이지로 이동  
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