#!/bin/bash

# Vue.js 애플리케이션 실행
npm run preview -- --host 0.0.0.0 &

# WebSocket 서버 실행
HOST="localhost" PORT=1234 npx y-websocket
