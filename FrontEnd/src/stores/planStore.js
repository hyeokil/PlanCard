import { ref, computed } from "vue";
import { defineStore } from "pinia";
export const usePlanStore = defineStore(
  "planStore", //<-- 여기엔 파일의 이름을 써야함
  () => {
    const plan = ref({
      id: 1,
      name: "첫번째 나들이",
      startDate: new Date(2024, 1, 10),
      endDate: new Date(2024, 1, 11),
    });

    const planMemberList = ref([]);
    const planDetailList = ref([]);
    const cardList = ref([]);

    const dateDiff = computed(
      () =>
        Math.abs(
          new Date(plan.value.endDate).getTime() - new Date(plan.value.startDate).getTime()
        ) /
          (1000 * 60 * 60 * 24) +
        1
    );

    const list = ref([
      { name: "객체1", id: 2 },
      { name: "객체2", id: 1 },
      { name: "객체3", id: 4 },
      { name: "객체4", id: 3 },
      { name: "객체5", id: 5 },
      { name: "객체6", id: 6 },
      { name: "객체8", id: 7 },
      { name: "객체7", id: 8 },
      { name: "객체9", id: 9 },
    ]);
    const planList = ref([
      { name: "객체1", id: 2, day: 1 },
      { name: "객체2", id: 1, day: 1 },
      { name: "객체3", id: 4, day: 1 },
      { name: "객체4", id: 3, day: 1 },
      { name: "객체5", id: 5, day: 1 },
      { name: "객체6", id: 6, day: 2 },
      { name: "객체8", id: 7, day: 2 },
      { name: "객체7", id: 8, day: 2 },
    ]);

    return {
      plan,
      planMemberList,
      planDetailList,
      cardList,
      dateDiff,
      planList,
      list,
    };
  }
);
