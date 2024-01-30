import { ref, computed } from "vue";
import { defineStore } from "pinia";
import { useRoute } from "vue-router";
export const usePlanStore = defineStore(
  "planStore", //<-- 여기엔 파일의 이름을 써야함
  () => {
    const plan = ref({
      id: 1,
      name: "첫번째 나들이",
      startDate: new Date(2024, 1, 10),
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
        cardId: 2,
        placeName: "장인족발",
        placeAddress: "장덕동 1574",
        orderNumber: 2,
        day: 1,
        memo: "장충동왕족발보쌈보다 장인족발이지",
      },
      {
        cardId: 3,
        placeName: "마루샤브",
        placeAddress: "장덕동 1634",
        orderNumber: 3,
        day: 1,
        memo: "",
      },
      {
        cardId: 4,
        placeName: "24시 콩나물국밥",
        placeAddress: "장덕동 1302",
        orderNumber: 1,
        day: 1,
        memo: "",
      },
      {
        cardId: 2,
        placeName: "장인족발",
        placeAddress: "장덕동 1574",
        orderNumber: 1,
        day: 2,
        memo: "장충동왕족발보쌈보다 장인족발이지",
      },
    ]);

    const card_list = ref([
      {
        cardId: 2,
        placeName: "장인족발",
        placeAddress: "장덕동 1574",
        placePosition: "",
        memo: "장충동왕족발보쌈보다 장인족발이지",
      },
      {
        cardId: 3,
        placeName: "마루샤브",
        placeAddress: "장덕동 1634",
        placePosition: "",
        memo: "",
      },
      {
        cardId: 4,
        placeName: "24시 콩나물국밥",
        placeAddress: "장덕동 1302",
        placePosition: "",
        memo: "",
      },
    ]);

    const place_list = ref([
      {
        place_id: 1,
        name: "장인족발",
        address: "장덕동 1574",
        image: "",
      },
      {
        place_id: 2,
        name: "마루샤브",
        address: "장덕동 1634",
        image: "",
      },
      {
        place_id: 3,
        name: "24시 콩나물국밥",
        address: "장덕동 1302",
        image: "",
      },
    ]);

    const meeting_view = ref(true);
    /////////////////////////////////////////////
    // const plan = ref(null);
    // const plan_member_list = ref(null);
    // const plan_detail_list = ref(null);
    // const card_list = ref(null);
    // const place_list = ref(null);
    /////////////////////////////////////////////////////

    ////////////////////////////////////////////////////
    // const dateDiff = ref(
    //   Math.abs(
    //     (plan.value.endDate.getTime() - plan.value.startDate.getTime()) / (1000 * 60 * 60 * 24)
    //   )
    // );
    /////////////////////////////////////////////////////

    const dateDiff = computed(
      () =>
        Math.abs(
          new Date(plan.value.endDate).getTime() - new Date(plan.value.startDate).getTime()
        ) /
          (1000 * 60 * 60 * 24) +
        1
    );

    const goCheck = (payload) => {
      if (payload.name !== null) {
        plan.value.name = payload.name;
        // axios요청 제목변경
      } else {
        plan.value.startDate = new Date(payload.date[0], payload.date[1], payload.date[2]);
        plan.value.endDate = new Date(payload.date[3], payload.date[4], payload.date[5]);
        // axios요청 날짜변경
      }
      console.log(plan.value);
    };

    return {
      plan,
      plan_member_list,
      plan_detail_list,
      card_list,
      place_list,
      meeting_view,
      dateDiff,
      goCheck,
    };
  }
);
