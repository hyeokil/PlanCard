

<script setup>
import { ref, watch, computed } from "vue";
import { cardCreateApi, placeSearchApi } from '@/api/cardApi';
import { usePlanStore } from "@/stores/planStore";
import { useRoute } from 'vue-router';

const planStore = usePlanStore()
const route = useRoute();
const planId = route.params.id; // URL에서 planId를 추출합니다.

// 여행지 검색
const searchText = ref('')
// const searchText = computed(()=>planStore.sttSearchText)
const searchPlaces = ref([])

// 검색결과 가져오기
watch(() => planStore.sttSearchText, (newV) => {
  searchText.value = planStore.sttSearchText
})

// 검색결과가 cardListRaw에 있으면 파랑색 글씨+ 클릭불가능으로 바꿈

const cardListRaw = computed(()=>planStore.cardListRaw)
// const cardListRaw = ref([])


// 검색하기 
watch(searchText, async(newV, oldV) => {
    if (newV === "") {
        searchPlaces.value = []
    } else {
        const response = await placeSearchApi(newV);
        if (response.data.dataHeader.successCode === 0) {
            searchPlaces.value = response.data.dataBody
        }
      }
    }, { deep: true })

    
// 카드 추가하기
async function addToCardList(place) {
  try {
    console.log(planStore.Added)
    const response = await cardCreateApi(planId, place.placeId);
    if (response.data.dataHeader.successCode === 0) {
      console.log('카드생성 완료')
      planStore.Added = !planStore.Added
      console.log(planStore.Added)
    
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


// 방법1. place내의 값을 모두 가져와 backend에는 저장만하고 프론트단에서 따로 cardRaw에 추가하는 방법
// 방법2. 추가버튼을 누르면 백엔드로 넘어가 카드 생성 + 프론트에서 fetch 한번 더 하기
// -> 각 회의 방의 cardId가 생성되는 방식이 순서대로가 아님 (다른 방까짐 모두 통합하여 cardId생성)


</script>

<template>
  <div class="searchPlace">
    <!-- 검색 -->
    <div style="position: relative;">
      <h6 style="margin-left: 1rem;">장소 검색하기</h6>
      <input type="text" v-model="searchText">
      <!-- <input type="text" v-model="searchText" placeholder="장소를 입력해주세요"> -->
      <button style="position: absolute; top: 2.8rem; right: 1rem; color: #3498DB;"><i class="pi pi-check"></i></button>
      <!-- 사실 이 버튼은 의미가 없음 그냥 검색결과가 나오기까지 시간벌기용 -->
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
            :key="place.placeId"
            id="placeThing">
              <div @click.self.prevent 
              :class="{'card-in':true, 
              'active':cardListRaw.findIndex(card => card.placeName === place.placeName)!==-1, 
              'hidden':cardListRaw.findIndex(card => card.placeName === place.placeName)===-1}">
                {{ place.placeName }}
              </div>
              <div @click="addToCardList(place)" 
              :class="{'card-not-in': true,
              'active':cardListRaw.findIndex(card => card.placeName === place.placeName)===-1, 
              'hidden':cardListRaw.findIndex(card => card.placeName === place.placeName)!==-1}">            
                {{ place.placeName }}
              </div>
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

<style scoped>
.active{
  display: block;
}
.hidden{
  display: none;
}
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
  overflow-x: hidden;
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
  color: #3498DB;
  cursor: default;
}

#placeThing {
  padding-left: 15px;
  cursor: pointer;
  transition: transform 0.3s ease;
  margin: 5px 0px 5px 0px;
}
#placeThing:hover {
  transform: scale(1.05);
}
</style>