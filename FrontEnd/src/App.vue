<template>
  <div>
    <router-view />
  </div>
</template>



<script setup>

</script>



<style>
  /* 폰트 정의 */
  @font-face {
    font-family: 'NanumBarunGothic';
    src: url('/fonts/NanumBarunGothic.eot');
    src: url('/fonts/NanumBarunGothic.eot') format('embedded-opentype'),url('/fonts/NanumBarunGothic.woff') format('woff');
  }

  /* 폰트 적용 */
  * {
    font-family: 'NanumBarunGothic', 'serif';
  }
</style>