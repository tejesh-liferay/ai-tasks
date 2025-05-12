# Using Conditions

Classifies the given text and chat through different personas based on the classification.


![Using Conditions](./using-conditions.png "Using Conditions")

## Requirements

* Local Ollama. See [installation instructions](https://ollama.com/)

## Installation

1. Import the [AI Task](./using-conditions.json)
1. Test in the chat preview. Try with something positive and with something negative and notice the difference in the tone of the response.



## Usage Example through REST API

```
curl -X 'POST' \
  'http://localhost:8080/o/ai-tasks/v1.0/generate/using-conditions' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -u 'test@liferay.com:test' \
  -d '{
  "input": {"text": "This is a bad day"}
}'
```