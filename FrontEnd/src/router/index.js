import { createRouter, createWebHashHistory } from "vue-router";
import AppLayout from "@/layout/AppLayout.vue";
//  createWebHistory,
const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: "/",
      component: AppLayout,
      children: [
        {
          path: "/",
          name: "main",
          component: () => import("@/views/TheMainView.vue"),
          redirect: { name: "main-plan" },
          children: [
            {
              path: "plan",
              name: "main-plan",
              component: () => import("@/components/plan/MyPlans.vue"),
            },
          ],
        },
        {
          path: "/member",
          name: "member",
          component: () => import("@/views/TheMemberView.vue"),
          children: [
            {
              path: "login",
              name: "member-login",
              component: () => import("@/components/member/MemberLogin.vue"),
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
              children: [
                {
                  path: "myplan",
                  name: "mypage-myplan",
                  component: () => import("@/components/member/mypage/MyPagePlan.vue"),
                },
                {
                  path: "myalarm",
                  name: "mypage-alarm",
                  component: () => import("@/components/member/mypage/MyPageAlarm.vue"),
                },
                {
                  path: "myfriend",
                  name: "mypage-myfriend",
                  component: () => import("@/components/member/mypage/MyPageFriend.vue"),
                },
              ],
            },
          ],
        },
        {
          path: "/meeting",
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
        },
        // (추가기능) 경로 추천
        {
          path: "/path",
          name: "path",
          component: () => import("@/views/ThePathView.vue"),
          redirect: { name: "path-main" },
          children: [
            {
              path: "list",
              name: "path-list",
              component: () => import("@/components/path/PathList.vue"),
            },
            {
              path: "detail/:pathid",
              name: "path-detail",
              component: () => import("@/components/path/PathDetail.vue"),
            },
          ],
        },
      ],
    },
  ],
});

export default router;

// const router = creatdeRouter({
//   history: createWebHistory(import.meta.env.BASE_URL),
//   routes: [
//     {
//       path: "/",
//       name: "main",
//       component: () => import("@/views/TheMainView.vue"),
//     },
//     {
//       path: "/member",
//       name: "member",
//       component: () => import("@/views/TheMemberView.vue"),
//       children: [
//         {
//           path: "login",
//           name: "member-login",
//           component: () => import("@/components/member/MemberLogin.vue"),
//         },
//         {
//           path: "signup",
//           name: "member-signup",
//           component: () => import("@/components/member/MemberSignUp.vue"),
//         },
//         {
//           path: "mypage",
//           name: "member-mypage",
//           component: () => import("@/components/member/MemberMyPage.vue"),
//           children: [
//             {
//               path: "myplan",
//               name: "mypage-myplan",
//               component: () => import("@/components/member/mypage/MyPagePlan.vue"),
//             },
//             {
//               path: "myalarm",
//               name: "mypage-alarm",
//               component: () => import("@/components/member/mypage/MyPageAlarm.vue"),
//             },
//             {
//               path: "myfriend",
//               name: "mypage-myfriend",
//               component: () => import("@/components/member/mypage/MyPageFriend.vue"),
//             },
//           ],
//         },
//       ],
//     },
//     {
//       path: "/meeting",
//       name: "meeting",
//       component: () => import("@/views/TheMeetingView.vue"),
//       redirect: { name: "meeting-create" },
//       children: [
//         {
//           path: "create",
//           name: "meeting-create",
//           component: () => import("@/components/meeting/MeetingCreate.vue"),
//         },
//         {
//           path: "view/:id",
//           name: "meeting-detail",
//           component: () => import("@/components/meeting/MeetingDetail.vue"),
//         },
//       ],
//     },
//     // (추가기능) 경로 추천
//     {
//       path: "/path",
//       name: "path",
//       component: () => import("@/views/ThePathView.vue"),
//       redirect: { name: "path-main" },
//       children: [
//         {
//           path: "list",
//           name: "path-list",
//           component: () => import("@/components/path/PathList.vue"),
//         },
//         {
//           path: "detail/:pathid",
//           name: "path-detail",
//           component: () => import("@/components/path/PathDetail.vue"),
//         },
//       ],
//     },
//   ],
// });

// export default router;
