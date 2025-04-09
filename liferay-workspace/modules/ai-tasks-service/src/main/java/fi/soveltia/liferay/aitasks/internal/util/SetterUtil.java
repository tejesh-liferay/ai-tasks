
package fi.soveltia.liferay.aitasks.internal.util;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.time.Duration;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Petteri Karttunen
 */
public class SetterUtil {

	public static void setNotBlankString(
		Consumer<String> consumer, String value) {

		if (!Validator.isBlank(value)) {
			if (value.startsWith("env:")) {
				String[] valueParts = value.split(":");

				value = System.getenv(StringUtil.trim(valueParts[1]));
			}

			consumer.accept(value);
		}
	}

	public static void setNotNullBoolean(
		Consumer<Boolean> consumer, JSONObject jsonObject, String key) {

		if (jsonObject.has(key)) {
			consumer.accept(jsonObject.getBoolean(key));
		}
	}

	public static void setNotNullDouble(
		Consumer<Double> consumer, Double value) {

		if ((value != null) && !value.isNaN()) {
			consumer.accept(value.doubleValue());
		}
	}

	public static void setNotNullDouble(
		Consumer<Double> consumer, JSONObject jsonObject, String key) {

		if (jsonObject.has(key)) {
			consumer.accept(jsonObject.getDouble(key));
		}
	}

	public static void setNotNullDoubleAsFloat(
		Consumer<Float> consumer, Double value) {

		if (value != null) {
			consumer.accept(value.floatValue());
		}
	}

	public static void setNotNullDoubleAsFloat(
		Consumer<Float> consumer, JSONObject jsonObject, String key) {

		if (jsonObject.has(key)) {
			Double value = jsonObject.getDouble(key);

			consumer.accept(value.floatValue());
		}
	}

	public static void setNotNullDurationOfSeconds(
		Consumer<Duration> consumer, JSONObject jsonObject, String key) {

		if (jsonObject.has(key)) {
			consumer.accept(Duration.ofSeconds(jsonObject.getInt(key)));
		}
	}

	public static void setNotNullInteger(
		Consumer<Integer> consumer, Integer value) {

		if (value != null) {
			consumer.accept(value);
		}
	}

	public static void setNotNullInteger(
		Consumer<Integer> consumer, JSONObject jsonObject, String key) {

		if (jsonObject.has(key)) {
			consumer.accept(jsonObject.getInt(key));
		}
	}

	public static void setNotNullJSONArrayAsStringList(
		Consumer<List<String>> consumer, JSONObject jsonObject, String key) {

		JSONArray jsonArray = jsonObject.getJSONArray(key);

		if (jsonArray != null) {
			consumer.accept(JSONUtil.toStringList(jsonArray));
		}
	}

	public static void setNotNullJSONObjectAsStringMap(
		Consumer<Map<String, String>> consumer, JSONObject jsonObject1,
		String key) {

		JSONObject jsonObject2 = jsonObject1.getJSONObject(key);

		if (jsonObject2 != null) {
			consumer.accept(JSONUtil.toStringMap(jsonObject2));
		}
	}

	public static void setNotNullLong(
		Consumer<Long> consumer, JSONObject jsonObject, String key) {

		if (jsonObject.has(key)) {
			consumer.accept(jsonObject.getLong(key));
		}
	}

	public static void setNotNullLong(Consumer<Long> consumer, Long value) {
		if (value != null) {
			consumer.accept(value);
		}
	}

}