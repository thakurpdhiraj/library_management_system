FROM maven:3.6.3-adoptopenjdk-11 as build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src src
RUN mvn clean package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


FROM adoptopenjdk:11-jre-hotspot as run
ARG DEPENDENCY=/app/target/dependency
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /wait
RUN chmod +x /wait
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
CMD /wait && java -cp app:app/lib/* com.dhitha.lms.auth.AuthApplication
# ENTRYPOINT ["java","-cp","app:app/lib/*","com.dhitha.lms.user.UserApplication"]