/**
 * @author Louis-Guillaume Durand
 */
import {
  ANTHROPIC_CHAT_MODEL,
  GEMINI_CHAT_MODEL,
  GEMINI_STREAMING_CHAT_MODEL,
  GOOGLE_IMAGEN,
  HUGGING_FACE_CHAT_MODEL,
  LIFERAY_SEARCH,
  MISTRALAI_CHAT_MODEL,
  OLLAMA_CHAT_MODEL,
  OLLAMA_STREAMING_CHAT_MODEL,
  OPENAI_CHAT_MODEL,
  OPENAI_IMAGE_MODEL,
  OPENAI_STREAMING_CHAT_MODEL,
  WEBHOOK,
} from '../constants/AITasksNodeTypesConstants';

export const getDefaultParameters = (nodeType) => {
  if (nodeType === ANTHROPIC_CHAT_MODEL) {
    return {
      apiKey: 'env:ANTHROPIC_API_KEY',
      baseUrl: '',
      memoryMaxMessages: 20,
      modelName: 'claude-3-sonnet-20240229',
      outputParameterName: 'text',
      promptTemplate: '{{input.text}}',
      systemMessage: '',
      temperature: 0.7,
      timeout: 20,
      useChatMemory: true,
    };
  }
  if (nodeType === GEMINI_CHAT_MODEL) {
    return {
      location: 'us-central1',
      memoryMaxMessages: 20,
      modelName: 'gemini-1.5-pro',
      maxOutputTokens: 350,
      outputParameterName: 'text',
      promptTemplate: '{{input.text}}',
      project: 'env:GCLOUD_PROJECT',
      systemMessage: '',
      temperature: 0.7,
      timeout: 20,
      topK: 3,
      topP: 1,
      useChatMemory: true,
    };
  }
  if (nodeType === GEMINI_STREAMING_CHAT_MODEL) {
    return {
      location: 'us-central1',
      memoryMaxMessages: 20,
      modelName: 'gemini-1.5-pro',
      maxOutputTokens: 350,
      promptTemplate: '{{input.text}}',
      project: 'env:GCLOUD_PROJECT',
      systemMessage: '',
      temperature: 0.7,
      timeout: 20,
      topK: 3,
      topP: 1,
      useChatMemory: true,
    };
  }
  if (nodeType === GOOGLE_IMAGEN) {
    return {
      endpoint: 'us-central1-aiplatform.googleapis.com:443',
      imageStyle: 'digitalArt',
      location: '',
      modelName: 'imagegeneration@005',
      outputParameterName: 'image',
      promptTemplate: '{{input.text}}',
      project: 'env:GCLOUD_PROJECT',
      publisher: 'google',
      sampleImageSize: 1536,
      sampleImageStyle: 'digitalArt',
    };
  }
  if (nodeType === HUGGING_FACE_CHAT_MODEL) {
    return {
      accessToken: 'env:HUGGING_FACE_ACCESS_TOKEN',
      baseUrl: '',
      maxNewTokens: '',
      memoryMaxMessages: 20,
      modelName: '',
      outputParameterName: 'text',
      returnFullText: true,
      temperature: 0.7,
      timeout: 20,
      waitForModel: true,
    };
  }
  if (nodeType === LIFERAY_SEARCH) {
    return {
      topK: 3,
      inputParameterName: 'text',
      sxpBlueprintExternalReferenceCode: 'semantic_search',
      taskContextOutputParameterName: 'ragDocuments',
      documentResultField: 'content_{{taskContext.currentLanguageId}}',
    };
  }
  if (nodeType === MISTRALAI_CHAT_MODEL) {
    return {
      apiKey: 'env:MISTRALAI_API_KEY',
      baseUrl: '',
      memoryMaxMessages: 20,
      modelName: 'mistral-small-latest',
      outputParameterName: 'text',
      promptTemplate: '{{input.text}}',
      systemMessage: '',
      temperature: 0.7,
      timeout: 20,
      useChatMemory: true,
    };
  }
  if (nodeType === OLLAMA_CHAT_MODEL) {
    return {
      baseUrl: 'http://localhost:11434',
      memoryMaxMessages: 20,
      modelName: 'llama3.2:1b',
      outputParameterName: 'text',
      promptTemplate: '{{input.text}}',
      systemMessage: '',
      temperature: 0.7,
      useChatMemory: true,
    };
  }
  if (nodeType === OLLAMA_STREAMING_CHAT_MODEL) {
    return {
      baseUrl: 'http://localhost:11434',
      memoryMaxMessages: 20,
      modelName: 'llama3.2:1b',
      promptTemplate: '{{input.text}}',
      systemMessage: '',
      temperature: 0.7,
      useChatMemory: true,
    };
  }
  if (nodeType === OPENAI_CHAT_MODEL) {
    return {
      apiKey: 'env:OPENAI_API_KEY',
      baseUrl: 'https://api.openai.com/v1/',
      memoryMaxMessages: 20,
      modelName: 'gpt-4o-mini',
      outputParameterName: 'text',
      promptTemplate: '{{input.text}}',
      systemMessage: '',
      temperature: 0.7,
      timeout: 20,
      useChatMemory: true,
    };
  }
  if (nodeType === OPENAI_STREAMING_CHAT_MODEL) {
    return {
      apiKey: 'env:OPENAI_API_KEY',
      baseUrl: 'https://api.openai.com/v1/',
      memoryMaxMessages: 20,
      modelName: 'gpt-4o-mini',
      promptTemplate: '{{input.text}}',
      systemMessage: '',
      temperature: 0.7,
      timeout: 20,
      useChatMemory: true,
    };
  }
  if (nodeType === OPENAI_IMAGE_MODEL) {
    return {
      apiKey: '',
      modelName: 'dall-e-2',
      outputParameterName: 'image',
      promptTemplate: '{{input.text}}',
      quality: 'standard',
      responseFormat: 'b64_json',
      style: 'Natural',
    };
  }
  if (nodeType === WEBHOOK) {
    return {
      httpMethod: 'POST',
      taskContextOutputParameterName: 'webhookResponse',
      url: '',
    };
  }
  return {};
};
