"""new_schema

Revision ID: 99f7d5f140c2
Revises: 421a73ac7d57
Create Date: 2021-05-23 14:02:24.785532

"""
from alembic import op

# revision identifiers, used by Alembic.
revision = '99f7d5f140c2'
down_revision = None
branch_labels = None
depends_on = None

sql = f"""
        CREATE TYPE public.soul_status AS ENUM (
            'UNBORN',
            'BORN',
            'LOST',
            'DEAD'
        );

        CREATE TYPE public.personal_program_status AS ENUM (
            'NEW',
            'IN_PROGRESS',
            'SUCCESS',
            'FAIL'
        );

        -- tables --

        CREATE TABLE public.users (
            id                      uuid  PRIMARY KEY,
            login                   VARCHAR (50) NOT NULL UNIQUE,
            password                VARCHAR (50) NOT NULL,
            is_admin                boolean default false
        );

        
        CREATE TABLE public.gods (
            id                  uuid  PRIMARY KEY,
            user_id             uuid        REFERENCES public.users(id) ON DELETE CASCADE,
            name                VARCHAR (256) NOT NULL
        );

        CREATE TABLE public.souls (
            id                  uuid  PRIMARY KEY,
            user_id             uuid        REFERENCES public.users(id) ON DELETE CASCADE,
            status              public.soul_status,
            is_mentor           boolean default false
        );

        CREATE TABLE public.lifes (
            id                  uuid  PRIMARY KEY,
            soul_id             uuid        REFERENCES public.souls(id) ON DELETE CASCADE,
            date_of_birth       timestamp without time zone,
            date_of_death       timestamp without time zone,
            soul_name           VARCHAR (256),
            soul_surname        VARCHAR (256),
            karma               integer default 0
        );

        CREATE TABLE public.soul_relatives (
            id                              uuid  PRIMARY KEY,
            soul_id                         uuid        REFERENCES public.souls(id) ON DELETE CASCADE,
            relative_id                     uuid        REFERENCES public.lifes(id) ON DELETE CASCADE,
            notify_relative_about_soul      boolean default false
        );

        CREATE TABLE public.exercises(
            id             uuid  PRIMARY KEY,
            name           VARCHAR (256),
            skill          VARCHAR (256)
        );

        CREATE TABLE public.personal_programs (
            id                           uuid  PRIMARY KEY,
            created_date                 timestamp without time zone,
            soul_id                      uuid        REFERENCES public.souls(id) ON DELETE CASCADE,
            progress_percentage          integer default 0,
            status                       public.personal_program_status
        );

        CREATE TABLE public.program_exercises (
            id                      uuid  PRIMARY KEY,
            program_id              uuid        REFERENCES public.personal_programs(id) ON DELETE CASCADE,
            exercise_id             uuid        REFERENCES public.exercises(id) ON DELETE CASCADE
        );

        CREATE TABLE public.life_sparks (
            id                      uuid  PRIMARY KEY,
            receive_date            timestamp   without time zone,
            received_by             uuid        REFERENCES public.souls(id) ON DELETE CASCADE,
            issued_by               uuid        REFERENCES public.souls(id) ON DELETE CASCADE,
            personal_program_id     uuid        REFERENCES public.personal_programs(id) ON DELETE CASCADE
        );

        CREATE TABLE public.life_tickets (
            id                      uuid  PRIMARY KEY,
            receive_date            timestamp   without time zone,
            life_spark_id           uuid        REFERENCES public.life_sparks(id) ON DELETE CASCADE,
            is_auto_issued          boolean default false
        );

    """


def upgrade():
    op.execute(sql)


def downgrade():
    sql = f"""

        DROP TABLE public.life_tickets cascade;
        DROP TABLE public.life_sparks cascade;
        DROP TABLE public.program_exercises cascade;
        DROP TABLE public.personal_programs cascade;
        DROP TABLE public.exercises cascade;
        DROP TABLE public.soul_relatives cascade;
        DROP TABLE public.lifes cascade;
        DROP TABLE public.souls cascade;
        DROP TABLE public.gods cascade;
        DROP TABLE public.users cascade;


        DROP TYPE public.soul_status cascade;
        DROP TYPE public.personal_program_status cascade;

    """

    op.execute(sql)
