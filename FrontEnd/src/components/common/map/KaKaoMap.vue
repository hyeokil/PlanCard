<template>
    <div>
        <div id="map" style="width: 100%; height: 100%"></div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
const map = ref(null);
const markers = ref(new Map());
const overlays = ref(new Map());
const polyline = ref(null);


const props = defineProps({
    cardPlaces: Array,
    detailPlaces: Array
});

// props 변경 감지
const cardPlaces = computed(() => {
    return props.cardList
})
let detailPlaces = computed(() => {
    return props.detailPlanList
})


// 여행계획들의 평균 위치를 계산하는 함수
async function calculateCenter(places) {
    const totalLat = places.reduce((acc, place)=> acc+place.placePosition[0]);
    const totalLng = places.reduce((acc, place)=> acc+place.placePosition[1]);
    return {
        lat: totalLat / places.length,
        lng: totalLng / places.length
    };
}


// 마커, 오버레이 초기화
async function clearMarkerAndOverlays() {
    console.log('clearMarkerAndOverlays 호출')
    
    //마커 제거
    markers.value.forEach((marker, key) => {
        // console.log(`마커 제거 전: ${key}`, marker);
        marker.setMap(null);
        // console.log(`마커 제거 후: ${key}`, marker);
    });
    markers.value.clear();
    
    //오버레이 제거
    overlays.value.forEach((overlay, key) => {
        overlay.setMap(null);
    });
    overlays.value.clear();
    
    // 폴리라인 제거
    if (polyline.value) {
        console.log('폴리라인제거');
        polyline.value.setMap(null);
        polyline.value = null;
    }
}



// 마커, 오버레이, 폴리라인 표시
async function displayMarkers() {
    console.log('displayMarkersAndPolyLine 호출')

    if (!window.kakao || !window.kakao.maps) {
        console.error('카카오맵 api 호출 x');
        return;
    }

    // 초기화
    const clear = await clearMarkerAndOverlays();


    // 1. 카드
    // 카드 마커 만들기
    cardPlaces.value.forEach((cardPlace) => {
        const position = new kakao.maps.LatLng(
            cardPlace.placePosition[0],
            cardPlace.placePosition[1]
        )

        // 마커 이미지 
        const imageSrc = '/image/icon/icon_pin_outline.png'
        const imageSize = new kakao.maps.Size(40, 40)
        const imageOption = { offset: new kakao.maps.Point(13, 35) }

        const markerImage = new kakao.maps.MarkerImage(
            imageSrc,
            imageSize,
            imageOption
        )

        const marker = new kakao.maps.Marker({
            map: map.value,
            position: position,
            image: markerImage,
        })

        markers.value.set(marker)
    })
    // 2. 계획
    // 계획 마커 만들기
    detailPlaces.value.forEach((detailPlace) => {
        const position = new kakao.maps.LatLng(
            detailPlace.placePosition[0],
            detailPlace.placePosition[1]
        )

        // 마커 이미지 
        const imageSrc = '/image/icon/icon_location_marker.png'
        const imageSize = new kakao.maps.Size(40, 40)
        const imageOption = { offset: new kakao.maps.Point(13, 35) }

        const markerImage = new kakao.maps.MarkerImage(
            imageSrc,
            imageSize,
            imageOption
        )

        console.log('인덱스는 어디로...', detailPlace.placePosition[2])
        const marker = new kakao.maps.Marker({
            map: map.value,
            position: position,
            image: markerImage,
            zIndex: detailPlace.placePosition[2]
        })

        // 오버레이 만들기
        const markerLabelContent = `<div class="marker-number">${detailPlace.placePosition[2] + 1}</div>`; // 여기서 index는 마커의 순서(1부터 시작)
        
        const markerLabel = new kakao.maps.CustomOverlay({
        content: markerLabelContent,
        map: map.value,
        position: marker.getPosition(),
        yAnchor: 1, // 마커 이미지의 중앙 아래에 오버레이가 오도록 설정
        zIndex: detailPlace.placePosition[2], // 순서대로 표시하기 위해 z-index 설정
        });

        markers.value.set(marker);
        overlays.value.set(markerLabel);

    })

    // 패스 만들기
    return detailPosition
}

async function displayPolyLine() {
    const position = await displayMarkers();
    let path = []
    props.detailPlaces[props.datecheck].forEach((place, index) => {
        path.push(new kakao.maps.LatLng(place.placePosition[0], place.placePosition[1]))
        if (props.detailPlaces[props.datecheck].length === index+1) {
            console.log(path)
            polyline.value = new kakao.maps.Polyline({
                path: path,
                strokeWeight: 5,
                strokeColor: "#FFAE00",
                strokeOpacity: 1,
                strokeStyle: "solid",
            });
            polyline.value.setMap(map.value);
        }
    })
}


// 지도 초기화
async function initMap() {
    const container = document.getElementById('map');
    let center = new kakao.maps.LatLng(33.450701, 126.570667);

    const options = {
        center: center,
        level: 5
    }

    // 새로 센터 찾기
    if (props.detailPlaces[props.datecheck].length!==0) {
        const newCenter = await calculateCenter(props.detailPlaces[props.datecheck])
        center = newCenter
        
    } else if (props.cardPlaces.length !== 0) {
        const newCenter = await calculateCenter(props.cardPlaces)
        center = newCenter

    }

    map.value = new kakao.maps.Map(container, options);
    
    // 마커 표시
    displayPolyLine();
}

const loadKaKaoMapScript = () => {
    const script = document.createElement('script');
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}&libraries=services,clusterer`;
    script.onload = () => kakao.maps.load(initMap);
    document.head.appendChild(script);

}

onMounted(() => {
    if (!window.kakao || !window.kakao.maps) {
        loadKaKaoMapScript();
    }
    else {
        initMap();
    }

})
</script>

<style scoped>
.overlay{
    background-color: white;
    border-radius: 5px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.8);
}
.marker-number{
    color: #fff;
    background-color: blue;
    border-radius: 50%;
    font-size: 14px;
    display: inline-block;
}
</style>