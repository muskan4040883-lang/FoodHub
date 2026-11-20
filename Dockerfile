FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN javac --add-modules jdk.httpserver FoodHub.java
EXPOSE 8081
CMD ["java", "--add-modules", "jdk.httpserver", "FoodHub"]
