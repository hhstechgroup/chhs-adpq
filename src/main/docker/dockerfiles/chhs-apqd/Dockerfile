FROM java:alpine

ADD chhs-apqd.war /chhs-apqd.war
ADD run.sh /run.sh
ADD chhs-apqd.git.txt /chhs-apqd.git.txt
RUN sh -c 'touch /chhs-apqd.war' \
&&  apk add --update tzdata \
&&  chmod +x /run.sh

EXPOSE 8080

CMD ["/run.sh"]
