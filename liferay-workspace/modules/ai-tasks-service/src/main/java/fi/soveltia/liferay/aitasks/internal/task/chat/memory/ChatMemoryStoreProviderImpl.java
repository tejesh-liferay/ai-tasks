package fi.soveltia.liferay.aitasks.internal.task.chat.memory;

import com.liferay.portal.kernel.util.StringUtil;

import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;

import fi.soveltia.liferay.aitasks.configuration.AITasksConfiguration;
import fi.soveltia.liferay.aitasks.configuration.AITasksConfigurationProvider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Petteri Karttunen
 */
@Component(
	configurationPid = "fi.soveltia.liferay.aitasks.configuration.AITasksConfiguration",
	service = ChatMemoryStoreProvider.class
)
public class ChatMemoryStoreProviderImpl implements ChatMemoryStoreProvider {

	@Override
	public ChatMemoryStore getChatMemoryStore() {
		AITasksConfiguration aiTasksConfiguration =
			_aiTasksConfigurationProvider.getSystemConfiguration();

		if (StringUtil.equals(
				"mapDb", aiTasksConfiguration.chatMemoryProvider())) {

			return new MapDBChatMemoryStore();
		}

		return new InMemoryChatMemoryStore();
	}

	@Reference
	private AITasksConfigurationProvider _aiTasksConfigurationProvider;

}