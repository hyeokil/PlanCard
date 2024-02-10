INSERT INTO card (created_at, place_id, plan_id, updated_at, memo, position)
VALUES
    (NOW(), (SELECT id FROM place ORDER BY RAND() LIMIT 1), 1, NOW(), '메모 1', '위치 1'),
(NOW(), (SELECT id FROM place ORDER BY RAND() LIMIT 1), 2, NOW(), '메모 2', '위치 2'),
(NOW(), (SELECT id FROM place ORDER BY RAND() LIMIT 1), 1, NOW(), '메모 3', '위치 3'),
(NOW(), (SELECT id FROM place ORDER BY RAND() LIMIT 1), 2, NOW(), '메모 4', '위치 4'),
(NOW(), (SELECT id FROM place ORDER BY RAND() LIMIT 1), 1, NOW(), '메모 5', '위치 5'),
(NOW(), (SELECT id FROM place ORDER BY RAND() LIMIT 1), 2, NOW(), '메모 6', '위치 6'),
(NOW(), (SELECT id FROM place ORDER BY RAND() LIMIT 1), 1, NOW(), '메모 7', '위치 7'),
(NOW(), (SELECT id FROM place ORDER BY RAND() LIMIT 1), 2, NOW(), '메모 8', '위치 8');