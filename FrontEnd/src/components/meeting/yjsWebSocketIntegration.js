// src/yjsWebSocketIntegration.js
import * as Y from 'yjs';
import { WebsocketProvider } from 'y-websocket';

export class YjsWebSocketIntegration {
  constructor(roomId, wsUrl) {
    this.doc = new Y.Doc();
    this.wsProvider = new WebsocketProvider(wsUrl, roomId, this.doc);

    this.wsProvider.on('status', (event) => {
      console.log(event.status); // 연결 상태 확인
    });
  }

  // Yjs 문서 변경 사항 감지 및 처리
  observeDocument(callback) {
    this.doc.on('update', (update) => {
      // 서버로 변경 사항 전송 또는 로컬에서 처리
      callback(update);
    });
  }

  // 서버에서 받은 변경 사항을 Yjs 문서에 적용
  applyUpdate(update) {
    Y.applyUpdate(this.doc, update);
  }

  // 리소스 정리
  destroy() {
    this.wsProvider.destroy();
    this.doc.destroy();
  }
}
