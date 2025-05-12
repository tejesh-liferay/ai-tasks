
package fi.soveltia.liferay.aitasks.internal.task.node.type.google;

import com.liferay.portal.kernel.json.JSONObject;

import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.vertexai.HarmCategory;
import dev.langchain4j.model.vertexai.SafetyThreshold;
import dev.langchain4j.model.vertexai.SchemaHelper;
import dev.langchain4j.model.vertexai.VertexAiGeminiStreamingChatModel;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseStreamingChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.task.node.type.StreamingChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=geminiStreamingChatModel",
	service = AITaskNode.class
)
public class GeminiStreamingChatModelAITaskNode
	extends BaseStreamingChatModelAITaskNode
	implements StreamingChatModelAITaskNode {

	@Override
	public StreamingChatLanguageModel getStreamingChatLanguageModel(
		JSONObject jsonObject) {

		VertexAiGeminiStreamingChatModel.VertexAiGeminiStreamingChatModelBuilder
			builder = VertexAiGeminiStreamingChatModel.builder();

		SetterUtil.setNotNullJSONArrayAsStringList(
			builder::allowedFunctionNames, jsonObject, "allowedFunctionNames");
		SetterUtil.setNotBlankString(
			builder::location, jsonObject.getString("location"));
		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logRequests");
		SetterUtil.setNotNullBoolean(
			builder::logRequests, jsonObject, "logResponses");
		SetterUtil.setNotNullInteger(
			builder::maxOutputTokens, jsonObject, "maxOutputTokens");
		SetterUtil.setNotBlankString(
			builder::modelName, jsonObject.getString("modelName"));
		SetterUtil.setNotBlankString(
			builder::project, jsonObject.getString("project"));
		SetterUtil.setNotBlankString(
			builder::responseMimeType,
			jsonObject.getString("responseMimeType"));

		JSONObject responseSchemaJSONObject = jsonObject.getJSONObject(
			"responseSchema");

		if (responseSchemaJSONObject != null) {
			builder.responseSchema(
				SchemaHelper.fromJsonSchema(
					responseSchemaJSONObject.toString()));
		}

		JSONObject safetySettingsJSONObject = jsonObject.getJSONObject(
			"safetySettings");

		if (safetySettingsJSONObject != null) {
			builder.safetySettings(_toSafetySettings(safetySettingsJSONObject));
		}

		SetterUtil.setNotNullDoubleAsFloat(
			builder::temperature, jsonObject, "temperature");
		SetterUtil.setNotNullInteger(builder::topK, jsonObject, "topK");
		SetterUtil.setNotNullDoubleAsFloat(builder::topP, jsonObject, "topP");
		SetterUtil.setNotNullBoolean(
			builder::useGoogleSearch, jsonObject, "useGoogleSearch");
		SetterUtil.setNotBlankString(
			builder::vertexSearchDatastore,
			jsonObject.getString("vertexSearchDatastore"));

		return builder.build();
	}

	private Map<HarmCategory, SafetyThreshold> _toSafetySettings(
		JSONObject jsonObject) {

		Map<HarmCategory, SafetyThreshold> safetySettings = new HashMap<>();

		for (String key : jsonObject.keySet()) {
			safetySettings.put(
				HarmCategory.valueOf(key), SafetyThreshold.valueOf(key));
		}

		return safetySettings;
	}

}