import os
from urllib.parse import urlparse

import psycopg2 as psycopg2


def delete(table_name: str, cur):
    query = f"DELETE FROM {table_name} CASCADE ;"
    cur.execute(query)


if __name__ == '__main__':
    db_url = os.getenv('DATABASE_URL', 'postgresql://postgres:1234@localhost:55435/soul_mate')

    url = urlparse(db_url)
    conn = psycopg2.connect(
        host=url.hostname,
        database=url.path[1:],
        user=url.username,
        password=url.password,
        port=url.port)

    cur = conn.cursor()

    delete('users', cur)
    delete('gods', cur)
    delete('souls', cur)
    delete('lifes', cur)
    delete('soul_relatives', cur)
    delete('personal_programs', cur)
    delete('exercises', cur)
    delete('program_exercises', cur)
    delete('life_sparks', cur)
    delete('life_tickets', cur)
    delete('system_modes', cur)

    conn.commit()
