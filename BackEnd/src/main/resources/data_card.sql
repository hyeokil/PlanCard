INSERT INTO card (
                   created_at
                 ,place_id
                 ,plan_id
                 ,updated_at
                 ,memo
                 ,position
)
VALUES (
         NOW()
       ,(
             SELECT id
             FROM place
             ORDER BY RAND() LIMIT 1
)
     ,1
     ,NOW()
     ,'메모 1-1'
     ,'위치 1-1'
    )
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,1
	,NOW()
	,'메모 1-2'
	,'위치 1-2'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,1
	,NOW()
	,'메모 1-3'
	,'위치 1-3'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,1
	,NOW()
	,'메모 1-4'
	,'위치 2-1'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,2
	,NOW()
	,'메모 2-1'
	,'위치 2-2'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,2
	,NOW()
	,'메모 2-2'
	,'위치 2-3'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,2
	,NOW()
	,'메모 2-3'
	,'위치 2-4'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,2
	,NOW()
	,'메모 2-4'
	,'위치 2-5'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,2
	,NOW()
	,'메모 2-5'
	,'위치 2-6'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,3
	,NOW()
	,'메모 3'
	,'위치 3'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,4
	,NOW()
	,'메모 4'
	,'위치 4'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,5
	,NOW()
	,'메모 5'
	,'위치 5'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,6
	,NOW()
	,'메모 6'
	,'위치 6'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,7
	,NOW()
	,'메모 7'
	,'위치 7'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,8
	,NOW()
	,'메모 8'
	,'위치 8'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,9
	,NOW()
	,'메모 9'
	,'위치 9'
	)
	,(
	NOW()
	,(
		SELECT id
		FROM place
		ORDER BY RAND() LIMIT 1
		)
	,10
	,NOW()
	,'메모 10'
	,'위치 10'
	);