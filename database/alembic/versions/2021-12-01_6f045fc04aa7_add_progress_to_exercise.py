"""add progress to exercise

Revision ID: 6f045fc04aa7
Revises: 1156d745acb3
Create Date: 2021-12-01 17:22:53.671731

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '6f045fc04aa7'
down_revision = '1156d745acb3'
branch_labels = None
depends_on = None


def upgrade():
    op.execute("""
    ALTER TABLE public.program_exercises ADD COLUMN progress INTEGER DEFAULT 0 NOT NULL;
    """)


def downgrade():
    op.execute("""
    ALTER TABLE public.program_exercises DROP COLUMN progress;
    """)
