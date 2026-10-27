from typing import Annotated
from fastapi import (APIRouter,
                     Form)

from llm.model import generate

router = APIRouter(prefix="/qwen2B", tags=["🤖 LLM PROCESS"])

@router.post("/request")
async def recieve_llm_response(user_message: Annotated[str, Form()]) -> None:
    reply = await generate(user_message)
    return reply