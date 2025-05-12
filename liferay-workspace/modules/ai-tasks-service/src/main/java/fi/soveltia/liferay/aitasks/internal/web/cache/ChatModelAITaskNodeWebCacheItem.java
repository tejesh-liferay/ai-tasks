package fi.soveltia.liferay.aitasks.internal.web.cache;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.webcache.WebCacheItem;
import com.liferay.portal.kernel.webcache.WebCachePoolUtil;

import fi.soveltia.liferay.aitasks.configuration.AITasksConfiguration;
import fi.soveltia.liferay.aitasks.internal.task.ai.service.ChatAssistant;
import fi.soveltia.liferay.aitasks.internal.task.ai.service.WithMemoryChatAssistant;

/**
 * @author Petteri Karttunen
 */
public class ChatModelAITaskNodeWebCacheItem implements WebCacheItem {

	public static Object get(
		AITasksConfiguration aiTasksConfiguration, Object object,
		String chatMemoryId, String id, String userMessage) {

		try {
			return WebCachePoolUtil.get(
				StringBundler.concat(
					ChatModelAITaskNodeWebCacheItem.class.getName(),
					StringPool.POUND, chatMemoryId, StringPool.POUND, id,
					StringPool.POUND, userMessage),
				new ChatModelAITaskNodeWebCacheItem(
					aiTasksConfiguration, object, chatMemoryId, userMessage));
		}
		catch (Exception exception) {
			if (_log.isDebugEnabled()) {
				_log.debug(exception);
			}

			return null;
		}
	}

	public ChatModelAITaskNodeWebCacheItem(
		AITasksConfiguration aiTasksConfiguration, Object object,
		String chatMemoryId, String userMessage) {

		_aiTasksConfiguration = aiTasksConfiguration;
		_object = object;
		_chatMemoryId = chatMemoryId;
		_userMessage = userMessage;
	}

	@Override
	public Object convert(String key) {
		try {
			Object result = null;

			if (_object instanceof ChatAssistant) {
				ChatAssistant chatAssistant = (ChatAssistant)_object;

				result = chatAssistant.chat(_userMessage);
			}
			else if (_object instanceof WithMemoryChatAssistant) {
				WithMemoryChatAssistant withMemoryChatAssistant =
					(WithMemoryChatAssistant)_object;

				result = withMemoryChatAssistant.chat(
					_chatMemoryId, _userMessage);
			}

			if (result == null) {
				throw new RuntimeException("Result is null");
			}

			return result;
		}
		catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public long getRefreshTime() {
		return _aiTasksConfiguration.taskNodeCacheTimeout();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ChatModelAITaskNodeWebCacheItem.class);

	private final AITasksConfiguration _aiTasksConfiguration;
	private final String _chatMemoryId;
	private final Object _object;
	private final String _userMessage;

}