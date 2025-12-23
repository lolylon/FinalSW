# FinalExam Project

## Описание
Полнофункциональное Spring Boot приложение с системой аутентификации, базой данных и полным набором тестов.

## Технологический стек
- **Spring Boot 3.2.x**
- **Java 17**
- **Spring Security с JWT**
- **Spring Data JPA**
- **H2 Database**
- **Liquibase + Flyway** (миграции)
- **MapStruct** (маппинг)
- **Docker**
- **JUnit 5 + Mockito** (тестирование)

## Структура проекта

### Основные компоненты
- **Entity**: User, Role, Post
- **Repository**: UserRepository, PostRepository, RoleRepository
- **Service**: UserService, PostService, AuthService
- **Controller**: UserController, PostController, AuthController
- **Security**: JWT аутентификация, CustomUserDetailsService
- **DTO**: UserDto, PostDto, LoginRequest, LoginResponse, RegisterRequest

### Миграции базы данных
- **Liquibase**: XML changelogs в `src/main/resources/db/changelog/`
- **Flyway**: SQL миграции в `src/main/resources/db/migration/`

## Требования к запуску

### Предварительные требования
- JDK 17
- Maven 3.6+
- Docker (опционально)

### Запуск приложения

1. **Сборка проекта:**
```bash
./mvnw clean compile
```

2. **Запуск приложения:**
```bash
./mvnw spring-boot:run
```

3. **Запуск с Docker:**
```bash
docker-compose up
```

## API Эндпоинты

### Аутентификация
- `POST /api/auth/register` - Регистрация пользователя
- `POST /api/auth/login` - Вход пользователя
- `POST /api/auth/refresh` - Обновление токена

### Управление пользователями (требует авторизации)
- `GET /api/users` - Получить всех пользователей
- `GET /api/users/{id}` - Получить пользователя по ID
- `POST /api/users` - Создать пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

### Управление постами (требует авторизации)
- `GET /api/posts` - Получить все посты
- `GET /api/posts/{id}` - Получить пост по ID
- `POST /api/posts` - Создать пост
- `DELETE /api/posts/{id}` - Удалить пост

### Консоль H2
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Тестирование

### Запуск всех тестов
```bash
./mvnw test
```

### Типы тестов
- **Unit тесты**: Сервисы и бизнес-логика
- **Integration тесты**: Контроллеры и REST API
- **Repository тесты**: Работа с базой данных
- **Контрольные тесты**: Полная проверка системы

### Пример контрольного теста
```java
@Test
void testUserRegistrationAndLogin() throws Exception {
    // Регистрация
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setName("Test User");
    registerRequest.setEmail("test@example.com");
    registerRequest.setPassword("password123");
    
    // Вход
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("test@example.com");
    loginRequest.setPassword("password123");
    
    // Проверка JWT токена
    // ...
}
```

## Конфигурация

### application.properties
```properties
# База данных
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=none

# JWT
jwt.secret=mySecretKeyForFinalExamProject123456789
jwt.expiration=86400

# Безопасность
spring.security.user.name=admin
spring.security.user.password=admin
```

## Docker

### Dockerfile
Многоэтапная сборка с оптимизацией размера образа.

### docker-compose.yml
Оркестрация Spring Boot приложения с H2 базой данных.

## Архитектурные паттерны

### Реализованные паттерны
- **Repository Pattern**: Абстракция доступа к данным
- **Service Layer**: Бизнес-логика
- **DTO Pattern**: Передача данных
- **JWT Authentication**: Stateless аутентификация
- **Migration Tools**: Управление схемой БД

### Связи в базе данных
- **User ↔ Role**: Many-to-Many
- **Post → User**: Many-to-One

## Примеры использования

### Регистрация пользователя
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","password":"password123"}'
```

### Вход и получение токена
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"password123"}'
```

### Доступ к защищенному эндпоинту
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Мониторинг и отладка

### Логирование
- Уровень логирования: INFO для приложения, DEBUG для Spring Web
- Форматированный SQL вывод включен

### Health checks
- Spring Actuator endpoints доступны на `/actuator`

## Развитие проекта

### Возможные улучшения
1. **База данных**: PostgreSQL/MySQL вместо H2
2. **Кэширование**: Redis для кэширования
3. **Мониторинг**: Micrometer + Prometheus
4. **API Documentation**: OpenAPI/Swagger
5. **Валидация**: Bean Validation с кастомными валидаторами

## Лицензия
Проект создан в образовательных целях.
