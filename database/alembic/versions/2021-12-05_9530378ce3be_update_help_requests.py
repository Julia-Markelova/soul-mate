"""update help requests

Revision ID: 9530378ce3be
Revises: 312a604b7ecc
Create Date: 2021-12-05 18:28:46.998463

"""
from alembic import op

# revision identifiers, used by Alembic.
revision = '9530378ce3be'
down_revision = '312a604b7ecc'
branch_labels = None
depends_on = None


def upgrade():
    op.execute("""
    ALTER TABLE public.help_requests DROP CONSTRAINT help_requests_accepted_by_fkey;
    CREATE TYPE public.help_request_type AS ENUM ('GOD', 'MENTOR');
    ALTER TABLE public.help_requests ADD COLUMN type public.help_request_type NOT NULL DEFAULT 'GOD';
   """)


def downgrade():
    op.execute("""
    ALTER TABLE public.help_requests DROP COLUMN type;
    DROP TYPE public.help_request_type;
    ALTER TABLE public.help_requests ADD CONSTRAINT help_requests_accepted_by_fkey FOREIGN KEY (accepted_by) 
    REFERENCES public.gods (id);
   """)
