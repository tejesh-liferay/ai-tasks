package fi.soveltia.liferay.aitasks.internal.task.response;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;

import dev.langchain4j.service.TokenStream;

import fi.soveltia.liferay.aitasks.internal.task.node.util.ExecutionTraceUtil;
import fi.soveltia.liferay.aitasks.task.response.AITaskTokenStreamHandler;

import java.util.function.Consumer;

/**
 * @author Petteri Karttunen
 */
public class AITaskTokenStreamHandlerImpl implements AITaskTokenStreamHandler {

	public AITaskTokenStreamHandlerImpl(
		String nodeId, String systemMessage, TokenStream tokenStream,
		boolean trace, String userMessage) {

		_nodeId = nodeId;
		_systemMessage = systemMessage;
		_tokenStream = tokenStream;
		_trace = trace;
		_userMessage = userMessage;
	}

	@Override
	public AITaskTokenStreamHandler onCompleteResponse(
		Consumer<String> consumer) {

		_tokenStream.onCompleteResponse(
			chatResponse -> {
				if (!_trace) {
					return;
				}

				consumer.accept(
					String.valueOf(
						JSONFactoryUtil.createJSONObject(
							HashMapBuilder.<String, Object>put(
								_nodeId,
								HashMapBuilder.<String, Object>put(
									"response", chatResponse.toString()
								).put(
									"systemMessage", _systemMessage
								).put(
									"userMessage", _userMessage
								).putAll(
									ExecutionTraceUtil.getExecutionTrace(
										chatResponse.finishReason(),
										chatResponse.tokenUsage())
								).build()
							).build())));
			});

		return this;
	}

	@Override
	public AITaskTokenStreamHandler onError(Consumer<Throwable> consumer) {
		_tokenStream.onError(consumer);

		return this;
	}

	@Override
	public AITaskTokenStreamHandler onPartialResponse(
		Consumer<String> consumer) {

		_tokenStream.onPartialResponse(consumer);

		return this;
	}

	@Override
	public void start() {
		_tokenStream.start();
	}

	private final String _nodeId;
	private final String _systemMessage;
	private final TokenStream _tokenStream;
	private final boolean _trace;
	private final String _userMessage;

}