"""add tables

Revision ID: 1156d745acb3
Revises: f207da28f482
Create Date: 2021-11-26 22:25:38.946287

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '1156d745acb3'
down_revision = 'f207da28f482'
branch_labels = None
depends_on = None


def upgrade():
    op.execute(
        """
        
        CREATE TYPE public.system_mode_type AS ENUM (
            'LIFE_SPARK_MODE',
            'LIFE_TICKET_MODE'
        );
        
        CREATE TABLE public.system_modes (
            id                      uuid  PRIMARY KEY,
            admin_id                uuid        REFERENCES public.users(id) ON DELETE CASCADE ON UPDATE CASCADE,
            type                    public.system_mode_type,
            is_manual_mode          BOOLEAN NOT NULL
        );
        
        """
    )


def downgrade():
    op.execute(
        """
        DROP TABLE public.system_modes cascade;
        DROP TYPE public.system_mode_type cascade;
        
        """
    )
