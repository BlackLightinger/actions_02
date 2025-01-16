# Используем образ с OpenJDK 21
FROM openjdk:17-jdk-slim

# Рабочая директория для приложения
WORKDIR /app

# Копируем проект в контейнер
COPY . /app

RUN apt-get update && apt-get install -y wget unzip

# Устанавливаем wget и unzip для скачивания и распаковки Gradle
RUN wget https://services.gradle.org/distributions/gradle-8.0-bin.zip && \
    unzip gradle-8.0-bin.zip && \
    mv gradle-8.0 /opt/gradle && \
    ln -s /opt/gradle/bin/gradle /usr/local/bin/gradle

# Копируем gradle wrapper в контейнер (если он есть в вашем проекте)
COPY gradle /app/gradle

# Если вы хотите использовать gradle wrapper, убедитесь, что он доступен в пути
RUN chmod +x ./gradlew

# Устанавливаем переменные окружения для использования JDK 11
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

# Ожидаем, что jar файл будет собран на этапе локальной сборки
# Просто запускаем сервер через команду `java -jar` или с помощью gradle
CMD ["./gradlew", "run"]
