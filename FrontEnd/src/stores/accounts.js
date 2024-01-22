import axios from 'axios'
import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { useRouter } from 'vue-router'

export const useAccountsStore = defineStore('accounts', () => {
  const router = useRouter()
  const API_URL = "/api/v1"

  // 변수
  const token = ref(null)
  const userName = ref("")
  const userId = ref("")
  const password = ref("")


  // 회원가입
  const signUp = function (info) {
    const {userName, userId, password1, password2} = info
    axios({
      method: "post",
      url: `${API_URL}/users`,
      data: {
        userName,
        userId, 
        password1,
        password2,
      }
    })
      .then((response) => {
        console.log(response.data)
        console.log(response.data.key)
        token.value = response.data.key
        router.push({name: "home"})

      })
      .catch((error) => {
        alert("회원가입 실패")
        console.log(error)
      })
  }

  // 로그인 유무(true/false)
  const isLogin = computed(() => {
    if (token.value === null) {
      return false
    } else {
      return true
    }
  })

  // 로그인
  const logIn = function (info) {
    const {userName, userId, password} = info
    axios({
      method: "post",
      url: `${API_URL}/auth/login`,
      data: {
        userName,
        userId,
        password
      }
    })
      .then((response) => {
        console.log(response.data)
        console.log(response.data.key)
        token.value = response.data.key
      })
      .then((res) => {
        console.log(res)
        router.push({name: "home"})
      })
      .catch((err) => {
        console.log(err)
      })
  }

  // 로그아웃
  const logOut = function () {
    axios({
      method: "post",
      url: `${API_URL}`,
    })
      .then((res) => {
        token.value = null
        console.log(res)
        router.push({ name: "home" })
      })
      .catch((err) => {
        console.log(err)
      })
  }





    return {API_URL,
            token, 
            userName, 
            userId, 
            password, 
            isLogin,
            signUp, 
            logIn,
            logOut,

          }
}, { persist: true })