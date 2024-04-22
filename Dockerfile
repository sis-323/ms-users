FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

EXPOSE 9100

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

ENV DB_URL "jdbc:postgresql://localhost:5431/sis-323"
ENV DB_USER "postgres"
ENV DB_PASSWORD "admin"
ENV ISSUER_URI "http://localhost:8080/realms/sis-323"

ENTRYPOINT ["java","-cp","app:app/lib/*","com.users.msusers.MsUsersApplicationKt"]