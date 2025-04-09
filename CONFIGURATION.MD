
# Configuration Reference

_(Under construction)_

## About Using Environment Variables

It's recommended to use environment variables in the configuration for API keys and other, string value sensitive information. 

To use environment variables, prefix the value in the task node configuration with `env:`. For example, to configure the OpenAI authentication key as an environment variable, one could enter in the task configuration:
```
env:OPEN_AI_AUTHENTICATION_KEY
```

## Logging and Debugging

At the moment, there are two options to get details of the task execution:

1. Set the `debug: true` in the configuration. This will add LLM debug output to the HTTP response.
1. Set `logRequests: true` and/or `logResponses: true` for a task node. This will output the LLM communication to the Liferay log.

## Node Configuration Reference


### Tool Provider

Currently only MCP tool provider configuration is supported.

```
{
  "configuration": {
    "nodes": [
      {
        "toolProvider": {
          "mcp": {}
        }
      }
    ]
  }
}

```

#### MCP

A single MCP tool provider can use multiple clients.

##### MCP tool provider using HTTP transport client

See https://docs.langchain4j.dev/tutorials/mcp/#mcp-transport for the property reference. Timeout is in seconds.

```
{
  "configuration": {
    "nodes": [
      {
        "toolProvider": {
          "mcp": {
            "clients": [
              {
                "transport": {
                  "logRequests": true,
                  "logResponses": true,
                  "sseUrl": "http://example.com:3001/sse",
                  "type": "http",
                  "timeout": 60
                }
              }
            ]
          }
        }
      }
    ]
  }
}

```

##### MCP tool provider using stdio transport client

See https://docs.langchain4j.dev/tutorials/mcp/#mcp-transport for the property reference.


```
{
  "configuration": {
    "nodes": [
      {
        "toolProvider": {
          "mcp": {
            "clients": [
              {
                "transport": {
                  "command": [],
                  "environment": {},
                  "logEvents": true,
                  "type": "stdio"
                }
              }
            ]
          }
        }
      }
    ]
  }
}

```

##### An example of multiple MCP clients

```
{
  "configuration": {
    "nodes": [
      {
        "toolProvider": {
          "mcp": {
            "clients": [
              {
                "transport": {
                  "command": [],
                  "environment": {},
                  "logEvents": true,
                  "type": "stdio"
                }
              },
              {
                "transport": {
                  "logRequests": true,
                  "logResponses": true,
                  "sseUrl": "",
                  "type": "http",
                  "timeout": 60
                }
              }
            ],
            "failIfOneServerFails": false
          }
        }
      }
    ]
  }
}

```