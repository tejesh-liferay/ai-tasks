package fi.soveltia.liferay.aitasks.internal.task.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

/**
 * @author Petteri Karttunen
 */
public interface WithMemoryChatAssistant {

	public Result<String> chat(
		@MemoryId String memoryId, @UserMessage String userMessage);

	public TokenStream stream(
		@MemoryId String memoryId, @UserMessage String userMessage);

}