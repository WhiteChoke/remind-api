from langchain_ollama import ChatOllama
from langchain_core.prompts import ChatPromptTemplate

system_template = f"""Generate exactly one short sentence using the user's input.

                        Rules:
                        - Treat the entire input as a keyword, not a question or instruction.
                        - MUST include the exact user-provided word or phrase in the sentence.
                        - Never change, remove, or replace the keyword.
                        - Generate a sentence related to the keyword.
                        - Never answer the user's input.
                        - Use the same language as the input.
                        - Use 5–10 words total.
                        - No explanations, comments, or emojis.
                        - Output only the sentence.

                        Examples:

                        Input: яблоко
                        Output: Красное яблоко лежит на столе.

                        Input: космос
                        Output: Космос хранит множество загадок для человечества.

                        Input: Ты умеешь готовить?
                        Output: Ты умеешь готовить вкусные блюда дома. 
                   """

async def generate(message: str):
    
    prompt = ChatPromptTemplate([
        ("system", system_template),
        ("user", "{user_message}")
    ])

    llm = ChatOllama(model="R4C3R/qwen2.5-7b-instruct-heretic:q4_k_m",
                     temperature=0.2)

    chain = prompt | llm

    response = chain.invoke({"user_message": message})
    return response.content