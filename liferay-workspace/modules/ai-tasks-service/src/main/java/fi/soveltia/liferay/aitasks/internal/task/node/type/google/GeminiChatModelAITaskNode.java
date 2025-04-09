
package fi.soveltia.liferay.aitasks.internal.task.node.google;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.*;

import fi.soveltia.liferay.aitasks.internal.task.node.BaseChatModelAITaskNode;
import fi.soveltia.liferay.aitasks.internal.util.SetterUtil;
import fi.soveltia.liferay.aitasks.spi.task.node.AITaskNode;
import fi.soveltia.liferay.aitasks.task.node.AITaskNodeInformation;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	property = "ai.task.node.type=geminiChatModel", service = AITaskNode.class
)
public class GeminiChatModelAITaskNode
	extends BaseChatModelAITaskNode implements AITaskNode {

	@Override
	public AITaskNodeInformation getAITaskNodeInformation() {
		return new AITaskNodeInformation("geminiChatModel", "input");
	}

	protected ChatLanguageModel getChatLanguageModel(JSONObject jsonObject, boolean stream) {

		if (stream) {


		}

		VertexAiGeminiStreamingChatModel.VertexAiGeminiStreamingChatModelBuilder builder =
				VertexAiGeminiStreamingChatModel.builder();

/*		VertexAiGeminiChatModel.VertexAiGeminiChatModelBuilder builder =
			VertexAiGeminiChatModel.builder();
*/

		JSONArray allowedFunctionNamesJSONArray = jsonObject.getJSONArray(
				"allowedFunctionNames");

		if (allowedFunctionNamesJSONArray != null) {
			builder.allowedFunctionNames(
				JSONUtil.toStringList(allowedFunctionNamesJSONArray));
		}

		SetterUtil.setNotBlankString(
			builder::location, jsonObject.getString("location"));
		SetterUtil.setNotNullBoolean(
				builder::logRequests, jsonObject, "logRequests");
		SetterUtil.setNotNullBoolean(
				builder::logRequests, jsonObject, "logResponses");
		SetterUtil.setNotNullInteger(
			builder::maxOutputTokens, jsonObject, "maxOutputTokens");
		SetterUtil.setNotNullInteger(
			builder::maxRetries, jsonObject, "maxRetries");
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

		JSONObject safetySettingsJSONObject = jsonObject.getJSONObject("safetySettings");

		if (safetySettingsJSONObject != null) {
			builder.safetySettings(
				_toSafetySettings(safetySettingsJSONObject));
		}

		SetterUtil.setNotNullDoubleAsFloat(
			builder::temperature, jsonObject, "temperature");
		SetterUtil.setNotNullInteger(builder::topK, jsonObject, "topK");
		SetterUtil.setNotNullDoubleAsFloat(builder::topP, jsonObject, "topP");
		SetterUtil.setNotNullBoolean(builder::useGoogleSearch, jsonObject, "useGoogleSearch");
		SetterUtil.setNotBlankString( builder::vertexSearchDatastore,
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