<template>
  <div id="main-container" class="card p-fluid" style="border-radius: 10px 0px 0px 0px;">


    <!-- session이 false일때! 즉, 방에 들어가지 않았을때 -->
    <div id="join" v-if="!session">
      <div id="join-dialog">
        <div>
          <!-- <p>
            <label>이름 : </label>
            <input v-model="myUserName" type="text" required />
          </p>
          <p>
            <label>세션 이름 : </label>
            <input v-model="mySessionId" type="text" required />
          </p> -->
          <p>
            <button @click="joinSession" id="camOn">
              <span>cam&nbsp;</span>
              <span style="color: #3498DB;">ON</span>
            </button>
          </p>
        </div>
      </div>
    </div>


    <!-- session이 true일 때! 즉, 입장했을 때 -->
    <div id="session" v-if="session">

      <div id="session-header" style="display: flex;">
        <!-- <h1 id="session-title">{{ mySessionId }}</h1> -->
        <button id="buttonLeaveSession" @click="leaveSession">
          <span>cam&nbsp;</span>
          <span style="color: red;">OFF</span>
        </button>
        <!-- <input type="button" id="buttonLeaveSession" @click="leaveSession" value="cam OFF" /> -->

        <v-spacer></v-spacer>

        <div style="display: flex; align-items: center;">
          <!-- 캠활성화, 음소거 버튼 -->
          <button id="camera-activate" @click="handleCameraBtn"
            style="width: 20px; height: 20px; border-radius: 50%; border: 2px rgba(0, 0, 0, 0.7) solid; margin-right: 3px;">
            <img src="/녹화on.png" alt="녹화on" style="width: 100%; height: 100%; padding: 2px;">
          </button>
          <button id="mute-activate" @click="handleMuteBtn"
            style="width: 20px; height: 20px; border-radius: 50%; border: 2px rgba(0, 0, 0, 0.7) solid; margin-right: 3px;">
            <img src="/녹음on.png" alt="녹음on" style="width: 100%; height: 100%; padding: 2px;">
          </button>

          <!-- 캠,오디오 선택 옵션 -->
          <select name="cameras" @change="handleCameraChange"
            style="border: 2px rgba(0, 0, 0, 0.7) solid; width: 30px; font-size: 11px; margin-right: 3px; cursor: pointer;"
            class="font-content">
            <option disabled>Camera Select</option>
          </select>
          <select name="audios" @change="handleAudioChange"
            style="border: 2px rgba(0, 0, 0, 0.7) solid; width: 30px; font-size: 11px; margin-right: 3px; cursor: pointer"
            class="font-content">
            <option disabled>MIC Select</option>
          </select>
        </div>



        <!-- 내 캠 -->
        <!-- <div id="main-video">
          <UserVideo :stream-manager="mainStreamManagerComputed" />
      </div> -->
      </div>
      <hr>


      <!-- 모든 캠 -->
      <div :class="{'card': true, 'p-fluid': true, 'video-container': true, 'scrollable': subscribersComputed.length > 3 }">
        <UserVideo :stream-manager="publisherComputed" @click.native="updateMainVideoStreamManager(publisher)" />

        <UserVideo v-for="sub in subscribersComputed" :key="sub.stream.connection.connectionId" :stream-manager="sub"
          @click.native="updateMainVideoStreamManager(sub)" />
      </div>

      <!-- 채팅창 -->
      <!-- <div id="chat-container" style="border: 3px solid black;">
        <div id="chat-window">
          <ul id="chat-history">
            <li v-for="(message, index) in messages" :key="index">
              <strong>{{ message.username }}:</strong> {{ message.message }}
            </li>
          </ul>
        </div>
        <form id="chat-write">
          <input type="text" placeholder="전달할 내용을 입력하세요." v-model="inputMessage" style="border: 3px black solid;">
          <button @click="sendMessage" style="border: 3px black solid;">전송</button>
        </form>
      </div> -->


      <!-- <button class="btn sttBtn" @click="sttToggle()">
        {{ sttOn ? "stt OFF" : "stt ON" }}
      </button> -->

    </div>


  </div>
</template>    



<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router';
import { OpenVidu } from "openvidu-browser";
import UserVideo from "@/components/meeting/items/UserVideo.vue";
import { createSessionApi, connectionSessionApi } from "@/api/webrtcApi";
import { useAccountsStore } from '@/stores/accountsStore'; // accountsStore 가져오기
import RecordRTC from 'recordrtc';

// OpenVidu objects
const OV = ref(undefined)
const session = ref()
let mainStreamManager = ref(undefined)
const publisher = ref(undefined)
const subscribers = ref([])
const accountStore = useAccountsStore(); // accountsStore 사용

// Join form
const mySessionId = ref('');
const connectionToken = ref('');
// Vue Router를 이용하여 현재 라우트에 접근
const route = useRoute();

// 라우트 파라미터에서 세션 ID를 가져와서 mySessionId에 할당
mySessionId.value = route.params.id;
const myUserName = accountStore.memberInfo.nickname;

// 채팅창을 위한 변수
const inputMessage = ref("")
const messages = ref([])

// 카메라 및 오디오 설정을 위한 변수
const muted = ref(false)       // 기본은 음소거 비활성화
const camerOff = ref(false)    // 기본 카메라 활성화
const selectedCamera = ref("")  // 카메라 변경시 사용할 변수 
const selectedAudio = ref("")  // 오디오 변경시 사용할 변수

// 다시그려내기 위해 computed 작성
const mainStreamManagerComputed = computed(() => mainStreamManager.value);
const publisherComputed = computed(() => publisher.value);
const subscribersComputed = computed(() => subscribers.value);




async function joinSession() {
  // --- 1) Get an OpenVidu object ---
  OV.value = new OpenVidu();
  console.log(OV.value);
  // --- 2) Init a session ---
  session.value = OV.value.initSession()

  // --- 3) Specify the actions when events take place in the session ---
  // On every new Stream received...
  session.value.on("streamCreated", ({ stream }) => {
    // const subscriber = session.subscribe(stream)
    const subscriber = session.value.subscribe(stream)
    subscribers.value.push(subscriber)
  })

  // On every Stream destroyed...
  session.value.on("streamDestroyed", ({ stream }) => {
    const index = subscribers.value.indexOf(stream.streamManager, 0)
    if (index >= 0) {
      subscribers.value.splice(index, 1)
    }
  })

  // On every asynchronous exception...
  session.value.on("exception", ({ exception }) => {
    console.warn(exception);
  });

  // 채팅 이벤트 수신 처리
  // session.value.on('signal:chat', (event) => { // event.from.connectionId === session.value.connection.connectionId (수신자와 발신자가 같으면?)
  //   const messageData = JSON.parse(event.data);
  //   if (event.from.connectionId === session.value.connection.connectionId) {
  //     messageData['username'] = '나'
  //   }
  //   messages.value.push(messageData);
  // });

  // 세션 생성
  await createSession(mySessionId.value);
  // 세션 아이디를 통한 연결 토큰 생성
  await createToken(mySessionId.value);

  // WebSocket 연결 시도
  if (connectionToken.value) {
    await session.value.connect(connectionToken.value, { clientData: myUserName });
    // .then(() => {
    //   let publisher_tmp = OV.value.initPublisher(undefined, {
    //     audioSource: undefined, // The source of audio. If undefined default microphone
    //     videoSource: undefined, // The source of video. If undefined default webcam
    //     publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
    //     publishVideo: true, // Whether you want to start publishing with your video enabled or not
    //     resolution: "640x480", // The resolution of your video
    //     frameRate: 30, // The frame rate of your video
    //     insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
    //     mirror: false, // Whether to mirror your local video or not
    //   });

    //   // Set the main video in the page to display our webcam and store our Publisher
    //   mainStreamManager.value = publisher_tmp;
    //   publisher.value = publisher_tmp;

    //   // --- 6) Publish your stream ---

    //   session.value.publish(publisher.value);
    // })
    // .catch((error) => {
    //   console.error("There was an error connecting to the session:", error);
    // });

    let publisher_tmp = OV.value.initPublisher(undefined, {
      audioSource: undefined, // The source of audio. If undefined default microphone
      videoSource: undefined, // The source of video. If undefined default webcam
      publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
      publishVideo: true, // Whether you want to start publishing with your video enabled or not
      resolution: "640x480", // The resolution of your video
      frameRate: 30, // The frame rate of your video
      insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
      mirror: false, // Whether to mirror your local video or not
    });
    mainStreamManager.value = publisher_tmp;
    publisher.value = publisher_tmp;
    await session.value.publish(publisher.value);
  }




  await getMedia();

  window.addEventListener("beforeunload", leaveSession)
}

function leaveSession() {
  // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
  if (session.value) session.value.disconnect()

  // Empty all properties...
  session.value = undefined;
  mainStreamManager.value = undefined;
  publisher.value = undefined;
  subscribers.value = [];
  OV.value = undefined;

  // Remove beforeunload listener
  window.removeEventListener("beforeunload", leaveSession)

}

function updateMainVideoStreamManager(stream) {
  if (mainStreamManager.value === stream) return
  mainStreamManager.value = stream
}



// 세션 생성 메서드
async function createSession(sessionId) {
  try {
    const response = await createSessionApi(sessionId);
    if (response.data.dataHeader.successCode === 0) {
      mySessionId.value = response.data.dataBody.sessionId;
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

async function createToken(sessionId) {
  try {
    const response = await connectionSessionApi(sessionId);
    if (response.data.dataHeader.successCode === 0) {
      connectionToken.value = response.data.dataBody.connectionToken;
      console.log(connectionToken.value);
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

// 채팅창 구현을 위한 함수
function sendMessage(event) {
  event.preventDefault();
  if (inputMessage.value.trim()) {
    // 다른 참가원에게 메시지 전송하기
    session.value.signal({
      data: JSON.stringify({ username: myUserName.value, message: inputMessage.value }), // 메시지 데이터를 문자열로 변환해서 전송
      type: 'chat' // 신호 타입을 'chat'으로 설정
    });
    inputMessage.value = '';
  }
}


// 캠, 오디오 등 기기와 관련된 함수
// 카메라와 오디오를 가져옴.
async function getMedia() {
  try {
    const devices = await navigator.mediaDevices.enumerateDevices();
    const cameras = devices.filter((device) => device.kind === 'videoinput');
    const audios = devices.filter((device) => device.kind === 'audioinput');
    // const audios = undefined

    const cameraSelect = document.querySelector('select[name="cameras"]');
    const audioSelect = document.querySelector('select[name="audios"]');

    // 카메라 및 오디오 선택기 요소가 존재하는지 확인
    if (cameras) {
      cameras.forEach((camera) => {
        const option = document.createElement('option');
        option.value = camera.deviceId;
        option.text = camera.label;
        cameraSelect.appendChild(option);
      });
    } else {
      const notCamera = cameraSelect.querySelector('option:disabled');
      notCamera.innerText = '사용 가능한 카메라가 없습니다.'
    }
    if (audios) {
      audios.forEach((audio) => {
        const option = document.createElement('option');
        option.value = audio.deviceId;
        option.text = audio.label;
        audioSelect.appendChild(option);
      });
    } else {
      const notAudio = audioSelect.querySelector('option:disabled');
      notAudio.innerText = '사용 가능한 마이크가 없습니다.'
    }
  } catch (error) {
    console.error('Error getting media devices:', error);
  }
}


// 카메라 조작
function handleCameraBtn() {
  if (!publisher.value) return;
  // 카메라 상태 토글
  camerOff.value = !camerOff.value;
  const cameraActivate = document.getElementById('camera-activate')
  if (camerOff.value) {   //카메라 비활성화상태
    cameraActivate.innerHTML = '<img src="/녹화off.png" alt="녹화off" style="width: 100%; height: 100%; padding: 2px;">'
  } else {                //카메라 활성화상태
    cameraActivate.innerHTML = '<img src="/녹화on.png" alt="녹화on" style="width: 100%; height: 100%; padding: 2px;">'
  }

  // 카메라 작동 상태를 적용
  publisher.value.publishVideo(!camerOff.value);
}

// 마이크 조작
function handleMuteBtn() {
  if (!publisher.value) return;

  // 음소거 상태 토글
  muted.value = !muted.value;
  const muteActivate = document.getElementById('mute-activate')
  if (muted.value) {   //마이크 활성화상태
    muteActivate.innerHTML = '<img src="/녹음off.png" alt="녹음off" style="width: 100%; height: 100%; padding: 2px;">'
  } else {                //마이크 비활성화상태
    muteActivate.innerHTML = '<img src="/녹음on.png" alt="녹음on" style="width: 100%; height: 100%; padding: 2px;">'
  }
  // 음소거 설정을 적용
  publisher.value.publishAudio(!muted.value);
}

// select태그에서 사용할 기기를 선택했을때
// 캠
async function handleCameraChange(event) {
  selectedCamera.value = event.target.value;
  await replaceCameraTrack(selectedCamera.value);
}
// 마이크
async function handleAudioChange(event) {
  selectedAudio.value = event.target.value;
  await replaceAudioTrack(selectedAudio.value);
}

async function replaceCameraTrack(deviceId) {
  if (!publisher.value) return;

  const newConstraints = {
    audio: false,
    video: {
      deviceId: { exact: deviceId },
    },
  };

  try {
    const newStream = await navigator.mediaDevices.getUserMedia(newConstraints);
    const newVideoTrack = newStream.getVideoTracks()[0];
    await publisher.value.replaceTrack(newVideoTrack);
  } catch (error) {
    console.error("Error replacing video track:", error);
  }
}

async function replaceAudioTrack(deviceId) {
  if (!publisher.value) return;

  const newConstraints = {
    audio: {
      deviceId: { exact: deviceId },
    },
    video: false,
  };

  try {
    const newStream = await navigator.mediaDevices.getUserMedia(newConstraints);
    const newAudioTrack = newStream.getAudioTracks()[0];
    await publisher.value.replaceTrack(newAudioTrack);
  } catch (error) {
    console.error("Error replacing audio track:", error);
  }
}


const webSocket = ref(null);
const mediaRecorder = ref(null); // MediaRecorder 인스턴스를 관리할 ref
const recorder = ref(null);


// OpenVidu Publisher 객체에서 오디오 스트림을 얻습니다.
async function getAudioStreamFromPublisher(publisher) {
  // publisher.stream이 유효한지 확인
  if (!publisher || !publisher.stream || typeof publisher.stream.getMediaStream !== 'function') {
    throw new Error('Invalid publisher or stream');
  }

  const mediaStream = publisher.stream.mediaStream;
  // console.log("mediaStream 확인: ", mediaStream);
  const audioTracks = mediaStream.getAudioTracks();
  console.log("audioTracks 확인하기: ", audioTracks);

  // 오디오 트랙이 존재하는지 확인
  if (audioTracks.length === 0) {
    throw new Error('No audio tracks found');
  }

  return new MediaStream([audioTracks[0]]);
}

// 웹소켓 시작 메서드
function startAudioWebSocket() {
  const serverURL = `${import.meta.env.VITE_VUE_AUDIO_WS_URL}`;
  webSocket.value = new WebSocket(serverURL);

  webSocket.value.onopen = function (event) {
    console.log("Audio WebSocket is open now.", event);
  };

  webSocket.value.onmessage = function (event) {
    console.log("Received message from server", event.data);
  };

  webSocket.value.onclose = function (event) {
    console.log("Audio WebSocket is closed now.", event);
  };

  webSocket.value.onerror = function (event) {
    console.error("Audio WebSocket error observed:", event);
  };
}

// 웹소켓 종료 메서드
function closeAudioWebSocket() {
  if (webSocket.value) {
    webSocket.value.close();
    webSocket.value = null;
  }
}



// 오디오 스트림을 캡쳐하고 AudioWebSocket을 통해 서버로 전송합니다.
// async function captureAndSendAudio(publisher) {
//   try {
//     const audioStream = await getAudioStreamFromPublisher(publisher);
//     // 오디오 스트림 가져오기
//     // const audioStream = await navigator.mediaDevices.getUserMedia({ audio: true });
//     const options = {
//       audioBitsPerSecond: 128000,
//       mimeType: 'audio/webm'
//     };

//     mediaRecorder.value = new MediaRecorder(audioStream, options);
//     console.log("MediaRecorder 객체 생성됨:", mediaRecorder.value);

//     mediaRecorder.value.ondataavailable = async (event) => {
//       if (event.data.size > 0) {
//         // webm 오디오를 wav로 변환
//         const arrayBuffer = await convertWebmToWav(event.data);
//         // WebSocket이 열려 있고, 준비 상태인 경우 변환된 오디오 데이터 전송
//         if (event.data.size > 0 && webSocket.value && webSocket.value.readyState === WebSocket.OPEN) {
//           webSocket.value.send(arrayBuffer); // WebSocket을 통해 바이너리 데이터 전송
//           console.log("서버로 오디오 데이터 전송됨: ", arrayBuffer);
//         }
//       }
//     };

//     // 5초 간격으로 데이터 캡쳐 시작
//     mediaRecorder.value.start(5000);
//     console.log("MediaRecorder 캡쳐 시작됨");

//   } catch (error) {
//     console.error('Error capturing audio:', error);
//   }
// }

// 실시간 오디오 캡쳐 시작
function startAudioCapture() {
  // 오디오 캡처 설정
  navigator.mediaDevices.getUserMedia({ audio: true }).then(stream => {
    recorder.value = new RecordRTC(stream, {
      type: 'audio',
      mimeType: 'audio/webm;codecs=opus', 
      timeSlice: 3000, // 4초마다 데이터를 전송
      ondataavailable: blob => {
        if (webSocket.value && webSocket.value.readyState === WebSocket.OPEN) {
          console.log("음성 데이터 확인: ", blob);
          webSocket.value.send(blob); // Blob 데이터를 웹소켓을 통해 전송
        }
      }
    });

    recorder.value.startRecording();
  }).catch(error => console.error("오디오 캡처 시작 실패:", error));
}

// 웹소켓 연결 종료 및 오디오 캡처 중지
function stopAudioCapture() {
  if (recorder.value) {
    recorder.value.stopRecording(() => {
      let blob = recorder.value.getBlob();
      if (webSocket.value && webSocket.value.readyState === WebSocket.OPEN) {
        webSocket.value.send(blob);
      }
      recorder.value.destroy();
      recorder.value = null;
    });
  }

  if (webSocket.value) {
    webSocket.value.close();
    webSocket.value = null;
  }
}



const sttOn = ref(false);

// SpeechRecognition 인스턴스를 관리할 ref 추가
const speechRecognition = ref(null);

const sttToggle = async () => {
  sttOn.value = !sttOn.value;

  if (sttOn.value) {
    // STT 활성화 시
    // SpeechRecognition 인스턴스 생성
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (SpeechRecognition) {
      speechRecognition.value = new SpeechRecognition();
      speechRecognition.value.continuous = true; // 연속적인 입력 처리
      speechRecognition.value.lang = 'ko-KR'; // 언어 설정 (한국어)
      speechRecognition.value.interimResults = true; // 중간 결과 반환

      // 음성 인식 시작
      speechRecognition.value.start();

      // 음성 인식 결과 이벤트 처리
      speechRecognition.value.onresult = function(event) {
        const current = event.resultIndex;
        const transcript = event.results[current][0].transcript;  // 변환된 텍스트
        const text = transcript.split(" "); // 변환된 텍스트를 공백단위로 끊어서 배열로 저장
        const lastWord = text[text.length - 1]; // 배열의 맨 마지막 요소(단어) 추출
  
        console.log(lastWord); // 맨 마지막 단어 
      };

      speechRecognition.value.onerror = function(event) {
        console.error('SpeechRecognition error', event.error);
      };

      console.log("STT 시작");
    } else {
      console.error("이 브라우저는 SpeechRecognition을 지원하지 않습니다.");
    }
  } else {
    // STT 비활성화 시
    if (speechRecognition.value) {
      speechRecognition.value.stop(); // 음성 인식 중지
      speechRecognition.value = null;
      console.log("STT 중지");
    }
  }
};

function processNewWords(currentTranscript) {
  // 새로운 전체 텍스트(currentTranscript)와 이전 텍스트(lastTranscript)를 공백으로 분리
  const lastWords = lastTranscript.value.split(' ');
  const currentWords = currentTranscript.split(' ');

  // 새로운 단어만 필터링
  const newWords = currentWords.filter(word => !lastWords.includes(word));

  // 새로운 단어들이 있으면 콘솔에 출력
  newWords.forEach(word => console.log(word));

  // 이전 텍스트 업데이트
  lastTranscript.value = currentTranscript;
}



onMounted(() => {
  // startAudioWebSocket();
});

onUnmounted(() => {
  // stopAudioCapture();
})
</script>

<style scoped>
#main-container {
  margin: 0;
}

.video-container {
  width: 100%;
  height: auto;
  position: relative;

  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  padding: 5px;
  padding-bottom: 0;

  margin-bottom: 0;
}
.video-container.scrollable {
  overflow-x: hidden;
  overflow-y: auto;
  max-height: 250px;
  }

.video {
  width: 100%;
  /* 비디오 너비를 컨테이너의 100%로 설정 */
  height: auto;
  /* 높이를 자동으로 조정 */
  object-fit: cover;
  /* 비디오를 컨테이너에 맞추어 확장 */
}



#camOn {
  transition: transform 0.3s ease;
}

#camOn:hover {
  transform: scale(1.05);
}

#buttonLeaveSession {
  transition: transform 0.3s ease;
}

#buttonLeaveSession:hover {
  transform: scale(1.05);
}

.sttBtn {
  top: 5px;
  right: 170px;

  width: 70px;
  height: 30px;

  border: rgba(0, 0, 0, 0.1) 2.5px solid;
  border-radius: 5cm;
  background-color: #FFC0CB;
  color: #fff;

  text-align: center;
  padding-top: 3px;
  z-index: 10;

  transition: transform 0.3s ease;

}
</style>