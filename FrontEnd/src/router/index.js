import { createRouter, createWebHistory } from "vue-router";
import AppLayout from "@/layout/AppLayout.vue";
//  createWebHistory,
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),  // 변경된 부분
  routes: [  // routes : 라우터 경로와 해당 컴포넌트를 정의
    {
      path: "",
      component: AppLayout,
      redirect: { name: "main" },
      children: [  // children : 하위 경로
        {
          path: "/",  // 메인 페이지
          name: "main",
          component: () => import("@/views/TheMainView.vue"),
          // children: [
          //   {
          //     path: "plan",  // 메인 페이지에 나오는 지도
          //     name: "main-plan",
          //     component: () => import("@/components/plan/MyPlans.vue"),
          //   },
          // ],
        },
        {
          path: "member",
          name: "member",
          // component: () => import("@/views/TheMemberView.vue"),
          children: [
            {
              path: "login",
              name: "member-login",
              component: () => import("@/components/member/MemberLogin.vue"),
            },
            {
              path: "loading/:oAuthDomain",
              name: "member-loading",
              component: () => import("@/components/member/MemberLoading.vue"),
            },
            {
              path: "signup",
              name: "member-signup",
              component: () => import("@/components/member/MemberSignUp.vue"),
            },
            {
              path: "mypage",
              name: "member-mypage",
              component: () => import("@/components/member/MemberMyPage.vue"),
              redirect: { name: "mypage-mypagemain" },
              children: [
                {
                  path: "mypagemain",
                  name: "mypage-mypagemain",
                  component: () => import("@/components/member/mypage/MyPageMain.vue"),
                },
                {
                  path: "myplan",
                  name: "mypage-myplan",
                  component: () => import("@/components/member/mypage/MyPagePlan.vue"),
                },
                {
                  path: "myPasswordResetting",
                  name: "mypage-myPasswordResetting",
                  component: () => import("@/components/member/mypage/MyPasswordResetting.vue"),
                },
                {
                  path: "myInfoModify",
                  name: "mypage-myInfoModify",
                  component: () => import("@/components/member/mypage/MyInfoModify.vue"),
                },
              ],
            },
          ],
        },
        {
          path: "meeting",
          name: "meeting",
          component: () => import("@/views/TheMeetingView.vue"),
          redirect: { name: "meeting-create" },
          children: [
            {
              path: "create",
              name: "meeting-create",
              component: () => import("@/components/meeting/MeetingCreate.vue"),
            },
            {
              path: "view/:id",
              name: "meeting-detail",
              component: () => import("@/components/meeting/MeetingDetail.vue"),
            },
          ],
        }     
      ],
    },
  ],
});

export default router;
