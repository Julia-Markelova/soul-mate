"""drop not null constraints

Revision ID: 7b240435f2e4
Revises: 2bb95a57477b
Create Date: 2021-12-07 00:54:37.535135

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '7b240435f2e4'
down_revision = '2bb95a57477b'
branch_labels = None
depends_on = None


def upgrade():
    op.execute(
        """
        ALTER TABLE public.life_sparks ALTER COLUMN personal_program_id DROP NOT NULL;
        ALTER TABLE public.life_sparks ALTER COLUMN issued_by DROP NOT NULL;
        """
    )


def downgrade():
    op.execute(
        """
        ALTER TABLE public.life_sparks ALTER COLUMN personal_program_id SET NOT NULL;
        ALTER TABLE public.life_sparks ALTER COLUMN issued_by SET NOT NULL;
        """
    )
