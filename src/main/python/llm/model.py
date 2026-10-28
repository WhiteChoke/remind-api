from langchain_ollama import ChatOllama
from langchain_core.prompts import ChatPromptTemplate

# ONLY CHANGE IT IF YOU KNOW THE SHIT
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

async def generate(message: str) -> str:

    """
    Choose between those models, depends on whatever you need:

    CPU only / Low VRAM:
        qwen3.5:2b
        qwen2.5:3b-instruct
        qwen3:1.7b

    6GB VRAM:
        gemma3:4b ( includes 140+ languages )
        qwen3.5:4b 
        yi:6b-chat-v1.5-q4_K_M ( might not work well on all the languages )

    Generate function itself, unironically generates the LLM response the user will recieve.\n
    I had listed some of the models to choose from, but if you'd like to experiment with your own, then allright.
    """
    
    prompt = ChatPromptTemplate([
        ("system", system_template),
        ("user", "{user_message}")
    ])

    # Replace the "model" arg to switch it
    llm = ChatOllama(model="R4C3R/qwen2.5-7b-instruct-heretic:q4_k_m",
                     temperature=0.2)

    chain = prompt | llm

    response = chain.invoke({"user_message": message})
    return response.content