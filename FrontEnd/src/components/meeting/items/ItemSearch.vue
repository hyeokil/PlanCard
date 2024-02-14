<template>
  <div class="searchPlace">
    <!-- 검색 -->
    <div>
      <h6 style="margin-left: 1rem;">장소 검색하기</h6>
      <input type="text" v-model="searchText" placeholder="장소를 입력해주세요">
    </div>
      <!-- 검색 -->
      
      <!-- 장소 목록 -->
      <div class="result-container">
        <div style="color: black;">검색 결과</div>
        <div class="searchResult">
          <div v-if="searchPlaces.length!==0">
            <div 
            :class="place-list" 
            v-for="place in searchPlaces" 
            :key="place.placeId">
            {{ place.placeName }}
              <!-- <div v-if="cardListRaw.value.findIndex(card => card.placeName === place.placeName)" @click.self.prevent class="card-in">
                {{ place.placeName }}
              </div>
              <div v-else @click="addToCardList(placeId)" class="card-not-in'">
                {{ place.placeName }}
              </div> -->
            </div>
          </div>
          <div v-else-if="searchPlaces.length===0&&searchText!==''">
            <div class="place-list" style="cursor:default;">
              <div>검색 결과가 없어요.😢</div>
              <div style="font-size: small;">다른 단어로 검색해주세요.</div>
            </div>
          </div>
        </div>
      </div>
      <!-- 장소 목록 -->
    
  </div>
</template>

<script setup>
import { ref, watch } from "vue"
import { cardCreateApi, placeSearchApi } from '@/api/cardApi';
import { usePlanStore } from "@/stores/planStore";

const planStore = usePlanStore()

// 여행지 검색
const searchText = ref("")
const searchPlaces = ref([])



searchPlaces.value = [
  {
      "placeId": 1,
      "placeName": "한국의 집"
  },
  {
      "placeId": 24,
      "placeName": "서울 남대문로 한국전력공사 사옥"
  },
  {
      "placeId": 48,
      "placeName": "한국은행 본관"
  },
  {
      "placeId": 49,
      "placeName": "한국은행 화폐박물관"
  },
  {
      "placeId": 132,
      "placeName": "한국금융사박물관"
  },
  {
      "placeId": 154,
      "placeName": "한국유림독립운동파리장서비"
  },
]
// watch(searchText, async(newV, oldV) => {
//     if (newV === "") {
//         searchPlaces.value = []
//     } else {
//         console.log('검색어가 바껴요', newV, typeof(newV))
//         const response = await placeSearchApi(newV);
//         if (response.data.dataHeader.successCode === 0) {
//             searchPlaces.value = response.data.dataBody
//             console.log('검색완?',searchPlaces.value)
//         }
//     }
// }, { deep: true })

async function addToCardList(placeId) {
  try {
    const response = await cardCreateApi(placeId);
    if (response.data.dataHeader.successCode === 0) {
      console.log('카드생성 완료')
      planStore.Added = true

    } else {
        alert(response.data.dataHeader.resultMessage);
    }
  } catch (error) {
    if (error.response) {
            console.error(error);
            const errorResponse = error.response.data;
            alert(errorResponse.dataHeader.resultMessage);
        } else if (error.message === 'Network Error' || error.code === 'ERR_NETWORK') {
            // 네트워크 에러 처리
            alert("서버에 연결할 수 없습니다. 네트워크 연결을 확인해주세요.");
        }
  }
}

const cardListRaw = ref([])
watch(planStore.cardListRaw, (newV, oldV) => {
  cardListRaw.value = planStore.cardListRaw
}, {deep:true}, {immediate:true})

// 방법1. place내의 값을 모두 가져와 backend에는 저장만하고 프론트단에서 따로 cardRaw에 추가하는 방법
// 방법2. 추가버튼을 누르면 백엔드로 넘어가 카드 생성 + 프론트에서 fetch 한번 더 하기

</script>

<style scoped>
.searchPlace{
  padding-top: 3rem;
  padding-left: 5%;
  padding-right: 5%;
  width: 100%;
}
input{
  width: 100%;
  background-color: #fff;
  border-radius: 10px;
  padding: 10px 10px 5px 15px;
}
input:focus{
  outline: 3px solid #3498DB;
}
.result-container{
  width: calc(30vw - 42px);
  padding: 1rem;
  margin-top: 1rem;
  height: 70vh;
  background-color: #fff;
  border-radius: 10px;
}
.searchResult{
  width: 100%;
  height: 62vh;
  overflow-y: auto;
}
.place-list{
  margin-top: 0.5rem ;
    width: 100%;
    border-radius: 5px;
    padding: 0.4rem;
    position: relative;
    padding-top: 0.7rem;
    padding-left: 1rem;
  }
.card-not-in{
  background-color: #fff;
  cursor: pointer;
}
.card-in{
  background-color: #3498DB;
  cursor: default;
}
</style>