
# 베이스 이미지 설정
FROM node:latest as build

# 작업 디렉토리 설정
WORKDIR /front

# 필요한 파일 복사
COPY package*.json ./

# 종속성 설치
RUN npm install

# 소스 코드 복사
COPY . .

# Vue.js 애플리케이션 빌드
RUN npm run build

# 기본 실행 명령 설정
CMD ["npm", "run", "preview","--","--host", "0.0.0.0"]

# 포트 노출
EXPOSE 4173