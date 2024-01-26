import { ref } from "vue";
import { defineStore } from "pinia";

export const usePlanStore = defineStore(
  "plan",
  () => {
    const plan = ref({
      id: 1,
      name: "첫번째 나들이",
      startDate: new Date(2024, 1, 9),
      endDate: new Date(2024, 1, 11),
    });

    const plan_member_list = ref([
      {
        plan_member_id: 1,
      },
      {
        plan_member_id: 2,
      },
    ]);

    const plan_detail_list = ref([
      {
        plan_detail_id: 1,
        cardId: 2,
        plan_id: 1,
        orderNumber: 2,
        day: 1,
        memo: "",
      },
      {
        plan_detail_id: 2,
        cardId: 3,
        plan_id: 1,
        orderNumber: 3,
        day: 1,
        memo: "",
      },
      {
        plan_detail_id: 3,
        cardId: 4,
        plan_id: 1,
        orderNumber: 1,
        day: 1,
        memo: "",
      },
      {
        plan_detail_id: 4,
        cardId: 2,
        plan_id: 1,
        orderNumber: 1,
        day: 2,
        memo: "",
      },
    ]);

    // 상세계획 조회나 생성때 card로 바꿔주세용

    const card_list = ref([
      {
        cardId: 2,
        planName: "첫번째 나들이",
        placeName: "장인족발",
        memo: "",
      },
      {
        cardId: 3,
        planName: "첫번째 나들이",
        placeName: "마루샤브",
        memo: "",
      },
      {
        cardId: 4,
        planName: "첫번째 나들이",
        placeName: "24시 콩나물국밥",
        memo: "",
      },
    ]);

    const place_list = ref([
      {
        place_id: 1,
        name: "장인족발",
        address: "장덕동",
        category: "food",
        image: "",
      },
      {
        place_id: 2,
        name: "마루샤브",
        address: "수완지구",
        category: "food",
        image: "",
      },
      {
        place_id: 3,
        name: "24시 콩나물국밥",
        address: "장덕동",
        category: "food",
        image: "",
      },
    ]);

    const dateDiff = ref(
      Math.abs(
        (plan.value.endDate.getTime() - plan.value.startDate.getTime()) / (1000 * 60 * 60 * 24)
      )
    );

    const goCheck = (payload) => {
      if (payload.name !== "") {
        plan.value.name = payload.name;
        // axios요청 제목변경
      } else if (payload.startDate !== "" && payload.endDate !== "") {
        plan.value.startDate = payload.startDate;
        plan.value.endDate = payload.endDate;
        // axios요청 날짜변경
      }
      console.log(dateDiff);
    };

    return {
      plan,
      plan_member_list,
      plan_detail_list,
      card_list,
      place_list,
      dateDiff,
      goCheck,
    };
  },
  { persist: true }
);
