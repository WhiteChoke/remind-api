from sqlalchemy import (delete,
                        select)
from typing import Annotated
from pathlib import Path

from sqlalchemy.ext.asyncio import AsyncSession

from fastapi import (APIRouter,
                     Form,
                     Depends)

from db.engine import localSession
from db.model import Cards

router = APIRouter(prefix="/storage", 
                   tags=["❌ DELETE"])

async def getDB():
    db = localSession()
    try:
        yield db
    finally:
        await db.close()

SessionDep = Annotated[AsyncSession, Depends(getDB)]

async def compare(file_type,
                  type):
    return file_type == type

@router.delete("/card/delete")
async def delete_given_card(id: Annotated[int, Form()],
                               db: SessionDep):
    try:
        stmt = (
            select(Cards).
            where(Cards.card_id == id)
        )

        value = await db.scalar(stmt)

        if not value:
            return False

        ROOT = Path(__file__).resolve().parents[5]
        file = ROOT / "data" / f"file{value.card_id}"
        file.unlink()

        stmt = (
            delete(Cards).
            where(Cards.card_id == id)
        )

        await db.execute(stmt)
        await db.commit()

    except Exception as e:
        return f"Something is wrong! 😂 ERROR: {e}"