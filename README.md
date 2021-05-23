# База данных

- Запуск и добавление таблиц:
```
   $ cd database
   $ export `cat .env | xargs`  // там порт для бд указан
   $ docker-compose up
```

- Подключение:
  - database: `soul_mate`
  - user: `postgres`
  - port: указан в `database/.env`
  - password: `1234`
  
