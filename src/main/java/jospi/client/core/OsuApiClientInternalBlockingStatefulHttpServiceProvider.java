package jospi.client.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jospi.client.authorization.StatefulHttpServiceProvider;
import jospi.client.request.HttpMethod;
import jospi.client.request.HttpRequest;
import jospi.client.request.RequestBundle;
import jospi.models.authorization.ApiAuthorizationResponse;
import jospi.models.authorization.AuthorizationCodeResponse;

public final class OsuApiClientInternalBlockingStatefulHttpServiceProvider extends StatefulHttpServiceProvider {
    private final CloseableHttpClient httpClient;
	private final AbstractApiAuthorizationContainer authorization;

    protected OsuApiClientInternalBlockingStatefulHttpServiceProvider(RequestBundle bundle, AbstractApiAuthorizationContainer auth) {
		this.httpClient = bundle.getHttpClient();
		this.authorization = auth;
	}

	protected AuthorizationCodeResponse exchangeCode(String authBody) {
		return Objects.requireNonNull(requestNewToken(authBody), "An error occured while exchanging code for an access token. (response is null)");
	}

	protected <T extends ApiAuthorizationResponse> T requestNewToken(String authBody) {
		HttpRequest request = new HttpRequest(HttpMethod.POST, buildUri(REQTOKEN));
		request.setEntity(new StringEntity(authBody, ContentType.APPLICATION_FORM_URLENCODED));
		request.setHeader(HttpHeaders.ACCEPT, "application/json");
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		T response = simpleJsonRequest(request);
		return Objects.requireNonNull(response, "An error occured while requesting a new access token. (response is null)");
	}

	protected <T> T genericGetJson(String url, HttpMethod method) {
		HttpRequest request = new HttpRequest(method, buildUri(ROOT, url));
		request.setHeader(HttpHeaders.ACCEPT, "application/json");
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + authorization.getInstance().getAccessToken());
		T response = simpleJsonRequest(request);
		return Objects.requireNonNull(response, "null object response");
	}

	private <T> T simpleJsonRequest(HttpRequest request) {
		return simpleJsonRequest(request, httpResponse -> {
			String content = readInputStream(httpResponse.getEntity().getContent());
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(content, new TypeReference<T>(){});
		});
	}

	private <T> T simpleJsonRequest(HttpRequest request, HttpClientResponseHandler<? extends T> responseHandler) {
		T response = null;
		try {
			response = httpClient.execute(request, responseHandler);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	private String buildUri(String... pathArgs) {
		StringBuilder sb = new StringBuilder(GATEWAY);
		for (String arg : pathArgs) sb.append(arg);
		return new String(sb);
	}
	
	private String readInputStream(InputStream inputStream) {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			for (int length; (length = inputStream.read(buffer)) != -1;) {
				result.write(buffer, 0, length);
			}
			return result.toString(StandardCharsets.UTF_8.name());
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return "";
	}
}