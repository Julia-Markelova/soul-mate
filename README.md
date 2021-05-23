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