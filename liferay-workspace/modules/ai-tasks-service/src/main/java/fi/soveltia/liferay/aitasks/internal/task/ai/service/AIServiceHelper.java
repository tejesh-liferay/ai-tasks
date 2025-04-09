package fi.soveltia.liferay.aitasks.internal.task.ai.service;

import com.liferay.portal.kernel.json.JSONObject;

/**
 * @author Petteri Karttunen
 */
public interface AIServiceHelper {

	public Object createAssistant(
		Class<?> clazz, JSONObject jsonObject, Object model,
		String systemMessage, boolean useChatMemory);

}