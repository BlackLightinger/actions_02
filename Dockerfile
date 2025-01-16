# Используем образ с OpenJDK 21
FROM openjdk:21-jdk-slim

# Рабочая директория для приложения
WORKDIR /app

# Копируем проект в контейнер
COPY . /app

# Устанавливаем wget и unzip для скачивания и распаковки Gradle
RUN apt-get update && apt-get install -y wget unzip && \
    wget https://services.gradle.org/distributions/gradle-8.0-bin.zip && \
    unzip gradle-8.0-bin.zip && \
    mv gradle-8.0 /opt/gradle && \
    ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle

# Копируем gradle wrapper в контейнер (если он есть в вашем проекте)
COPY gradle /app/gradle

# Если вы хотите использовать gradle wrapper, убедитесь, что он доступен в пути
RUN chmod +x ./gradlew

# Ожидаем, что jar файл будет собран на этапе локальной сборки
# Просто запускаем сервер через команду `java -jar` или с помощью gradle
CMD ["./gradlew", "run"]
