from sqlalchemy import select, update
from pathlib import Path
from typing import Annotated

from sqlalchemy.ext.asyncio import AsyncSession

from dataclasses import dataclass

from fastapi import (APIRouter,
                     UploadFile,
                     File,
                     Form,
                     Depends)

from db.engine import localSession
from db.model import Cards

router = APIRouter(prefix="/storage", 
                   tags=["😂 PUT"])

async def getDB():
    db = localSession()
    try:
        yield db
    finally:
        await db.close()

SessionDep = Annotated[AsyncSession, Depends(getDB)]

@dataclass
class ItemDTO:
    card_id: int
    card_file_name: str
    relative_path: str

async def compare(file_type,
                  type):
    return file_type == type

@router.put("/card/put")
async def replace_old_card(file: Annotated[UploadFile, File()],
                           type: Annotated[str, Form()],
                           old_id: Annotated[int, Form()],
                           new_id: Annotated[int, Form()],
                           db: SessionDep):
    """❗ PAY ATTENTION ❗
    IN THIS ROUTER THERE'S 2 ID VARIATIONS:

    1. OLD_ID (int):
        Stands for the id you want to change.

    2. NEW_ID (int):
        Stands for the id you want to set as new.

    If you don't want to change an ID: just leave set the IDs similar to previous one that belongs to the object in an input field.

    Если по английски не поймёшь, то в два поля вставляется старый ID в том случае если новый не нужен.
    """
    try:

        ft = file.content_type.split("/")[-1]
        fs = file.content_type.split("/")[0]

        stmt_1 = (
            select(Cards).
            where(Cards.card_id == old_id)
        )

        value = await db.scalar(stmt_1)

        if value:
            DATA_DIR = Path(__file__).resolve().parents[5] / "data"

            new_path = DATA_DIR / f"file{new_id}"
            old_path = DATA_DIR / f"file{old_id}"

            old_path.unlink()

            file_info = f"filename: {Path(file.filename).stem}\ntype: {ft}\nsort: {fs}"
            new_path.write_text(file_info, encoding="utf-8")

            RELATIVE = new_path.relative_to(DATA_DIR.parent).as_posix()

            stmt_2 = (
                update(Cards).
                where(Cards.card_id == old_id).
                values(
                    card_id=new_id,
                    card_file_name=file.filename.split(".")[0],
                    relative_path=RELATIVE
                )
            )

            await db.execute(stmt_2)
            await db.commit()
        else:
            return "Not a single related db recording!"
    except Exception as e:
        return f"Something is wrong! 😂 ERROR: {e}"