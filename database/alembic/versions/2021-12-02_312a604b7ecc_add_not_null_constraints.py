"""Add not null constraints

Revision ID: 312a604b7ecc
Revises: d819328b4b79
Create Date: 2021-12-02 23:38:41.559477

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '312a604b7ecc'
down_revision = 'd819328b4b79'
branch_labels = None
depends_on = None


def upgrade():
    op.alter_column("gods", "user_id", nullable=False)
    op.alter_column("souls", "user_id", nullable=False)
    op.alter_column("souls", "status", nullable=False)
    op.alter_column("lifes", "soul_id", nullable=False)
    op.alter_column("lifes", "soul_name", nullable=False)
    op.alter_column("lifes", "soul_surname", nullable=False)
    op.alter_column("soul_relatives", "soul_id", nullable=False)
    op.alter_column("soul_relatives", "relative_id", nullable=False)
    op.alter_column("exercises", "name", nullable=False)
    op.alter_column("exercises", "skill", nullable=False)
    op.alter_column("personal_programs", "soul_id", nullable=False)
    op.alter_column("personal_programs", "status", nullable=False)
    op.alter_column("program_exercises", "program_id", nullable=False)
    op.alter_column("program_exercises", "exercise_id", nullable=False)
    op.alter_column("life_sparks", "received_by", nullable=False)
    op.alter_column("life_sparks", "issued_by", nullable=False)
    op.alter_column("life_sparks", "personal_program_id", nullable=False)
    op.alter_column("life_tickets", "life_spark_id", nullable=False)


def downgrade():
    pass
