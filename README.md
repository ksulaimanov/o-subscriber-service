# O! Subscriber Service

Backend application сервиса абонента мобильного оператора **O!**.

Стек: **Java 17**, **Spring Boot**, **Thymeleaf**, **Spring Security**, **PostgreSQL**, **Maven**.

## Функциональность
Приложение включает две зоны:

**Абонентская часть**
- регистрация
- вход в систему
- просмотр профиля
- пополнение баланса
- смена тарифа
- обновление email
- загрузка и удаление фотографии

**Административная часть**
- просмотр списка абонентов
- просмотр профиля абонента
- активация и деактивация абонента

## Реализованные требования
- Spring MVC
- REST API
- Validation и Custom Validation
- Standard Service / Abstract class / Bean Component
- Native JDBC / JdbcClient / JdbcTemplate / JdbcOperations / JPA
- Transactional
- Photo save to File System
- Basic Authentication
- JWT
- Form Login

## Требования для запуска
- Java 17
- Maven
- PostgreSQL

## Настройка БД
Создать базу данных:

```sql
CREATE DATABASE o_subscriber_db;
```
Конфигурация

Файл: src/main/resources/application.yml

```
spring:
datasource:
url: jdbc:postgresql://localhost:5432/o_subscriber_db
username: ваш_username
password: ваш_password
driver-class-name: org.postgresql.Driver

jpa:
hibernate:
ddl-auto: update
show-sql: true

servlet:
multipart:
max-file-size: 10MB
max-request-size: 10MB

server:
port: 8080
error:
whitelabel:
enabled: false

app:
jwt:
secret: ${JWT_SECRET:myVerySecretKeyForJwtTokenGeneration123456}
expiration: 86400000
file:
upload-dir: uploads/
```


Запуск

Из корня проекта:
```mvn spring-boot:run```
После запуска приложение доступно по адресу:

```http://localhost:8080```

Доступ
Администратор

Администратор создаётся автоматически при запуске приложения через DataInitializer.

login: admin

password: admin123