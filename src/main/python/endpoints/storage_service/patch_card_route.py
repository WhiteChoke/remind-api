from sqlalchemy import (select,
                        update)
from pathlib import Path
from typing import Annotated

from sqlalchemy.ext.asyncio import AsyncSession

from fastapi import (APIRouter,
                     Form,
                     Depends)

from db.engine import localSession
from db.model import Cards

router = APIRouter(prefix="/storage", 
                   tags=["⚙️ PATCH"])

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

@router.patch("/card/patch/id")
async def update_card_id(id: Annotated[int, Form()],
                         new_id: Annotated[int, Form()],
                         db: SessionDep):
    try:
            stmt = (
                select(Cards).
                where(Cards.card_id == id)
            )

            value = await db.scalar(stmt)

            if value:
                DATA_DIR = Path(__file__).resolve().parents[5] / "data"

                #LOAD
                old_path = DATA_DIR / f"file{id}"
                new_path = DATA_DIR / f"file{new_id}"
                #SAVE
                text = old_path.read_text(encoding="utf-8").splitlines()
                #DELETE
                old_path.unlink()

                # Somehow we've got LSD from those action words....

                file_info: str = "\n".join(text)
                new_path.write_text(file_info)
                RELATIVE = new_path.relative_to(DATA_DIR.parent).as_posix()
                
                to_exe = (
                    update(Cards).
                    where(Cards.card_id == id).
                    values(card_id=new_id,
                           relative_path=RELATIVE)
                )

                await db.execute(to_exe)
                await db.commit()
                
                return True
            else:
                return False
    except Exception as e:
        return f"Something is wrong! 😂 ERROR: {e}"

@router.patch("/card/patch/name")
async def update_card_name(id: Annotated[int, Form()],
                           name: Annotated[str, Form()],
                           db: SessionDep):
    try:
            stmt = (
                select(Cards).
                where(Cards.card_id == id)
            )

            value = await db.scalar(stmt)

            if value:
                DATA_DIR = Path(__file__).resolve().parents[5] / "data"

                path = DATA_DIR / f"file{id}"
                text = path.read_text(encoding="utf-8").splitlines()
                path.unlink()
                
                text.pop(0)
                text.insert(0, f"filename: {name}")

                file_info: str = "\n".join(text)
                path.write_text(file_info)
                
                to_exe = (
                    update(Cards).
                    where(Cards.card_id == id).
                    values(card_file_name=name)
                )

                await db.execute(to_exe)
                await db.commit()
                
                return True
            else:
                return False
    except Exception as e:
        return f"Something is wrong! 😂 ERROR: {e}"

@router.patch("/card/patch/path")
async def update_card_path(id: Annotated[int, Form()],
                           path: Annotated[str, Form()],
                           db: SessionDep):
    try:
            stmt = (
                select(Cards).
                where(Cards.card_id == id)
            )
            value = await db.scalar(stmt)

            if value:
                to_exe = (
                    update(Cards).
                    where(Cards.card_id == id).
                    values(relative_path=path)
                )

                await db.execute(to_exe)
                await db.commit()
                
                return True
            else:
                return False
    except Exception as e:
        return f"Something is wrong! 😂 ERROR: {e}"