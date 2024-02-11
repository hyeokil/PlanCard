<template>
    <div id="main-container" class="card p-fluid">
  
  
      <!-- session이 false일때! 즉, 방에 들어가지 않았을때 -->
      <div id="join" v-if="!session">
        <div id="join-dialog">
          <div>
            <p>
              <label>이름 : </label>
              <input v-model="myUserName" type="text" required />
            </p>
            <p>
              <label>세션 이름 : </label>
              <input v-model="sessionId" type="text" required />
            </p>
            <p>
              <button @click="joinSession">
                입장!
              </button>
            </p>
          </div>
        </div>
      </div>
  
  
      <!-- session이 true일 때! 즉, 입장했을 때 -->
      <div id="session" v-if="session">
  
        <div id="session-header">
          <h1 id="session-title">{{ sessionId }}</h1>
          <input type="button" id="buttonLeaveSession" @click="leaveSession" value="Leave session" />
        </div>
  
        <!-- 내 캠 -->
        <div id="main-video">
          <UserVideo :stream-manager="mainStreamManagerComputed" />
        </div>
  
        <!-- 모든 캠 -->
        <div id="video-container">
          <!-- <UserVideo :stream-manager="publisherComputed" @click.native="updateMainVideoStreamManager(publisher)" /> -->
  
          <UserVideo v-for="sub in subscribersComputed" :key="sub.stream.connection.connectionId" :stream-manager="sub"
            @click.native="updateMainVideoStreamManager(sub)" />
        </div>
  
        <!-- 채팅창 -->
        <div id="chat-container" style="border: 3px solid black;">
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
        </div>
  
        <!-- 캠활성화, 음소거 버튼 -->
        <button id="camera-activate" @click="handleCameraBtn" style="border: 3px black solid;">캠 비활성화</button>
        <button id="mute-activate" @click="handleMuteBtn" style="border: 3px black solid;">마이크 활성화</button>
  
        <!-- 캠,오디오 선택 옵션 -->
        <div>
          <select name="cameras" @change="handleCameraChange" style="border: 3px black solid;">
            <option disabled>사용할 카메라를 선택하세요</option>
          </select>
          <select name="audios" @change="handleAudioChange" style="border: 3px black solid;">
            <option disabled>사용할 마이크를 선택하세요</option>
          </select>
        </div>
  
      </div>
  
  
    </div>
  </template>    
  
  
  
  <script setup>
  import { ref, computed } from 'vue'
  import axios from 'axios'
  import { OpenVidu } from "openvidu-browser";
  import UserVideo from "@/components/meeting/items/UserVideo.vue";
  import { createSessionApi, connectionSessionApi } from "@/api/webrtcApi";
  
  // OpenVidu objects
  const OV = ref(undefined)
  const session = ref(undefined)
  let mainStreamManager = ref(undefined)
  const publisher = ref(undefined)
  const subscribers = ref([])
  
  // Join form
  const myUserName = ref("Participant" + Math.floor(Math.random() * 100))
  
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
  
  const sessionId = ref('');
  const connectionToken = ref('');
  
  
  async function joinSession() {
    // --- 1) Get an OpenVidu object ---
    OV.value = new OpenVidu()
  
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
    session.value.on('signal:chat', (event) => { // event.from.connectionId === session.value.connection.connectionId (수신자와 발신자가 같으면?)
      const messageData = JSON.parse(event.data);
      if (event.from.connectionId === session.value.connection.connectionId) {
        messageData['username'] = '나'
      }
      messages.value.push(messageData);
    });
  
  
    try {
      // 세션 생성
      await createSession();
      // 연결 토큰 생성
      await connectionSession();
  
      // WebSocket 연결 시도
      if (connectionToken.value) {
        session.value.connect(connectionToken.value, { clientData: myUserName.value })
          .then(() => {
            let publisher = OV.value.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "640x480", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: false, // Whether to mirror your local video or not
            });
  
            // Set the main video in the page to display our webcam and store our Publisher
            this.mainStreamManager = publisher;
            this.publisher = publisher;
  
            // --- 6) Publish your stream ---
  
            this.session.publish(this.publisher);
          })
          .catch((error) => {
            console.error("There was an error connecting to the session:", error);
          });
      }
    } catch (error) {
      console.error('세션 생성 또는 연결 중 오류 발생:', error);
    }
  
  
    window.addEventListener("beforeunload", leaveSession)
  }
  
  async function createSession() {
    try {
      const response = await createSessionApi();
      if (response.data.dataHeader.successCode === 0) {
        sessionId.value = response.data.dataBody.sessionId;
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
  
  async function connectionSession() {
    try {
      const response = await connectionSessionApi(sessionId.value);
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
      cameraActivate.innerText = '캠 활성화'
    } else {                //카메라 활성화상태
      cameraActivate.innerText = '캠 비활성화'
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
      muteActivate.innerText = '마이크 비활성화'
    } else {                //마이크 비활성화상태
      muteActivate.innerText = '마이크 활성화'
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
  
  
  </script>