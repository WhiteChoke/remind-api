from sqlalchemy.orm import (DeclarativeBase,
                            mapped_column,
                            Mapped)

from sqlalchemy import BIGINT

class Base(DeclarativeBase):
    pass

class Cards(Base):
    __tablename__ = "card"

    id: Mapped[int] = mapped_column(primary_key=True)
    card_id: Mapped[int] = mapped_column(BIGINT, unique=True)
    card_file_name: Mapped[str]
    relative_path: Mapped[str]
