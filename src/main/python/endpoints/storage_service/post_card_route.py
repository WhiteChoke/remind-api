from sqlalchemy import select
from pathlib import Path
from typing import Annotated

from sqlalchemy.ext.asyncio import AsyncSession

from dataclasses import dataclass, asdict

from fastapi import (APIRouter,
                     UploadFile,
                     File,
                     Form,
                     Depends)

from db.engine import localSession
from db.model import Cards

router = APIRouter(prefix="/storage", 
                   tags=["✨ POST"])

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

@router.post("/card/create")
async def create_new_card(file: Annotated[UploadFile, File()],
                          type: Annotated[str, Form()],
                          id: Annotated[int, Form()],
                          db: SessionDep):
    try:

        ft = file.content_type.split("/")[-1]
        fs = file.content_type.split("/")[0]

        if await compare(file_type=ft,
                        type=type): 

            stmt = (
                select(Cards).
                where(Cards.card_id == id)
            )
            value = await db.scalar(stmt)

            if not value:
                file_info: str = f"filename: {file.filename.split(".")[0]}\ntype: {ft}\nsort: {fs}"
                
                Path("../../../data").mkdir(exist_ok=True)
                Path(f"../../../data/file{id}").write_text(file_info,
                                                           encoding="utf-8")
                base = Path("../../../")
                FILE = Path(f"../../../data/file{id}")

                relative_file_path = FILE.relative_to(base)
                RELATIVE = str(relative_file_path).replace("\\", '/') 

                dto = ItemDTO(
                    card_id=id,
                    card_file_name=file.filename.split(".")[0],
                    relative_path=RELATIVE
                )
                card_dto = Cards(**asdict(dto))

                db.add(card_dto)
                await db.commit()
                
                return True
            else:
                return False
        else: 
            return False
    except Exception as e:
        return f"Something is wrong! 😂 ERROR: {e}"