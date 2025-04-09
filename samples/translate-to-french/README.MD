# Translate to French

An AI Task translating the given text to French.


![Translate to French](./translate-to-french.png "Translate to French")

## Requirements

* OpenAI API account set up

## Installation

1. Set up OPENAI_API_KEY environment variable
1. Import the [AI Task](./translate-to-french.json)
1. Test in the chat preview


## Usage Example through REST API

```
curl -X 'POST' \
  'http://localhost:8080/o/ai-tasks/v1.0/generate/translate-to-french' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -u 'test@liferay.com:test' \
  -d '{
  "input": {"text": "Hello world"}
}'
```