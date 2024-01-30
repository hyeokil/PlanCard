<template>
    <div id="div1">
        <h1>My plan</h1>
        <v-btn class="startBtn" id="addBtn">
            <p style="margin-bottom: 20%;">+</p>
        </v-btn>
    </div>
    
    <hr id="separator">

    <div v-for="plan in plans" :key="plan.index" class="card f-fluid" id="plan">
        <div id="planName">
            <p>{{ plan.name }}</p>
        </div>
        <div id="div2">
            <p class="card f-fluid" id="planPeople">인원 : {{ plan.people }}명</p>
            <p class="card f-fluid" id="planDate">{{ plan.startDate }} ~ {{ plan.EndDate }}</p>
            <v-btn class="startBtn">
                <router-link :to="{path: 'view/:id'}" class="router-link-active">
                    Start
                </router-link>
            </v-btn>
        </div>
    </div>
</template>



<script setup>
  import { ref } from "vue";

  const plans = ref([
    {
        name: "텐텐 여행1",
        people: 6,
        startDate: "2024.01.29",
        EndDate: "2024.02.01"
    },
    {
        name: "텐텐 여행2",
        people: 6,
        startDate: "2024.02.23",
        EndDate: "2024.02.28"
    },
  ])


</script>



<style scoped>
  #div1 {
    display: flex; /* 하위 요소들을 가로 정렬. */
    flex-wrap: nowrap; /* 하위 요소들을 한 줄에 모두 표시. */
    align-items: center; /* 하위 요소들을 수직 가운데 정렬. */
    justify-content: space-between; /* 좌우 정렬 */
  }
  h1 {
    font-weight: bold;
  }
  #addBtn {
    background-color: #3498DB;
    color: white;
    font-size: 40px;
    font-weight: bold;
    padding: 0;
    margin: 0;

    display: flex;
    justify-content: center;
    align-items: center;
  }

  #separator {
    border: none;
    border-top: 4px solid #3498DB;
    border-radius: 10px;
  }

  #plan {
    border: 1px solid rgba(0, 0, 0, 0.1);
    background-color: #EAF9FF;
    height: 70px;
    display: flex; /* 하위 요소들을 가로 정렬. */
    flex-wrap: nowrap; /* 하위 요소들을 한 줄에 모두 표시. */
    align-items: center; /* 하위 요소들을 수직 가운데 정렬. */
  }
  #planName {
    color: #3498DB;
    font-weight: bold;
    font-size:xx-large;
    margin-top: 0;
    margin-bottom: 0;
    width: 70%;
    /* 작아지면 텍스트 가리기 */
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  #div2 {
    display: flex;
    flex-wrap: nowrap;
    width: 50%;
    justify-content: flex-end;
  }
  #planPeople{
    border: 1px solid rgba(0, 0, 0, 0.1);
    color: black;
    font-weight: bold;
    font-size: larger;
    margin-right: 2%;
    margin-left: 2%;
    margin-top: 0;
    margin-bottom: 0;
    padding: 2%;
    padding-top: 1%;
    padding-bottom: 1%;
  }
  #planDate {
    border: 1px solid rgba(0, 0, 0, 0.1);
    color: black;
    font-weight: bold;
    font-size: larger;
    margin-right: 2%;
    margin-left: 2%;
    margin-top: 0;
    margin-bottom: 0;
    padding: 2%;
    padding-top: 1%;
    padding-bottom: 1%;
  }
  .startBtn {
    border: 1px solid rgba(0, 0, 0, 0.1);
    background-color: #3498DB;
    color: #FFFFFF;
    font-weight: bold;
    margin-right: 2%;
    margin-left: 2%;    
  }
  .router-link-active { /* router-link의 글자 색이 변하지 않게 하는 css */
    background-color: transparent !important; /* 배경색을 투명으로 설정 */
    color: inherit !important; /* 글자색을 상속 받음 */
  }
</style>