from typing import Annotated
from fastapi import (APIRouter,
                     Form)

from llm.model import generate

router = APIRouter(prefix="/model", tags=["🤖 LLM"])

@router.post("/request")
async def recieve_llm_response(user_message: Annotated[str, Form()]) -> str:
    """
    Returns the LLM response\n
    by checking out the llm/model.py, you can notice how to modify the response.
    """
    reply: str = await generate(user_message)
    return reply