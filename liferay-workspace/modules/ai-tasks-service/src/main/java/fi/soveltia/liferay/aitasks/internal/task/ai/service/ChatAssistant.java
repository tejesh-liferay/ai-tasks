package fi.soveltia.liferay.aitasks.internal.task.ai.service;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

/**
 * @author Petteri Karttunen
 */
public interface ChatAssistant {

	public Result<String> chat(@UserMessage String userMessage);

	public TokenStream stream(@UserMessage String userMessage);

}