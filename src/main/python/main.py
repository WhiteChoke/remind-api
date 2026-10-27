import uvicorn
import asyncio

from fastapi import FastAPI

from db.engine import engine
from db.model import Base

from endpoints import sentence_generator

from endpoints.storage_service import post_card_route
from endpoints.storage_service import delete_card_route
from endpoints.storage_service import put_card_route
from endpoints.storage_service import patch_card_route


from service.cleanup import cleanup_service

app = FastAPI()

app.include_router(sentence_generator.router)
app.include_router(post_card_route.router)
app.include_router(delete_card_route.router)
app.include_router(put_card_route.router)
app.include_router(patch_card_route.router)

async def startup() -> None:
    async with engine.begin() as session:
        await session.run_sync(Base.metadata.drop_all)
        await session.run_sync(Base.metadata.create_all)

async def asyncio_tasks() -> None:
    t1 = asyncio.create_task(cleanup_service())
    asyncio.gather(t1)

async def main() -> None:

    await startup()
    await asyncio_tasks()

    config = uvicorn.Config(app=app,
                            host="127.0.0.1",
                            port=8000)
    server = uvicorn.Server(config)
    await server.serve()

if __name__ == "__main__":
    asyncio.run(main=main())