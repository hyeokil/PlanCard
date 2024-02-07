<template>
    <div>
        <h1>실시간 공동 편집</h1>
        <textarea v-model="sharedText"></textarea>
    </div>
</template>
    
<script setup>
import { ref, onUnmounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { WebsocketProvider } from 'y-websocket';
import * as Y from 'yjs';
import { debounce } from 'lodash';

const route = useRoute();
const roomId = route.params.id; // URL에서 roomId 추출
const wsUrl = `ws://localhost:1234`; // WebSocket 서버 URL

const doc = new Y.Doc();
let sharedText = ref('');
const yText = doc.getText('shared');

// WebSocket 프로바이더 초기화
const wsProvider = new WebsocketProvider(wsUrl, roomId, doc);

// Yjs 문서 관찰 및 sharedText 업데이트
yText.observe(debounce(() => {
    sharedText.value = yText.toString();
}, 500)); // 변화가 있을 때만 sharedText 업데이트

// sharedText의 변화를 감지하여 Yjs 모델 업데이트
watch(sharedText, (newValue, oldValue) => {
    if (newValue !== oldValue) {
        yText.delete(0, yText.length);
        yText.insert(0, newValue);
    }
});

onUnmounted(() => {
    wsProvider.destroy();
    doc.destroy();
});
</script>
