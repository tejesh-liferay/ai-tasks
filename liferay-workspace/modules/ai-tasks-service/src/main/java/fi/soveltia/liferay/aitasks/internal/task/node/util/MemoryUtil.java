package fi.soveltia.liferay.aitasks.internal.task.node.util;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;

import fi.soveltia.liferay.aitasks.task.context.AITaskContext;

/**
 * @author Petteri Karttunen
 */
public class MemoryUtil {

	public static String getMemoryId(
		AITaskContext aiTaskContext, String nodeId) {

		return getMemoryId(
			aiTaskContext.getAITaskExternalReferenceCode(),
			aiTaskContext.getCompanyId(), nodeId, aiTaskContext.getUserId());
	}

	public static String getMemoryId(
		String aiTaskExternalReferenceCode, long companyId, String nodeId,
		long userId) {

		return StringBundler.concat(
			companyId, StringPool.POUND, userId, StringPool.POUND,
			aiTaskExternalReferenceCode, StringPool.POUND, nodeId);
	}

}