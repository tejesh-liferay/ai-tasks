package fi.soveltia.liferay.aitasks.internal.task.node.tool;

import com.liferay.portal.kernel.json.JSONObject;

/**
 * @author Petteri Karttunen
 */
public interface AIToolsProvider {

	public Object getTool(JSONObject jsonObject, String key);

}