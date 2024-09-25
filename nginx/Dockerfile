FROM nginx:1.21.3-alpine

RUN rm /etc/nginx/conf.d/default.conf

COPY ./nignx.conf /etc/nginx/conf.d

CMD ["nginx", "-g", "daemon off;"]

EXPOSE 80