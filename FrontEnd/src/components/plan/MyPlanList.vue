<template>
    <div style="overflow: hidden; position: relative;">
        <div style="margin-top: 5rem; display: flex; flex-direction: column; gap: 3rem;">
            <div class="parent">
                <div class="part">
                    <div class="background-image">
                        <div class="first-text-content">
                            <div style="font-size: 1.6rem;">
                                <div>친구, 연인, 가족, 나와 함께 쓰는 여행 플래너</div>
                            </div>
                            <div style="font-size: 5rem; color: #3498DB;">플랜카드</div>
                            <div style="font-size: 1.2rem;">플랜카드로 모두 함께 즐거운 여행을 만들어요.</div>
                            <button id="startBtn" @click="goLogin()">바로 시작하기</button>
                        </div>
                    </div>
                </div>
                <div class="part">
                    <div class="background-image" ref="firstPage">
                        <div class="img-content">
                            <img src="/question-two-color.png" alt="">
                        </div>
                        <div class="text-content">
                            <div style="position: relative;">
                                <div class="bubble1" >잠깐만, 방금 전에 어디 말했더라?</div>
                            </div>
                            <div class="main">기억이 나지 않을 땐,</div>
                            <div><span :class="{'fadein':isIntersecting1}">STT</span>를 통해 말했던 장소를 바로 카드로 만들어줘요.</div>
                        </div>
                    </div>
                </div>
                <div class="part">
                    <div class="background-image" ref="secondPage">
                        <div class="img-content-1">
                            <img src="/loading-two-color.png" alt="">
                        </div>
                        <div class="text-content-1">
                            <div style="position: relative;">
                                <div class="bubble2">정리한 계획 보내줄게, 기다려봐. </div>
                            </div>
                            <div style="margin-top: 1rem; margin-bottom: 1rem; padding-left: 20%;">
                                <div>(1시간 뒤)</div>
                            </div>
                            <div style="position: relative;">
                                <div class="bubble1">언제 줘?</div>
                            </div>
                            <div class="main">기다리기 싫을 땐,</div>
                            <div><span :class="{'fadein':isIntersecting2}">공동편집</span>으로 여행계획을 곧바로 공유할 수 있어요.</div>
                        </div>
                    </div>
                </div>
                <div class="part">
                    <div class="background-image" style="height: calc(100vh - 103px);" ref="thirdPage">
                        <div class="img-content">
                            <img src="/manager-two-color.png" alt="">
                        </div>
                        <div class="text-content">
                            <div style="position: relative;">
                                <div class="bubble1">예약 마감까지 10분 남았는데 다들 왜 답장이 없어? ㅠㅠ</div>
                            </div>
                            <div class="main">바로 결정해야할 땐,</div>
                            <div><span :class="{'fadein':isIntersecting3}">WebRTC</span>를 통해 실시간 화상통화로 모두 함께 계획을 짤 수 있어요.</div>
                        </div>  
                    </div>
                </div>
                <div class="background-image part last-background" ref="child">
                    <div class="last-text-content">
                            <!-- Generator: Adobe Illustrator 27.9.0, SVG Export Plug-In . SVG Version: 6.00 Build 0)  -->
                            <div>
                                <svg version="1.1" id="레이어_2" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px"
                                y="0px" viewBox="0 0 383.4 368.83" style="enable-background:new 0 0 383.4 368.83; position: absolute; top: 0px; left: 0px;" xml:space="preserve">
                                <path id="P" :class="{'st0':true, 'logo':isIntersecting}" d="M115.03,134.84c0,0,9.98,1.78,19.92-1.75c9.93-3.52,10.43-10.06,10.43-10.06
                                c-0.08-0.24,1.79-9.89-5.83-14.44c-8.42-5.04-17.73-2.42-22.34-0.51c-1.79,0.74-2.94,2.5-2.94,4.44v41.12"/>
                                <path id="L" :class="{'st1':true, 'logo':isIntersecting}" d="M160.29,104.56l8.85,0.81c0.81,0.07,1.43,0.75,1.43,1.56v45.21"/>
                                <path id="A-1" :class="{'st1':true, 'logo':isIntersecting}" d="M226.22,151.17l-10.01-20.72c0,0-4.56-8.53-15.04-6.45c-10.49,2.08-11.99,10.43-11.38,16.13
                                c0.51,4.75,4.45,10.63,12.38,10.29c8.18-0.35,16.21-9.04,14.99-17.74"/>
                                <path id="N" :class="{'st1':true, 'logo':isIntersecting}" d="M269.86,151.17v-18.81c0,0-2.5-9.13-12.29-7.68c0,0-7.17,0.8-12.29,7.51l-3.18-10.02
                                c0,0,1.9,3.38,2.92,8.48c0.06,0.77,0.16,2.02,0.22,2.79l-0.68,18.11"/>
                                <path id="C" :class="{'st1':true, 'logo':isIntersecting}" d="M133.12,217.29c0,0-16.47-11.47-28.59,2.94c-0.6,0.72-1.09,1.52-1.5,2.36
                                c-2.16,4.43-7.46,18.3,2.89,31.5c1.22,1.55,2.83,2.75,4.63,3.55c4.87,2.15,15.01,4.97,23.71-3.44"/>
                                <path id="A-2" :class="{'st1':true, 'logo':isIntersecting}" d="M191.7,260.78l-10.01-20.72c0,0-4.56-8.53-15.04-6.45c-10.49,2.08-11.99,10.43-11.38,16.13
                                c0.51,4.75,4.45,10.63,12.38,10.29c8.18-0.35,16.21-9.04,14.99-17.74"/>
                                <path id="R" :class="{'st1':true, 'logo':isIntersecting}" d="M231.61,238.8c0,0-13.86-6.11-19.11,8.24l-0.08,17.24c0.51-28.95-1.7-34.47-1.7-34.47"/>
                                <path id="D" :class="{'st1':true, 'logo':isIntersecting}" d="M282.81,212.39l0.85,37.87c0,0,0.34,11.19-13.75,10.52c-14.09-0.67-15.28-11.54-15.28-11.54
                                s-2.46-10.83,9.85-14.43c19.19-3.57,19.19,15.45,19.19,15.45"/>
                                </svg>
                                <img src="/logo_background.png" style="padding-left: 1rem; width: 100%; height: 100%;" alt="">
                            </div>
                        <div style="text-align: center; margin-top: 1rem; font-size: 2rem; width: 300px;">
                            <div>다같이 쓰는 플랜카드,</div>
                            <div>한번 시작해볼까요? </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();
const firstPage = ref()
const secondPage = ref()
const thirdPage = ref()
const child = ref()
const isIntersecting1 = ref(false)
const isIntersecting2 = ref(false)
const isIntersecting3 = ref(false)
const isIntersecting = ref(false)

const goLogin = () => {
    router.push({name: 'member-login'})
}
onMounted(() => {
    function callback([entry], observer) {
        console.log(entry.isIntersecting, 'logo')
        isIntersecting.value = entry.isIntersecting
    }
    function callback1([entry], observer) {
        console.log(entry.isIntersecting, '1')
        isIntersecting1.value = entry.isIntersecting
    }
    function callback2([entry], observer) {
        console.log(entry.isIntersecting, '2')
        isIntersecting2.value = entry.isIntersecting
    }
    function callback3([entry], observer) {
        console.log(entry.isIntersecting, '3')
        isIntersecting3.value = entry.isIntersecting
    }
    const observer = new IntersectionObserver(callback);
    const observer1 = new IntersectionObserver(callback1);
    const observer2 = new IntersectionObserver(callback2);
    const observer3 = new IntersectionObserver(callback3);
    observer1.observe(firstPage.value)
    observer2.observe(secondPage.value)
    observer3.observe(thirdPage.value)
    observer.observe(child.value)

})
console.log(child)

</script>

<style scoped>
span{
    background-color: #3498DB;
    color: #fff;
    padding-left: 5px;
    padding-right: 5px;
    padding-top: 3px;
    padding-bottom: 1px;
    opacity: 0;
}
.parent{
    width: 100vw;
    height: calc(100vh - 105.73px);
    scroll-snap-type: y mandatory;
    overflow-y: scroll;
    overflow-x: hidden;

}
.part{
    scroll-snap-align: center;
}
.bubble1{
padding-top: 8px;
padding-bottom: 3px;
padding-left: 10px;
padding-right: 10px;    
background-color: #3498DB;
border-radius: 5px;
width: fit-content;
z-index: 2;
margin-bottom: 1rem;
color: white;
}
.bubble1::before{
    content:'';
    position: absolute;
    width: 0px;
    left: -5px;
    bottom: 0px;
    left: -5px;
    border-bottom: 10px solid #3498DB;
    border-top: 10px solid transparent;
    border-right: 5px solid #3498DB;
    border-left: 5px solid transparent;
    position: absolute;
    z-index: 1;
}
.bubble2{
padding-top: 8px;
padding-bottom: 3px;
padding-left: 10px;
padding-right: 10px;    
background-color: #3498DB;
border-radius: 5px;
width: fit-content;
z-index: 2;
margin-bottom: 1rem;
color: white;
}
.bubble2::after{
    content:'';
    position: absolute;
    width: 0px;
    top: 0px;
    left:218px;
    border-bottom: 10px solid transparent;
    border-top: 10px solid #3498DB;
    border-right: 5px solid transparent;
    border-left: 5px solid #3498DB;
    position: absolute;
    z-index: 1;
}
.background-image{
    background-color: #fff; 
    width: 100vw; 
    height: calc(100vh - 105.73px);
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
}
.first-text-content{
    position: absolute;
    top: 30%;
    left: 20%;
}

/* .text-content{
    position: absolute;
    top: 40%;
    left: 20%;
}
.text-content-1{
    position: absolute;
    top: 30%;
    right: 20%;
}
.img-content{
    position: absolute;
    top: 20%;
    right: 20%;
}
.img-content-1{
    position: absolute;
    top: 20%;
    left: 20%;
} */
button{
    margin-top: 20px;
}
.main{
    font-size: 3.4rem;
}
img{
    width: 400px;
    height: auto;
}

.last-background{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}
.last-background>.last-text-content{
    position: relative;
    width: 250px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}
.fadein{
    position:relative;
    animation:  fadein 2s ease-in 0s forwards;
    /* -webkit-animation: fadein 2s ease-in 0s forwards; */
    animation-fill-mode: both;
    border-radius: 5cm;
    padding: 3px 7px 1px 7px;
}
@keyframes fadein{
    from{
        transform: translateX(-10%);
        -webkit-transform: translateX(-10%);
    }
    to{
        opacity: 1;
        transform: translateX(0);
        -webkit-transform: translateX(0);
    }
}
/* @-webkit-keyframes fadein{
    from{
        opacity: 0;
        -webkit-transform: translateX(-10%);
    }
    to{
        opacity: 1;
        -webkit-transform: translateX(0);
    }
} */
.logo{
    stroke-dasharray: 620; 
    stroke-dashoffset: -620;
    animation: logo-anim 1s cubic-bezier(0, 0.3, 0, 1) 0s forwards;
}

@keyframes logo-anim {
    from {stroke-dashoffset : -620px;}
    to {stroke-dashoffset : 0px;}
}
.st0{fill:none;stroke:#FFFFFF;stroke-width:12;stroke-linecap:round;stroke-miterlimit:10;}
.st1{fill:none;stroke:#FFFFFF;stroke-width:12;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;}


#startBtn {
  background-color: #3498DB;
  color: #fff;
  width: 13rem;
  height: 4rem;
  border-radius: 5px;
  font-size: 1.2rem;
  transition: transform 0.3s ease;
}
#startBtn:hover {
transform: scale(1.05);
  border-color: #3498db
}
</style>