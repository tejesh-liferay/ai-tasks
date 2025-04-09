package fi.soveltia.liferay.aitasks.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Petteri Karttunen
 */
@ExtendedObjectClassDefinition(
	category = "ai-tasks", scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	description = "ai-tasks-configuration-description",
	id = "fi.soveltia.liferay.aitasks.configuration.AITasksConfiguration",
	localization = "content/Language", name = "ai-tasks-configuration-name"
)
public interface AITasksConfiguration {

	@Meta.AD(
		deflt = "mapDb", name = "chat-memory-provider",
		optionLabels = "InMemory,MapDB", optionValues = "inMemory,mapDb",
		required = false
	)
	public String chatMemoryProvider();

	@Meta.AD(
		deflt = "604800", name = "task-node-cache-timeout", required = false
	)
	public int taskNodeCacheTimeout();

}