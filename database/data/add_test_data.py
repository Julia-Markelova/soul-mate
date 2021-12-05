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

skills = ['Дружелюбие', 'Уверенность', 'Любопытство', 'Лидерство', 'Стойкость',
          'Интеллект', 'Выносливость', 'Удача', 'Чистоплотность', 'Эгоизм',
          'Сила', 'Внимательность', 'Харизма', 'Чувство юмора', 'Эмпатия']

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
souls_count = 100
relative_count = 10
mentors_count = 10
life_souls = 20

for i in range(len(god_names)):
    user_id = str(uuid4())
    users.append({
        'id': user_id,
        'login': f'god_{i}',
        'password': '1234',
        'role': 'GOD'
    })
    gods.append({
        'id': str(uuid4()),
        'user_id': user_id,
        'name': god_names[i],
    })

for i in range(souls_count):
    user_id = str(uuid4())
    users.append({
        'id': user_id,
        'login': f'soul_{i}',
        'password': '1234',
        'role': 'SOUL'
    })
    status = random.choice(soul_statuses)
    souls.append({
        'id': str(uuid4()),
        'user_id': user_id,
        'status': status,
        'is_mentor': False
    })

for i in range(relative_count):
    user_id = str(uuid4())
    soul_id = str(uuid4())
    users.append({
        'id': user_id,
        'login': f'relative_{i}',
        'password': '1234',
        'role': 'RELATIVE'
    })
    souls.append({
        'id': soul_id,
        'user_id': user_id,
        'status': 'BORN',
        'is_mentor': False
    })
    is_men = random.choice([True, False])
    born_years_ago = random.randint(15, 60)

    life_id = str(uuid4())
    lifes.append({
        'id': life_id,
        'soul_id': soul_id,
        'karma': random.randint(0, 100),
        'soul_name': random.choice(men_names) if is_men else random.choice(women_names),
        'soul_surname': random.choice(surnames) if is_men else f'{random.choice(surnames)}a',
        'date_of_birth': datetime.datetime.now() - datetime.timedelta(days=365 * born_years_ago),
        'date_of_death': None,
    })

    soul_relatives.append({
        'id': str(uuid4()),
        'soul_id': soul_id,
        'relative_id': life_id,
        'notify_relative_about_soul': True,
    })

for i in range(mentors_count):
    user_id = str(uuid4())
    users.append({
        'id': user_id,
        'login': f'mentor_{i}',
        'password': '1234',
        'role': 'MENTOR'
    })
    souls.append({
        'id': str(uuid4()),
        'user_id': user_id,
        'status': 'DEAD',
        'is_mentor': True
    })

# create admin
users.append({
    'id': str(uuid4()),
    'login': f'admin',
    'password': '1234',
    'role': 'ADMIN',
})


def create_life(soul_id: str, is_alive: bool):
    is_men = random.choice([True, False])
    if is_alive:
        born_years_ago = random.randint(10, 80)
        date_of_birth = datetime.datetime.now() - datetime.timedelta(days=365 * born_years_ago)
        date_of_death = None
    else:
        born_years_ago = random.randint(100, 120)
        date_of_birth = datetime.datetime.now() - datetime.timedelta(days=365 * born_years_ago)
        age = random.randint(10, 110)
        date_of_death = date_of_birth + datetime.timedelta(days=365 * age)

    return {
        'id': str(uuid4()),
        'soul_id': soul_id,
        'karma': random.randint(0, 100),
        'soul_name': random.choice(men_names) if is_men else random.choice(women_names),
        'soul_surname': random.choice(surnames) if is_men else f'{random.choice(surnames)}a',
        'date_of_birth': date_of_birth,
        'date_of_death': date_of_death,
    }


soul_id_to_life = {life['soul_id']: life for life in lifes}

for soul in souls:
    if soul['status'] == 'BORN':
        if soul_id_to_life.get(soul['id'], None) is None:
            lifes.append(create_life(soul['id'], True))
    elif soul['status'] == 'UNBORN':
        continue
    else:
        lifes.append(create_life(soul['id'], False))

dead_souls = [soul for soul in lifes if soul['date_of_death'] is not None]
alive_souls = [soul for soul in lifes if soul['date_of_death'] is None]
mentors = [soul for soul in souls if soul['is_mentor']]
#
# for i in range(len(alive_souls)):
#     relatives = random.randint(0, 2)
#     for j in range(relatives):
#         soul_relatives.append({
#             'id': str(uuid4()),
#             'soul_id': random.choice(dead_souls)['soul_id'],
#             'relative_id': alive_souls[i]['id'],
#             'notify_relative_about_soul': True,
#         })

for i in range(len(skills)):
    exercises.append({
        'id': str(uuid4()),
        'name': f'ex_{i}',
        'skill': skills[i]
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
        status = 'NEW'
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

system_modes = [
    {
        'id': str(uuid4()),
        'admin_id': str(users[-1]['id']),
        'is_manual_mode': False,
        'type': type_,
    } for type_ in ['LIFE_SPARK_MODE', 'LIFE_TICKET_MODE']
]


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
        insert('system_modes', system_modes, cur)

        conn.commit()

    except psycopg2.errors.UniqueViolation:
        print('Данные в бд уже добавлены.')
