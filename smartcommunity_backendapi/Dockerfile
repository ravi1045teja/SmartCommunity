FROM node:13-alpine
RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY . .

RUN npm install

COPY . .

EXPOSE 9500
CMD [ "node", "app.js" ]