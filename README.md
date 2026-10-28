# RemindAPI .python

> Python related project's part. REST API | LLM Response Generation

[![License](https://img.shields.io/badge/license-GNU-blue.svg)](LICENSE)
[![Status](https://img.shields.io/badge/status-active-brightgreen.svg)]()

## Requirements

```bash
# src\main\python
pip install requirements.txt
```

##### OLLAMA
```bash
# Windows:
irm https://ollama.com/install.ps1 | iex

# Linux | MacOS:
curl -fsSL https://ollama.com/install.sh | sh
```

##### IMPORTANT
```bash
# TO NOTICE:
# to use an API, you better not have CPU but GPU with 4GB+ VRAM.
```
## Usage

```bash
cd src\main\python
py main.py
```

## Configuration

in src\main\python, you should create an .env file

##### REQUIRED FIELDS:

| Variable | Description | Default |
|---|---|---|
| `DB_NAME` | db name | `Any` |
| `DB_HOST` | db host | `localhost` |
| `DB_PW` | db password | `Any` |
| `DB_PORT` | db port | `Any` |
| `USER` | user | `postgres / etc.` |

## Directory Structure

```text
python/
├── README.md
├── db/
├── endpoints/
├── llm/
├── service/
├── main.exe
├── main.py
├── requirements.txt
└── LICENSE
```

## Contributing

Contributions, bug reports, and suggestions are welcome. Please open an issue before making significant changes.

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE).

## Contact

- GitHub: https://github.com/Vealke
