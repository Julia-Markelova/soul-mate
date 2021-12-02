"""update user roles with mentor and relative

Revision ID: d819328b4b79
Revises: 6f045fc04aa7
Create Date: 2021-12-02 21:07:09.304676

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'd819328b4b79'
down_revision = '6f045fc04aa7'
branch_labels = None
depends_on = None


def upgrade():
    op.execute("""
    ALTER TYPE public.user_role ADD VALUE 'MENTOR';
    ALTER TYPE public.user_role ADD VALUE 'RELATIVE';
    """)


def downgrade():
    pass
