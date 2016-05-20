FROM java:alpine

ADD intake.war /intake.war
ADD run.sh /run.sh
ADD intake.git.txt /intake.git.txt
RUN sh -c 'touch /intake.war' \
&&  apk add --update tzdata \
&&  chmod +x /run.sh

EXPOSE 8080

CMD ["/run.sh"]
