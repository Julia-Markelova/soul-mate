"""ADD ROLE

Revision ID: f207da28f482
Revises: b72d29514cc1
Create Date: 2021-11-25 21:46:46.706980

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'f207da28f482'
down_revision = 'b72d29514cc1'
branch_labels = None
depends_on = None



def upgrade():
    op.execute(
        """
        CREATE TYPE public.user_role AS ENUM (
            'ADMIN',
            'GOD',
            'SOUL'
        );
        ALTER TABLE public.users ADD COLUMN role public.user_role;
        ALTER TABLE public.users DROP COLUMN is_admin;
        """
    )


def downgrade():
    op.execute(
        """
        ALTER TABLE public.users DROP COLUMN role;
        DROP TYPE public.user_role cascade;
        ALTER TABLE public.users ADD COLUMN is_admin BOOLEAN DEFAULT FALSE;
        """
    )
