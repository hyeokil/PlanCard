import { ref, computed } from "vue";
import { defineStore } from "pinia";
export const usePlanStore = defineStore(
  "planStore", //<-- 여기엔 파일의 이름을 써야함
  () => {
    const plan = ref({});

    const planMemberList = ref([]);
    const planDetailList = ref([]);
    const cardListRaw = ref([]);

    const dateDiff = computed(
      () =>
        Math.abs(
          new Date(plan.value.endDate).getTime() - new Date(plan.value.startDate).getTime()
        ) /
          (1000 * 60 * 60 * 24) +
        1
    );
    const isMeetingView = ref(false);
    const Added = ref(true);

    return {
      plan,
      planMemberList,
      planDetailList,
      cardListRaw,
      dateDiff,
      isMeetingView,
      Added,
    };
  }
);
