import asyncio

from pathlib import Path
from sqlalchemy import select

from db.engine import localSession
from db.model import Cards

folder = Path("../../../data")

async def timer(time: int) -> None:
    for _ in range(time):
        await asyncio.sleep(1)

async def cleanup_service() -> None:
    try:
        for file in folder.iterdir():
            if file.is_file():
                try:
                    text = file.read_text(encoding="utf-8").splitlines()
                    extracted = text[0].split(":")[1].replace(" ", "")
                    
                    async with localSession() as db:
                        filename = file.stem
                        print(f"pair 1: {filename}")
                        stmt = (
                            select(Cards).
                            where(Cards.card_file_name == extracted)
                        )
                        value = await db.scalar(stmt)
                        isinstance(value.card_file_name, Cards)
                except Exception:
                    file.unlink()
                    continue
    except Exception:
        pass

    await timer(1800)
    await cleanup_service()

if __name__ == "__main__":
    asyncio.run(cleanup_service())