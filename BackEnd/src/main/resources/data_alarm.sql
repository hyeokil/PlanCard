INSERT INTO `alarm`
(`created_at`, `from_member_id`, `to_member_id`, `updated_at`, `content`, `url`, `status`, `type`)
VALUES
    (NOW(), 2, 1, NOW(), '테스트 2트 님이 친구 요청을 보냈습니다.', NULL, 'UNREAD', 'FRIEND'),
    (NOW(), 3, 1, NOW(), '테스트 3트 님이 친구 요청을 보냈습니다.', NULL, 'UNREAD', 'FRIEND'),
    (NOW(), 4, 1, NOW(), '테스트 4트 님이 친구 요청을 보냈습니다.', NULL, 'UNREAD', 'FRIEND'),
    (NOW(), 5, 1, '2024-02-13 11:10:59.001788', '신동근 님이 여행가자 여행 계획 참여 요청을 보냈습니다.', 'https://i10c110.p.ssafy.io/meeting/view/2', 'UNREAD', 'PLAN'),
    (NOW(), 3, 1, '2024-02-13 11:10:59.005787', '테스트3 님이 여행가자 여행 계획 참여 요청을 보냈습니다.', 'https://i10c110.p.ssafy.io/meeting/view/3', 'UNREAD', 'PLAN');