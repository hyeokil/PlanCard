package com.ssafy.backend.domain.conference.controller;

import com.ssafy.backend.domain.conference.dto.ConnectionTokenDto;
import com.ssafy.backend.domain.conference.dto.SessionDto;
import com.ssafy.backend.global.common.dto.Message;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class ConferenceController {
    @Value("${openvidu.url}")
    private String OPENVIDU_URL;

    @Value("${openvidu.secret}")
    private String OPENVIDU_SECRET;

    private OpenVidu openvidu;

    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    /**
     * 새로운 OpenVidu 세션을 생성하고 세션 ID를 반환합니다.
     *
     * @param params 세션 속성을 담고 있는 Map 객체. (선택적)
     * @return 생성된 세션의 정보를 담고 있는 SessionDto 객체.
     * @throws OpenViduJavaClientException OpenVidu Java 클라이언트 관련 예외
     * @throws OpenViduHttpException OpenVidu HTTP 요청 관련 예외
     */
    @PostMapping("/api/v1/sessions")
    public ResponseEntity<Message<SessionDto>> initializeSession(@RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {

        SessionProperties properties = SessionProperties.fromJson(params).build();
        Session session = openvidu.createSession(properties);
        SessionDto sessionDto = new SessionDto(session.getSessionId());
        return ResponseEntity.ok().body(Message.success(sessionDto));
    }

    /**
     * 지정된 세션에 새로운 연결을 생성하고, 연결에 할당된 토큰을 반환합니다.
     *
     * @param sessionId 연결을 생성할 세션의 ID
     * @param params 연결 속성을 담고 있는 Map 객체. (선택적)
     * @return 생성된 연결의 토큰 정보를 담고 있는 ConnectionTokenDto 객체.
     * @throws OpenViduJavaClientException OpenVidu Java 클라이언트 관련 예외
     * @throws OpenViduHttpException OpenVidu HTTP 요청 관련 예외
     */
    @PostMapping("/api/v1/sessions/{sessionId}/connections")
    public ResponseEntity<Message<ConnectionTokenDto>> createConnection(@PathVariable("sessionId") String sessionId,
                                                   @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.fail(null, "해당 세션이 존재하지 않습니다."));
        }

        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
        Connection connection = session.createConnection(properties);
        ConnectionTokenDto connectionTokenDto = new ConnectionTokenDto(connection.getToken());
        return ResponseEntity.ok().body(Message.success(connectionTokenDto));
    }
}
