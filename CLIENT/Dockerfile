# React + TypeScript 클라이언트를 위한 Dockerfile
FROM node:22-alpine

WORKDIR /app

COPY package.json yarn.lock* ./

RUN yarn install --frozen-lockfile

COPY . .

EXPOSE 5000

# Vite 개발 서버 실행
CMD ["yarn", "dev"]

