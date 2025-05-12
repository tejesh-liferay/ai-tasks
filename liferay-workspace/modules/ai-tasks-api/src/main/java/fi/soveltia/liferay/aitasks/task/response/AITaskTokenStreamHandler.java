package fi.soveltia.liferay.aitasks.task.response;

import java.util.function.Consumer;

/**
 * @author Petteri Karttunen
 */
public interface AITaskTokenStreamHandler {

	public AITaskTokenStreamHandler onCompleteResponse(
		Consumer<String> consumer);

	public AITaskTokenStreamHandler onError(Consumer<Throwable> consumer);

	public AITaskTokenStreamHandler onPartialResponse(
		Consumer<String> consumer);

	public void start();

}