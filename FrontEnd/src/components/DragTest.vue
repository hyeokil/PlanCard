<template>

  <div class="row" style="display: flex;">

    <div class="col-3 card" style="margin-right: 20px;">
      <h3>stt로 생성된 카드</h3>
      <draggable
        class="dragArea list-group"
        :list="noneFixCards"
        :clone="clone"
        :group="{ name: 'people', pull: pullFunction }"
        @change="handleChange"
        @start="start"
        item-key="id"
      >
        <template #item="{ element }">
          <div class="list-group-item card">
            {{ element.name }}
          </div>
        </template>
      </draggable>
    </div>

    <div class="col-3 card" style="margin-right: 20px;" v-for="(fixCard, index) in fixCards" :key="index">
      <h3>Day {{ index + 1 }}</h3>
      <draggable
        class="dragArea list-group"
        :list="fixCard"
        :clone="clone"
        :group="{ name: 'people', pull: pullFunction }"
        @change="handleChange"
        @start="start"
        item-key="id"
      >
        <template #item="{ element }">
          <div class="list-group-item card">
            {{ element.name }}
          </div>
        </template>
      </draggable>
    </div>


  </div>
</template>

<script>
import draggable from "@/vuedraggable";
let idGlobal = 8;

export default {
  name: "clone-on-control",
  display: "Clone on Control",
  instruction: "Press Ctrl to clone element from list 1",
  order: 4,
  components: {
    draggable,
  },
  data() {
    return {
      noneFixCards: [],
      fixCards: [
      [
        { name: "재훈", id: 1 },
        { name: "지수", id: 2 },
        { name: "동근", id: 3 },
      ],
      [
        { name: "혁일", id: 4 },
        { name: "정인", id: 5 },
        { name: "세은", id: 6 },
      ],
    ],

      controlOnStart: true,
    };
  },
  mounted() {
    // 페이지가 로드될 때 저장된 카드 데이터를 불러옴
    this.loadCards();
  },
  methods: {
    clone({ name }) {
      return { name, id: idGlobal++ };
    },
    pullFunction() {
      return this.controlOnStart ? "clone" : true;
    },
    start({ originalEvent }) {
      this.controlOnStart = originalEvent.ctrlKey;
    },
    handleChange() {
      // 데이터가 이동될 때마다 실행되는 함수
      // 로컬 스토리지에 저장
      localStorage.setItem('noneFixCards', JSON.stringify(this.noneFixCards));
      for (let index = 0; index < this.fixCards.length; index++) {
        localStorage.setItem(`day${index + 1}`, JSON.stringify(this.fixCards[index]));
      }
    },
    loadCards() {
      // 로컬 스토리지에서 저장된 카드 데이터를 불러옴
      const storedCards = JSON.parse(localStorage.getItem("cards"));
      if (storedCards) {
        this.cards = storedCards;
      }
    },
    saveCards() {
      // 현재 카드 데이터를 로컬 스토리지에 저장
      localStorage.setItem("cards", JSON.stringify(this.cards));
      localStorage.setItem("noneFixCards", JSON.stringify(this.noneFixCards));

    },
    onCardMove() {
      // 카드가 이동될 때마다 호출되는 메서드
      // 카드의 순서를 업데이트하고 저장
      this.saveCards();
    }
  },
};

</script>
<style scoped></style>
