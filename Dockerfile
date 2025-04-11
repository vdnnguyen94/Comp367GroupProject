FROM openjdk:23

# Accept all build arguments
# ARG STUDENT1_NAME
# ARG STUDENT1_ID
# ARG STUDENT2_NAME
# ARG STAGE
# ARG APP_PORT

# Inject them as environment variables
# ENV STUDENT1_NAME=$STUDENT1_NAME
# ENV STUDENT1_ID=$STUDENT1_ID
# ENV STUDENT2_NAME=$STUDENT2_NAME
# ENV STAGE=$STAGE
# ENV APP_PORT=$APP_PORT
COPY .env /.env

COPY target/Maven-Web-App-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "/app.jar"]
