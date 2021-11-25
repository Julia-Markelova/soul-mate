import datetime
import os
import random
from urllib.parse import urlparse
from uuid import uuid4

import psycopg2 as psycopg2
from psycopg2.extras import execute_values

god_names = ['Зевс (Юпитер)',
             'Посейдон (Нептун)',
             'Гефест (Вулкан)',
             'Аполлон',
             'Гермес (Меркурий)',
             'Арес (Марс)',
             'Гера (Юнона)',
             'Афина (Минерва)']

men_names = ['Александр', 'Дмитрий', 'Максим', 'Сергей', 'Андрей', 'Алексей', 'Артём', 'Илья', 'Кирилл', 'Михаил',
             'Никита', 'Матвей', 'Роман', 'Егор', 'Арсений', 'Иван', 'Денис', 'Евгений', 'Даниил', 'Тимофей',
             'Владислав', 'Игорь', 'Владимир', 'Павел', 'Руслан', 'Марк', 'Константин', 'Тимур', 'Олег', 'Ярослав',
             'Антон', 'Николай', 'Глеб', 'Данил', 'Савелий', 'Вадим', 'Степан', 'Юрий', 'Богдан', 'Артур', 'Семен',
             'Макар', 'Лев', 'Виктор', 'Елисей', 'Виталий', 'Вячеслав', 'Захар', 'Мирон', 'Дамир', 'Георгий', 'Давид',
             'Платон', 'Анатолий', 'Григорий', 'Демид', 'Данила', 'Станислав', 'Василий', 'Федор', 'Родион', 'Леонид',
             'Одиссей', 'Валерий', 'Святослав', 'Борис', 'Эдуард', 'Марат', 'Герман', 'Даниэль', 'Петр', 'Амир',
             'Всеволод', 'Мирослав', 'Гордей', 'Артемий', 'Эмиль', 'Назар', 'Савва', 'Ян', 'Рустам', 'Игнат', 'Влад',
             'Альберт', 'Тамерлан', 'Айдар', 'Роберт', 'Адель', 'Марсель', 'Ильдар', 'Самир', 'Тихон', 'Рамиль',
             'Ринат', 'Радмир', 'Филипп', 'Арсен', 'Ростислав', 'Святогор', 'Яромир',
             ]

women_names = [
    'Анастасия', 'Анна', 'Мария', 'Елена', 'Дарья', 'Алина', 'Ирина', 'Екатерина', 'Арина', 'Полина', 'Ольга', 'Юлия',
    'Татьяна', 'Наталья', 'Виктория', 'Елизавета', 'Ксения', 'Милана', 'Вероника', 'Алиса', 'Валерия', 'Александра',
    'Ульяна', 'Кристина', 'София', 'Марина', 'Светлана', 'Варвара', 'Софья', 'Диана', 'Яна', 'Кира', 'Ангелина',
    'Маргарита', 'Ева', 'Алёна', 'Дарина', 'Карина', 'Василиса', 'Олеся', 'Аделина', 'Оксана', 'Таисия', 'Надежда',
    'Евгения', 'Элина', 'Злата', 'Есения', 'Милена', 'Вера', 'Мирослава', 'Галина', 'Людмила', 'Валентина', 'Нина',
    'Эмилия', 'Камилла', 'Альбина', 'Лилия', 'Любовь', 'Лариса', 'Эвелина', 'Инна', 'Агата', 'Амелия', 'Амина',
    'Эльвира', 'Ярослава', 'Стефания', 'Регина', 'Алла', 'Виолетта', 'Лидия', 'Амалия', 'Наталия', 'Марьяна',
    'Анжелика', 'Нелли', 'Влада', 'Виталина', 'Майя', 'Тамара', 'Мелания', 'Лиана', 'Василина', 'Зарина', 'Алия',
    'Владислава', 'Самира', 'Антонина', 'Ника', 'Мадина', 'Наташа', 'Каролина', 'Снежана', 'Юлиана', 'Ариана',
    'Эльмира', 'Ясмина', 'Жанна'
]

surnames = [
    'Иванов', 'Смирнов', 'Кузнецов', 'Попов', 'Васильев', 'Петров', 'Соколов', 'Михайлов', 'Новиков', 'Фёдоров',
    'Морозов', 'Волков', 'Алексеев', 'Лебедев', 'Семенов', 'Егоров', 'Павлов', 'Козлов', 'Степанов', 'Николаев',
    'Орлов', 'Андреев', 'Макаров', 'Никитин', 'Захаров'
]

soul_statuses = ['UNBORN', 'BORN', 'LOST', 'DEAD']

users = []
gods = []
souls = []
lifes = []
soul_relatives = []
exercises = []
program_exercises = []
personal_programs = []
life_sparks = []
life_tickets = []

users_count = 250
life_souls = 50
exercises_count = 50

for i in range(users_count):
    if i < len(god_names):
        role = 'GOD'
    else:
        role = 'SOUL'
    users.append({
        'id': str(uuid4()),
        'login': f'user_{i}',
        'password': '1234',
        'role': role
    })

for i in range(users_count):
    if i < len(god_names):
        gods.append({
            'id': str(uuid4()),
            'user_id': users[i]['id'],
            'name': god_names[i],
        })
    else:
        status = random.choice(soul_statuses)
        souls.append({
            'id': str(uuid4()),
            'user_id': users[i]['id'],
            'status': status,
            'is_mentor': False if status != 'DEAD' else random.choice([True, False])
        })

# create admin
users.append({
    'id': str(uuid4()),
    'login': f'admin',
    'password': '1234',
    'role': 'ADMIN',
})

for soul in souls[:life_souls]:
    count_of_lifes = random.randint(1, 3)
    for i in range(count_of_lifes):
        is_men = random.choice([True, False])
        born_years_ago = random.randint(10, 120)
        date_of_birth = datetime.datetime.now() - datetime.timedelta(days=365 * born_years_ago)
        date_of_death = None
        is_dead = random.choice([True, False])
        if is_dead:
            age = random.randint(10, born_years_ago)
            date_of_death = date_of_birth + datetime.timedelta(days=365 * age)

        lifes.append({
            'id': str(uuid4()),
            'soul_id': soul['id'],
            'karma': random.randint(0, 100),
            'soul_name': random.choice(men_names) if is_men else random.choice(women_names),
            'soul_surname': random.choice(surnames) if is_men else f'{random.choice(surnames)}a',
            'date_of_birth': date_of_birth,
            'date_of_death': date_of_death,
        })

dead_souls = [soul for soul in lifes if soul['date_of_death'] is not None]
alive_souls = [soul for soul in lifes if soul['date_of_death'] is None]
mentors = [soul for soul in souls if soul['is_mentor']]

for i in range(len(alive_souls)):
    relatives = random.randint(0, 2)
    for j in range(relatives):
        soul_relatives.append({
            'id': str(uuid4()),
            'soul_id': random.choice(dead_souls)['soul_id'],
            'relative_id': alive_souls[i]['id'],
            'notify_relative_about_soul': True,
        })

for i in range(exercises_count):
    exercises.append({
        'id': str(uuid4()),
        'name': f'ex_{i}',
        'skill': f'skill_{i}'
    })

for soul in souls:
    life = [life for life in lifes if life['soul_id'] == soul['id']]
    if len(life) > 0:
        has_program = True
        created_date = life[0]['date_of_birth'] - datetime.timedelta(days=random.randint(1, 31))
        progress = 100
        status = 'SUCCESS'
    else:
        has_program = random.choice([True, False])
        created_date = datetime.datetime.now() - datetime.timedelta(days=random.randint(1, 31))
        progress = random.randint(0, 99)
        status = 'NEW' if progress == 0 else 'IN_PROGRESS'
    if has_program:
        personal_programs.append({
            'id': str(uuid4()),
            'soul_id': soul['id'],
            'created_date': created_date,
            'progress_percentage': progress,
            'status': status,
        })

for program in personal_programs:
    for ex in exercises:
        if random.choice([True, False]):
            program_exercises.append({
                'id': str(uuid4()),
                'program_id': program['id'],
                'exercise_id': ex['id'],
            })

for life in lifes:
    program = [program for program in personal_programs if program['soul_id'] == life['soul_id']]
    life_sparks.append({
        'id': str(uuid4()),
        'receive_date': life['date_of_birth'],
        'received_by': life['soul_id'],
        'issued_by': random.choice(mentors)['id'],
        'personal_program_id': program[0]['id']
    })

for life_spark in life_sparks:
    life_tickets.append({
        'id': str(uuid4()),
        'receive_date': life_spark['receive_date'],
        'life_spark_id': life_spark['id'],
        'is_auto_issued': random.choice([True, False]),
    })


def insert(table_name: str, rec: list, cur):
    columns = rec[0].keys()
    query = "INSERT INTO {} ({}) VALUES %s".format(table_name, ','.join(columns))

    # convert projects values to sequence of seqeences
    values = [[value for value in r.values()] for r in rec]

    execute_values(cur, query, values)


if __name__ == '__main__':
    db_url = os.getenv('DATABASE_URL')
    if db_url is None:
        raise ValueError('Установите переменную окружения `DATABASE_URL`')
    url = urlparse(db_url)
    conn = psycopg2.connect(
        host=url.hostname,
        database=url.path[1:],
        user=url.username,
        password=url.password,
        port=url.port)

    cur = conn.cursor()

    try:
        insert('users', users, cur)
        insert('gods', gods, cur)
        insert('souls', souls, cur)
        insert('lifes', lifes, cur)
        insert('soul_relatives', soul_relatives, cur)
        insert('personal_programs', personal_programs, cur)
        insert('exercises', exercises, cur)
        insert('program_exercises', program_exercises, cur)
        insert('life_sparks', life_sparks, cur)
        insert('life_tickets', life_tickets, cur)

        conn.commit()

    except psycopg2.errors.UniqueViolation:
        print('Данные в бд уже добавлены.')


