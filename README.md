# GARAGE 17 — лабораторная работа №1

Готовый учебный проект по ТЗ **«REST API (контроллеры)»** для предметной области «Система записи клиентов на ремонт и ТО автомобилей».

## Что сделано по ТЗ

- Java Spring Boot приложение с Gradle-сборкой, открываемое в IntelliJ IDEA.
- DTO `AppointmentDto`: все поля приватные, данные REST API передаются через DTO.
- `AppointmentController` с `@RestController` и общим префиксом `@RequestMapping("/api/v1/appointments")`.
- Предзаполненная приватная коллекция `LinkedHashMap<Long, AppointmentDto>`.
- Полный CRUD: `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}`.
- Валидация входных данных и единый обработчик ошибок через `@RestControllerAdvice`.
- CORS для React-клиента на `localhost:5173`.
- Swagger UI и OpenAPI-документация.
- React-клиент, который загружает записи из REST API и выполняет создание, редактирование, удаление и фильтрацию записей.
- `data.json` с той же моделью данных, которая использовалась бы для json-server. Spring Boot API заменяет json-server для React-приложения.

## Структура

```text
WebIP/
├── build.gradle                         # Gradle-конфигурация Spring Boot
├── settings.gradle
├── data.json                            # исходные демонстрационные данные
├── backend/src/main/java/...            # Java-код приложения
│   ├── AutoServiceApplication.java
│   ├── config/CorsConfig.java
│   ├── controller/AppointmentController.java
│   ├── dto/AppointmentDto.java
│   ├── exception/                       # 404, 400 и единый формат ошибок
│   └── model/AppointmentStatus.java
├── backend/src/main/resources/application.yml
└── frontend/                            # React + Vite клиент
    ├── package.json
    ├── vite.config.js
    └── src/App.jsx
```

## Запуск в IntelliJ IDEA

1. Откройте папку `WebIP` в IntelliJ IDEA.
2. Импортируйте `build.gradle` как Gradle-проект и дождитесь загрузки зависимостей.
3. Запустите класс `ru.webip.autoservice.AutoServiceApplication`.
4. Проверьте API в [Swagger UI](http://localhost:8080/swagger-ui/index.html) или по адресу [OpenAPI JSON](http://localhost:8080/api-docs).
5. В отдельном терминале запустите React-клиент:

```powershell
cd frontend
npm install
npm run dev
```

6. Откройте [http://localhost:5173](http://localhost:5173).

## REST endpoints

| Метод | URL | Назначение |
| --- | --- | --- |
| GET | `/api/v1/appointments` | список записей, опциональный фильтр `?status=NEW` |
| GET | `/api/v1/appointments/{id}` | одна запись |
| POST | `/api/v1/appointments` | создать запись |
| PUT | `/api/v1/appointments/{id}` | полностью обновить запись |
| DELETE | `/api/v1/appointments/{id}` | удалить запись |

Для учебной лабораторной коллекция хранится в памяти приложения и заполняется предопределёнными DTO при запуске. При перезапуске данные возвращаются к демонстрационному состоянию.
