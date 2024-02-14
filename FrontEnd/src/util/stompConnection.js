import Stomp from 'webstomp-client';

let stompClient = null;

export function connectStompClient(serverURL, token, onConnected, onError) {
    const socket = new WebSocket(serverURL);
    stompClient = Stomp.over(socket);

    const headers = {
        Authorization: `Bearer ${token}`,
    };

    stompClient.connect(headers, frame => {
        console.log('WebSocket Stomp 연결: ' + frame);
        onConnected(stompClient);
    }, onError);
}

export function getStompClient() {
    return stompClient;
}