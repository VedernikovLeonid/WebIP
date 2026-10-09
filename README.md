# GARAGE 17 — лабораторная работа №2

Учебный проект по ТЗ **«REST API (многоуровневая архитектура)»** для предметной области «Система записи клиентов на ремонт и ТО автомобилей».

## Реализация требований

- Три сущности предметной области: клиент, автомобиль и запись на обслуживание.
- Все сущности наследуются от `BaseEntity` и содержат getters/setters.
- Связи реализованы ссылками: автомобиль ссылается на клиента, запись ссылается на клиента и автомобиль.
- Для каждой сущности создан `@Repository`, являющийся наследником `MapRepository` и реализующий `CommonRepository`.
- Для каждой сущности создан `@Service` с бизнес-логикой CRUD и зависимостями, переданными через конструктор.
- Для каждой сущности создан `@RestController` с полным CRUD и общим префиксом `/api/v1`.
- Для каждой сущности используются отдельные DTO запроса `Rq` и ответа `Rs`.
- Входные данные проверяются через `@Valid` и Jakarta Validation.
- Преобразование Entity <-> DTO вынесено в отдельные mapper-компоненты.
- Добавлены три unit-теста: `ClientServiceTest`, `VehicleServiceTest`, `AppointmentServiceTest`.
- React-клиент работает с новой многоуровневой REST API: перед созданием записи сохраняет клиента и автомобиль, затем создаёт запись по их идентификаторам.

## Слои backend

```text
backend/src/main/java/ru/webip/autoservice/
├── entity/       # BaseEntity, ClientEntity, VehicleEntity, AppointmentEntity
├── repository/   # CommonRepository, MapRepository и репозитории сущностей
├── service/      # бизнес-логика и CRUD
├── mapper/       # преобразование Entity в Rq/Rs DTO и обратно
├── dto/          # ClientRq/Rs, VehicleRq/Rs, AppointmentRq/Rs
├── controller/   # REST-контроллеры
├── config/       # CORS и демонстрационные данные
└── exception/    # единый формат ошибок API
```

Хранилище остаётся in-memory на основе `LinkedHashMap`, как требует учебное ТЗ. При старте `DemoDataInitializer` создаёт демонстрационных клиентов, автомобили и записи через сервисный слой.

## Запуск

Откройте корень `WebIP` в IntelliJ IDEA как Gradle-проект и запустите `ru.webip.autoservice.AutoServiceApplication`.

Backend: [http://localhost:8080](http://localhost:8080)

Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

React-клиент запускается отдельно:

```powershell
cd frontend
npm install
npm run dev
```

Клиент будет доступен на [http://localhost:5173](http://localhost:5173).

## Endpoint'ы

| Метод | URL | Назначение |
| --- | --- | --- |
| GET, POST | `/api/v1/clients` | список и создание клиентов |
| GET, PUT, DELETE | `/api/v1/clients/{id}` | просмотр, изменение и удаление клиента |
| GET, POST | `/api/v1/vehicles` | список и создание автомобилей |
| GET, PUT, DELETE | `/api/v1/vehicles/{id}` | просмотр, изменение и удаление автомобиля |
| GET, POST | `/api/v1/appointments` | список и создание записей |
| GET, PUT, DELETE | `/api/v1/appointments/{id}` | просмотр, изменение и удаление записи |

Проверка backend выполняется командой `gradlew.bat test`.
