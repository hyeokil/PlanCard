<template>
  <div class="card p-fluid" id="box">
    <div class="container; card p-fluid" id="signUpBox">
      <h1 id="signUpTitle">회원가입</h1>
      <form @submit.prevent="signUp" id="signUpForm">
        <div v-if="memberPreviewPhotoUrl" id="profileImg">
          <img :src="memberPreviewPhotoUrl" alt="프로필 이미지">
        </div>
        <div v-else></div>
        <div class="box, card p-fluid" id="memberEmailInput">
            <input type="text" id="memberEmail" v-model.trim="memberEmail" placeholder="이메일">
        </div>
        <div class="box, card p-fluid" id="memberNameInput">
            <input type="text" id="memberName" v-model.trim="memberName" placeholder="이름">
        </div>
        <div class="box, card p-fluid" id="memberNicknameInput">
            <input type="text" id="memberNickname" v-model.trim="memberNickname" placeholder="닉네임">
        </div>
        <div class="box, card p-fluid" id="memberPasswordInput">
            <input type="password" id="memberPassword" v-model.trim="memberPassword" placeholder="비밀번호">
        </div>
        <div class="box, card p-fluid" id="memberPasswordCheckInput">
            <input type="password" id="memberPasswordCheck" v-model.trim="memberPasswordCheck" placeholder="비밀번호 재입력">
        </div>
<!--  -->
        <div class="card p-fluid" id="profilePhoto">
        <p id="profilePhotoGuide">프로필 사진</p>
        <hr id="separator">
            <div class="box, card p-fluid" id="memberPhotoInput">
                <input type="file" id="memberPhoto" accept="image/*" @change="handlePhotoUpload" ref="fileInput">
            </div>
      </div>
<!--  -->

        <div class="box, card p-fluid"  id="signUpSubmit" style=" text-align: center;">
            <input type="submit" value="가입하기">
        </div>
      </form>




    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { memberSignUpApi } from "@/api/memberApi";
import { fileUploadApi } from "@/api/commonApi";

const router = useRouter();

const memberEmail = ref('');
const memberName = ref('');
const memberNickname = ref('');
const memberPassword = ref('');
const memberPasswordCheck = ref('');
const memberImage = ref('');

const memberPreviewPhotoUrl = ref('');
const fileInput = ref('');

// 이메일 유효성 검사
const isValidEmail = computed(() => {
  // 이메일 정규식
  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
  return emailRegex.test(memberEmail.value);
});

// 비밀번호 일치 검사
const doPasswordsMatch = computed(() => {
  return memberPassword.value === memberPasswordCheck.value;
});

// 회원가입 폼 유효성 검사
const isFormValid = computed(() => {
  return isValidEmail.value && doPasswordsMatch.value && memberNickname.value.trim().length > 0;
});


const signUp = async () => {
  if (!isFormValid.value) {
    alert("폼을 올바르게 작성해주세요.");
    return;
  }

  const signUpData = {
    email: memberEmail.value,
    password: memberPassword.value,
    name: memberName.value,
    nickname: memberNickname.value,
    image: memberImage.value
  };

  try {
    await memberSignUpApi(signUpData,
      (response) => {
        if (response.data.dataHeader.successCode === 0) {
          alert("회원가입에 성공했습니다.");
          router.push('/member/login');  // 로그인 페이지로 이동
        } else {
          alert(response.data.dataHeader.resultMessage);
        }
      });
  } catch (error) {
    console.error(error);
    alert("회원가입 중 오류가 발생했습니다.");
  }
};

// 파일 업로드를 위한 이벤트 핸들러
const handlePhotoUpload = (event) => {
  const file = event.target.files[0];
  const maxFileSize = 1 * 1024 * 1024; // 1MB

  if (!file.type.includes("jpeg") && !file.type.includes("png")) {
    alert("JPG 또는 PNG 이미지만 업로드 가능합니다.");
    resetFileInput();
    return;
  }

  if (file.size > maxFileSize) {
    alert("이미지 파일 크기는 1MB 이하만 가능합니다.");
    resetFileInput();
    return;
  }

  // memberPhoto.value = file;
  memberPreviewPhotoUrl.value = URL.createObjectURL(file);
  imageUpload(file);
}

// 입력 필드 리셋
const resetFileInput = () => {
  if (fileInput.value) {
    fileInput.value.value = ""; // Vue 3 Composition API에서 ref를 사용할 경우 .value 사용
  }
  memberPreviewPhotoUrl.value = "";
};

const imageUpload = async (file) => {
  // 파일을 FormData에 추가하여 전송 가능
  const formData = new FormData();
  formData.append('file', file);
  formData.append('nameFile', file.name);

  try {
    const response = await fileUploadApi(formData);
    if (response.data.dataHeader.successCode === 0) {
      console.log("이미지가 이미지 서버에 업로드 되었습니다.");
      memberImage.value = response.data.dataBody;
    }
    else {
      alert(response.data.dataHeader.resultMessage);
    }
  } catch (error) {
    console.error(error);
    alert("이미지 파일 업로드 과정에서 문제가 발생했습니다.");
  }
  
  
}
</script>



<style scoped>
  #box {
  display: flex;
  justify-content: center;
  }

  #signUpBox {
    background-color: #FFFFFF;
    border: 1px solid rgba(52, 152, 219, 0.5);
    width: 400px;
    height: 50%;
    padding: 40px;

    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
  }
  #signUpTitle {
    color: #3498db;
    /* font-weight: bold; */
  }
  #signUpForm {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  #profileImg {
    border-radius: 50%;
    border: 1px solid rgba(52, 152, 219, 0.5);
    background-color: #FFFFFF;
    width: 250px;
    height: 250px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative; /* 상대 위치 지정 */
    overflow: hidden; /* 원 밖으로 넘치는 부분 숨김 */
    margin-top: 10px;
    margin-bottom: 20px;
  }

  #profileImg img {
    max-width: none; /* 이미지 너비를 원본 크기로 설정 */
    max-height: none; /* 이미지 높이를 원본 크기로 설정 */
    width: 100%; /* 이미지 너비를 자동으로 조정 */
    height: auto; /* 이미지 높이를 자동으로 조정 */
    border-radius: 50%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%); /* 이미지를 수평 및 수직 가운데 정렬 */
  }
  #memberEmail, #memberName, #memberNickname, #memberPassword, #memberPasswordCheck, #memberPhoto {
    height: 35px;
    width: 250px;
  }
  #memberEmailInput, #memberNameInput, #memberNicknameInput, #memberPasswordInput, #memberPasswordCheckInput {
    background-color: rgba(245, 245, 245, 0.1);
    width: 90%;
    display: flex;
    align-items: center;
    border: 1px solid rgba(52, 152, 219, 0.5);
    height: 40px;
    margin-bottom: 10px;
  }
  #separator {
    margin-top: 3px;
    border: none;
    border-top:  1px solid rgba(52, 152, 219, 0.5);
    border-radius: 10px;
  }

  #profilePhoto {
    background-color: #FFFFFF;
    border: 1px solid rgba(52, 152, 219, 0.5);
    color: rgba(0, 0, 0, 0.5);
    /* font-weight: bold; */
    width: 100%;
    padding: 5px;
    margin-top: 20px;
  }
  #profilePhotoGuide {
    text-align: center;
    padding-top: 5px;
    margin: 0;
  }
  #userPhotoInput {
    border: 1px solid rgba(52, 152, 219, 0.5);
    display: flex;
    justify-content: center; /* 수평 가운데 정렬 */
    align-items: center;
    width: 90%; /* 현재 가로 너비 설정에 따라 조정 */
    margin: 0 auto; /* 가운데 정렬을 위한 외부 여백 설정 */
    margin-bottom: 10px;
  }
  #signUpSubmit {
    width: 90%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid rgba(0, 0, 0, 0.1);
    background-color: #3498db;
    color: #FFFFFF;
    /* font-weight: bold; */
    font-size: medium;
    height: 20px;
    padding: 20px;
    transition: transform 0.2s, border-color 0.2s; /* 부드러운 전환 효과를 위한 설정 */
    cursor: pointer; /* 마우스 포인터를 손가락 모양으로 변경 */
  }
  #signUpSubmit:hover {
    transform: scale(1.05); /* 이미지를 약간 확대 */
    border-color: #3498db; /* 테두리 색상 변경 */
  }

</style>