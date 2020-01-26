FROM openjdk:8
VOLUME /tmp
EXPOSE 8002
ADD ./target/microservicios.mongodb.banco.current_account-0.0.1-SNAPSHOT.jar cuentasCorrientes.jar
ENTRYPOINT ["java","-jar","/cuentasCorrientes.jar"]