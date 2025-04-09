package fi.soveltia.liferay.aitasks.internal.task.util;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import fi.soveltia.liferay.aitasks.task.context.AITaskContext;
import fi.soveltia.liferay.aitasks.task.context.AITaskContextParameter;

import java.util.Map;

/**
 * @author Petteri Karttunen
 */
public class TaskContextUtil {

	public static String replacePlaceHolderVariables(
		AITaskContext aiTaskContext, String value) {

		Map<String, AITaskContextParameter> aiTaskContextParameters =
			aiTaskContext.getAITaskContextParameters();

		for (Map.Entry<String, AITaskContextParameter> entry :
				aiTaskContextParameters.entrySet()) {

			AITaskContextParameter aiTaskContextParameter = entry.getValue();

			value = StringUtil.replace(
				value,
				StringBundler.concat("{{taskContext.", entry.getKey(), "}}"),
				aiTaskContextParameter.getStringValue());
		}

		return value;
	}

}