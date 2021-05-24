"""notification tables

Revision ID: b72d29514cc1
Revises: 99f7d5f140c2
Create Date: 2021-05-24 20:38:10.823143

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'b72d29514cc1'
down_revision = '99f7d5f140c2'
branch_labels = None
depends_on = None


def upgrade():
    op.execute(
        """
        
        CREATE TYPE public.help_request_status AS ENUM (
            'NEW',
            'ACCEPTED',
            'FINISHED'
        );
        
        CREATE TYPE public.message_status AS ENUM (
            'NEW',
            'SENT',
            'DELIVERED',
            'ERROR'
        );
        
        CREATE TABLE public.help_requests (
            id                      uuid  PRIMARY KEY,
            created_by              uuid        REFERENCES public.souls(id) ON DELETE CASCADE,
            accepted_by             uuid        REFERENCES public.gods(id) ON DELETE CASCADE,
            status                  public.help_request_status
        );
        
        CREATE TABLE public.messages (
            id                      uuid  PRIMARY KEY,
            relative_id             uuid        REFERENCES public.lifes(id) ON DELETE CASCADE,
            message                 varchar NOT NULL,
            status                  public.message_status
        );
        
        """
    )


def downgrade():
    op.execute(
        """
        DROP TABLE public.help_requests cascade;
        DROP TABLE public.messages cascade;


        DROP TYPE public.help_request_status cascade;
        DROP TYPE public.message_status cascade;
        
        """
    )
