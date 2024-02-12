<template>
  <div v-if="streamManager" id="videoContainer">
    <ov-video :stream-manager="streamManager" id="userVideo"/>
    <!-- {{ clientData }}이게 내 현재 이름임 -->
    <p style="color: #3498DB;">{{ clientData }}</p>
  </div>
</template>



<script>
  export default {
    name: 'UserVideo',
  }
</script>

<script setup>
  import { computed } from 'vue';
  import OvVideo from '@/components/meeting/items/OvVideo.vue';

  const props = defineProps({
    streamManager: Object,
  })

  // clientData는 computed로 진행됨
  const clientData = computed(() => {
    const { clientData } = getConnectionData();
    // console.log(clientData);
    return clientData;
  });

  function getConnectionData() {
    const { connection } = props.streamManager.stream;
    return JSON.parse(connection.data);
  }
</script>



<style>
#videoContainer {
  display: flex;
  flex-direction: column; /* 아이템을 세로로 배치하기 위한 설정 */
  justify-content: center; /* 수직으로 중앙 정렬 */
  align-items: center; /* 수평으로 중앙 정렬 */


  margin-right: 5px;
  margin-left: 5px;
  margin-bottom: 0px;

}

#userVideo {
  width: auto;
  height: 80px;
  border: 3px #3498DB solid;
  
}

</style>