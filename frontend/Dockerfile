FROM nginx:latest
RUN rm -rf /usr/share/nginx/html/*
ADD ./dist /usr/share/nginx/html/
RUN rm -rf /etc/nginx/conf.d/*
ADD ./nginx/* /etc/nginx/conf.d/
<<<<<<< HEAD
CMD ["nginx","-g","daemon off;"]
=======
CMD ["nginx","-g","daemon off;"]
>>>>>>> 824d314e452b5a1c568737bbd941cc43ddbccdd3
