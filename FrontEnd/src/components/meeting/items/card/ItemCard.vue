<template>
    <div 
        draggable="true"
        class="draggable"
        data-card-id="{{ card.card_id }}"
        id="card-{{ card.card_id }}"
        @dragstart="dragEvent($event)"
        >

        <div :class="{'card': true, 'card-before': fold, 'card-after': !fold }">
            <a @click="fold=!fold">
                {{ card.placeName }}
            </a>
            <div id="memo" :class="{'hidden':(card.memo ==='')||(!fold), 'active':card.memo !==''}">
                <v-tooltip
                v-model="show"
                location="top"
                >
                <template v-slot:activator="{ props }">
                    <v-btn
                    icon
                    v-bind="props"
                    size="x-small"
                    >
                    <v-icon color="grey-lighten-1" size="medium">
                        mdi-text-box
                    </v-icon>
                    </v-btn>
                </template>
                <span>{{ memo }}</span>
                </v-tooltip>
            </div>
            <div :class="[fold? 'hidden' :'active']">
                <p style="font-size: x-small;">{{ card.placeAddress }}</p>
                <a v-if="memo!==''" @click="memocheck=!memocheck" :class="{'active':!memocheck, 'hidden':memocheck}" style="font-size: small;" >{{ memo }}</a>
                <a v-else @click="memocheck=!memocheck" :class="{'active':!memocheck, 'hidden':memocheck}" style="font-size: small; color:gainsboro;" >메모를 추가해주세요</a>
                <form :class="{'active':memocheck, 'hidden':!memocheck}" @submit.prevent="memoUpdate()">
                    <input type="text" style="font-size: small; width: 100%; height: auto;" autofocus v-model="memo">
                    <input type="submit" value="수정">
                </form>
                <div class="delete-btn">
                    <ConfirmPopup group="headless">
                        <template #container="{ message, acceptCallback, rejectCallback}">
                            <div class="border-round p-3">
                                <span>{{ message.message }}</span>
                                <div class="flex align-items-center justify-content-end gap-2 mt-3">
                                    <Button label="취소" outlined @click="rejectCallback" severity="secondary" size="small" text></Button>
                                    <Button label="삭제" @click="acceptCallback" size="small"></Button>
                                </div>
                            </div>
                        </template>
                    </ConfirmPopup>
                    <v-btn icon size="x-small" @click="requireConfirmation($event)">
                        <v-icon color="grey-lighten-1" size="medium">
                            mdi-trash-can-outline
                        </v-icon>
                    </v-btn>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';
import { cardMemoUpdateApi } from '@/api/cardApi'

import ConfirmPopup from 'primevue/confirmpopup';
import Button from 'primevue/button'
import { useConfirm } from 'primevue/useconfirm';

const confirm = useConfirm()
const propsCard = defineProps({
    card: Object
})
const show = ref(false)
const fold = ref(true)
const memocheck = ref(false)

const memo = ref(propsCard.card.memo)

// 삭제 확인
const requireConfirmation = (event) => {
    confirm.require({
        target:event.currentTarget,
        group: 'headless',
        message: '정말로 삭제하시겠습니까?',
        accept: () => {
            const emit = defineEmits(['deleteEmit'])
            const deleteEmit = function () {
                emit('deleteEmit', propsCard.card.cardId)
            }
            deleteEmit()
        },
        reject: () => {
        }
    })
}

const dragEvent = (event) => {
    console.log(event, propsCard.card.cardId, '이벤트가 시작됨')
}

// 메모 수정
const memoUpdate = () => {
    memocheck.value = !memocheck.value
    const payload = {'cardId': propsCard.card.cardId, 'memo':memo.value }
    cardMemoUpdateApi(payload, (response) => {
        if (response.data.dataHeader.successCode === 1) {
                    let msg = "카드 메모 수정 중 문제가 발생했습니다.";
                    alert(msg);
                } else {
                    console.log("메모 수정 성공");
                }
            },
            (error) => {
                console.log(error);
            }
            );
    }
</script>

<style scoped>
.delete-btn{
    position: absolute;
    bottom: 10px;
    right: 10px;
}
.card{
    width: 150px;
    position: relative;
    margin: 0%;
    margin-left: 5%;
    padding: 15px;
    cursor: pointer;
}
.card-before{
    height: 50px;
    transition: .2s;
}
.card-after{
    height: 60%;
    transition: .2s;
}
.active{
    display: block;
}
.hidden{
    display: none;
}

#memo{
    position: absolute;
    top: 10px;
    right: 10px;
}


</style>