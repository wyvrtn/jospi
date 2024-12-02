package jospi.client.resources;

public class OsuApiException extends RuntimeException {
    private static final long serialVersionUID = 8596965465781109261L;

	public OsuApiException(final String message, final Throwable exception) {
		super(message, exception);
	}

	public OsuApiException(final String message) {
		super(message);
	}
}
