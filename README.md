# База данных

- Запуск и добавление таблиц:
```
   $ cd database
   $ export DB_PORT=55435 // или любой другой
   $ docker-compose up
```

- Подключение:
  - database: `soul_mate`
  - user: `postgres`
  - port: `55435`  // ваш порт
  - password: `1234`
  
- Тестовые данные:
  - в `database/data` есть скрипт по добавлению тестовых данных
  - скрипт запускается автоматически в `docker-compose.yml` (step `test_data`)

# Бэк
- после запуска базы данных в IntelliJ во вкладке Database 
   - + Data Source 
   - PostrgeSQL 
   - ввести данные
- при добавлении моделей в пакете ifmo.solumate.demo.models
   - если будут подсвечиваться красным аннотации Table / Column, нажать Alt+Enter, выбрать созданный на первом шаге Data source
- для получения и работы с данными использовать слой репозиториев ifmo.soulmate.demo.models.repositories
- контроллеры создавать в пакете ifmo.soulmate.demo.models.controllers
- конфиг для работы JPA c БД находится в C:\study\mpi\soul-mate\src\main\resources\application.properties
