<template>
    <div class="grid p-fluid card p-2">
        <div class="col-12">
            <h4>카드</h4>
            <div>
                <button @click="startGet">조회시작</button>
                <button @click="endGet">조회중지</button>
            </div>
        </div>
        <div class="box d-flex">
                <ItemCard
                v-for="card in cardList"
                :key="card.card_id"
                :card = card
                @delete-emit="cardDelete"
                />
        </div>
    </div>
</template>

<script setup>
import ItemCard from './ItemCard.vue'
import { ref, onMounted, defineEmits } from "vue";
import { useRoute } from "vue-router";
import { cardGetApi, cardDeleteApi } from '@/api/cardApi'

const route = useRoute()
const cardList = ref([])

const emit = defineEmits(['cardUpdate'])
const cardUpdate = function () {
    emit('cardUpdate', cardList.value)
}

//카드 조회 요청
let cardTimer;

function cardGet() {
    cardGetApi(route.params.id, (response) => {
            if (response.data.dataHeader.successCode === 1) {
                let msg = "카드 조회 중 문제가 발생했습니다.";
                alert(msg);
            } else {
                console.log("카드 조회 성공");
                cardList.value = response.data.dataBody
                cardUpdate()
            }
        },
        (error) => {
            console.log(error);
        }
        );
} 

function startGet() {
    cardTimer = setInterval(cardGet, 2000)
}

function endGet() {
    clearInterval(cardTimer)
}

//카드 제거
const cardDelete = (cardId) => {
    cardDeleteApi(
        cardId,
        (response) => {
            if (response.data.dataHeader.successCode === 1) {
                let msg = "카드 삭제 중 문제가 발생했습니다.";
                alert(msg);
            } else {
                console.log("카드 삭제 성공");
                endGet()
                cardGet()
                startGet()
            }
        },
        (error) => {
            console.log(error);
        }
    );
}

onMounted(() => {
    startGet
})
</script>

<style scoped>
.card{
    position: relative;
}
.box{
    position:absolute;
    width: 82%;
    height: 75%;
    /* background-color: aqua; */
    overflow: auto;
    top: 20%;
    left: 10%;
    right: 10%;
}

.d-flex{
    flex-wrap: wrap;
}
</style>