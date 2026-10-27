from pydantic_settings import (BaseSettings,
                               SettingsConfigDict)

class Settings(BaseSettings):

    DB_NAME: str
    DB_HOST: str
    DB_PW: str
    DB_PORT: int
    USER: str

    @property
    def get_DB_ASYNC(self):
        return f"postgresql+asyncpg://{self.USER}:{self.DB_PW}@{self.DB_HOST}:{self.DB_PORT}/{self.DB_NAME}"

    model_config = SettingsConfigDict(env_file=".env", extra="ignore")

settings = Settings()