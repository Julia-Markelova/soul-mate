"""add table for rejected requests

Revision ID: 2bb95a57477b
Revises: 9530378ce3be
Create Date: 2021-12-05 21:12:25.260621

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '2bb95a57477b'
down_revision = '9530378ce3be'
branch_labels = None
depends_on = None


def upgrade():
    op.execute("""
    CREATE TABLE public.rejected_requests (
        id UUID PRIMARY KEY,
        rejected_by UUID NOT NULL,
        request_id UUID REFERENCES public.help_requests(id) ON DELETE CASCADE ON UPDATE CASCADE
    );
    """)


def downgrade():
    op.execute("""
    DROP TABLE public.rejected_requests;
    """)
