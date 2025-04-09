/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */

export const ANTHROPIC_CHAT_MODEL = 'anthropicChatModel';
export const ENTRY_POINT_NODE = 'entryPointNode';
export const GEMINI_CHAT_MODEL = 'geminiChatModel';
export const GOOGLE_IMAGEN = 'googleImagen';
export const HUGGING_FACE_CHAT_MODEL = 'huggingFaceChatModel';
export const INPUT_TRIGGER = 'inputTrigger';
export const LIFERAY_SEARCH = 'liferaySearch';
export const MISTRALAI_CHAT_MODEL = 'mistralAIChatModel';
export const OLLAMA_CHAT_MODEL = 'ollamaChatModel';
export const OPENAI_CHAT_MODEL = 'openAIChatModel';
export const OPENAI_IMAGE_MODEL = 'openAIImageModel';
export const WEBHOOK = 'webhook';

export const getTextGenerationNodes = [
  ANTHROPIC_CHAT_MODEL,
  GEMINI_CHAT_MODEL,
  HUGGING_FACE_CHAT_MODEL,
  MISTRALAI_CHAT_MODEL,
  OLLAMA_CHAT_MODEL,
  OPENAI_CHAT_MODEL,
];

export const getImageGenerationNodes = [GOOGLE_IMAGEN, OPENAI_IMAGE_MODEL];

export const getTriggerNodes = [INPUT_TRIGGER];

export const getUtilityNodes = [LIFERAY_SEARCH, WEBHOOK];
