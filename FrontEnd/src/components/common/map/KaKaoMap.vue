<template>
    <div>
        <h1>KaKaoMap.vue</h1>
        <div id="map" style="width: 500px; height: 400px"></div>
    </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";
const map = ref(null);
const markers = ref(new Map());
const overlays = ref(new Map());
const polyline = ref(null);


const props = defineProps({
    places: Array,
});

// 여행지들의 평균 위치를 계산하는 함수
const calculateCenter = (places) => {
    let totalLat = 0;
    let totalLng = 0;
    places.forEach((place) => {
        totalLat += place.latitude;
        totalLng += place.longitude;
    })
    return {
        lat: totalLat / places.length,
        lng: totalLng / places.length
    };
};

// 주소 -> 좌표 변환
const addressToLatLng = (address) => {
    const geocoder = new kakao.maps.services.Geocoder();
    const coords = ref(null)
    geocoder.addressSearch(address, function (result, status) {
        // 검색완료
        if (status === kakao.maps.services.Status.OK) {
            coords.value = new kakao.maps.LatLng(result[0].y, result[0].x);
        }
        else {
            console.error('좌표 변환 X')
        }
        return { coords }
    })
}

// 마커, 오버레이 초기화
const clearMarkerAndOverlays = () => {
    console.log('clearMarkerAndOverlays 호출')

    //마커 제거
    markers.value.forEach((marker, key) => {
        console.log(`마커 제거 전: ${key}`, marker);
        marker.setMap(null);
        console.log(`마커 제거 후: ${key}`, marker);
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
    };
}

// 마커, 오버레이, 폴리라인 표시
const displayMarkersAndPolyLine = () => {
    console.log('displayMarkersAndPolyLine 호출')

    if (!window.kakao || !window.kakao.maps) {
        console.error('카카오맵 api 호출 x');
        return;
    }

    clearMarkerAndOverlays();

    let path = [];

    props.places.forEach((place, index) => {
        const position = new kakao.maps.LatLng(place.latitude, place.longitude);

        // 마커 이미지 설정
        const imageSrc = ''
        const imageSize = new kakao.maps.Size(40, 40);
        const imageOption = { offset: new kakao.maps.Point(13, 35) }

        // 마커에 사용될 이미지 객체
        const markerImage = new kakao.maps.MarkerImage(
            imageSrc,
            imageSize,
            imageOption
        );

        // 마커 생성 설정
        const marker = new kakao.maps.Marker({
            map: map.value,
            position: position,
            image: markerImage,
            zIndex: index,
        });

        // 마커 순서 표시를 위한 오버레이
        const markerLabelContent = `<div class="marker-number">${index + 1}</div>`;

        const markerLabel = new kakao.maps.CustomOverlay({
            content: markerLabelContent,
            map: map.value,
            position: marker.getPosition(),
            yAnchor: 1,
            zIndex: index,
        });

        marker.value.set(place.contentId, marker);
        overlays.value.set(place.contentId, markerLabel);
        map.value.setCenter(position);

        path.push(position)
    });

    polyline.value = new kakao.maps.Polyline({
        path: path,
        strokeWeight: 5,
        strokeColor: "#FFAE00",
        strokeOpacity: 1,
        strokeStyle: "solid",
    });
    polyline.value.setMap(map.value);
};

watch(() => {
    props.places, (newplaces) => {
        console.log('업데이트')
        displayMarkersAndPolyLine();
    },
    {deep: true}
})

// 지도 초기화
const initMap = () => {
    const container = document.getElementById('map');
    let center = new kakao.maps.LatLng(33.450701, 126.570667);

    // 새 center 구하기
    if (props.places && props.places.length > 0) {
        const avgCenter = calculateCenter(props.places);
        center = new kakao.maps.LatLng(avgCenter.lat, avgCenter.lng);
    }

    const options = {
        center: center,
        level: 9
    }

    map.value = new kakao.maps.Map(container, options);
    // 마커 표시

    displayMarkersAndPolyLine();
}

const loadKaKaoMapScript = () => {
    const script = document.createElement('script');
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY}&libraries=services,clusterer`;
    script.onload = () => kakao.maps.load(initMap);
    document.head.appendChild(script);
}

//import.meta.env.VITE_KAKAO_MAP_SERVICE_KEY
// 마커 표시 함수

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
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
}
.marker-number{
    color: #fff;
    background-color: blue;
    border-radius: 50%;
    font-size: 14px;
    display: inline-block;
}
</style>