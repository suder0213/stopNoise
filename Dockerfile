# 사용할 기본 이미지 (Amazon Corretto 21 버전)
FROM amazoncorretto:21

# 환경 변수 설정
# JAR_FILE 변수에 빌드된 JAR 파일의 경로를 저장
ARG JAR_FILE=build/libs/StopNoise-0.0.1-SNAPSHOT.jar

# 호스트의 JAR 파일을 컨테이너의 /app.jar 경로로 복사
COPY ${JAR_FILE} app.jar

# 애플리케이션 실행 명령어
# 컨테이너가 시작될 때 실행될 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]