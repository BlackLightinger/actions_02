# Используем образ с OpenJDK 17
FROM openjdk:17-jdk-slim

# Рабочая директория для приложения
WORKDIR /app

# Копируем проект в контейнер
COPY . /app

# Устанавливаем wget и unzip для скачивания и распаковки Gradle
RUN apt-get update && apt-get install -y wget unzip

# Устанавливаем переменную окружения JAVA_HOME для OpenJDK 17
ENV JAVA_HOME=/usr/lib/jvm/temurin-17-jdk-amd64

# Скачиваем и распаковываем Gradle
RUN wget https://services.gradle.org/distributions/gradle-8.0-bin.zip && \
    unzip gradle-8.0-bin.zip && \
    mv gradle-8.0 /opt/gradle && \
    ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle

# Копируем gradle wrapper в контейнер
COPY gradle /app/gradle

# Ожидаем, что jar файл будет собран на этапе локальной сборки
# Просто запускаем сервер через команду `java -jar` или с помощью gradle
CMD ["./gradlew", "run"]
