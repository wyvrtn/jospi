package jospi.client.core;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import jospi.client.authorization.ClientCredentialsGrant;
import jospi.client.request.HttpMethod;
import jospi.client.request.NetIOUtilities;
import jospi.client.request.RequestBundle;
import jospi.client.resources.Dictionary;
import jospi.endpoints.ApiEndpoints;

public final class OsuApiClient implements NetIOUtilities {
    public final ApiEndpoints endpoints;
    private AbstractApiAuthorizationContainer authorization;
    private final OsuApiClientInternalBlockingStatefulHttpServiceProvider svc;

    public OsuApiClient(final int clientId, final String clientSecret) {
        this(new ClientCredentialsGrant(clientId, clientSecret));
    }
	
    public OsuApiClient(final AbstractApiAuthorization auth) {
        this(auth, new RequestBundle());

    }
	
	public OsuApiClient(final AbstractApiAuthorization auth, final RequestBundle bundle) {
		endpoints = ApiEndpoints.createInstance(this);
		authorization = AbstractApiAuthorizationContainer.newInstance(auth);
		svc = new OsuApiClientInternalBlockingStatefulHttpServiceProvider(bundle, authorization);
	}

	public void updateAuthorization(final AbstractApiAuthorization newAuth) {
		synchronized (this) {
			authorization.setInstance(newAuth);
			ensureAccessToken();
		}
	}

	public void requiresUser() {
		if (authorization.getInstance() instanceof ClientCredentialsGrant) {
			throw new IllegalStateException("The method called must use Authorization Code Grant");
		}
	}

	public void ensureAccessToken() {
		synchronized (this) {
			if (authorization.getInstance().getExpirationDate().isAfter(OffsetDateTime.now())) {
				return;
			}
			if (!authorization.getInstance().isStatus()) {
				authorization.getInstance().authorizationFlow(svc);
			} else {
				authorization.getInstance().refreshAccessToken(svc);
			}
		}
	}

	public <T> CompletableFuture<T> getJsonAsync(final String url) {
		return getJsonAsync(url, HttpMethod.GET);
	}

	public <T> CompletableFuture<T> getJsonAsync(final String url, final HttpMethod method) {
		return CompletableFuture.supplyAsync(() -> getJson(url, method));
	}

	public <T> CompletableFuture<T> getJsonAsync(final String url, final Dictionary<String, Object> queryParams) {
		return getJsonAsync(url, queryParams, HttpMethod.GET);
	}

	public <T> CompletableFuture<T> getJsonAsync(final String url, final Map<String, Object> queryParams) {
		return getJsonAsync(url, queryParams, HttpMethod.GET);
	}

	public <T> CompletableFuture<T> getJsonAsync(final String url, final Dictionary<String, Object> queryParams, final HttpMethod method) {
		return CompletableFuture.supplyAsync(() -> getJson(url, queryParams, method));
	}

	public <T> CompletableFuture<T> getJsonAsync(final String url, final Map<String, Object> queryParams, final HttpMethod method) {
		return CompletableFuture.supplyAsync(() -> getJson(url, queryParams, method));
	}

	public <T> T getJson(final String url, final Dictionary<String, Object> queryParams) {
        return getJson(url + toQueryString(queryParams), HttpMethod.GET);
	}

	public <T> T getJson(final String url, final Map<String, Object> queryParams) {
	    return getJson(url + toQueryString(queryParams), HttpMethod.GET);
	}

	public <T> T getJson(final String url, final Dictionary<String, Object> queryParams, final HttpMethod method) {
        return getJson(url + toQueryString(queryParams), method);
	}

	public <T> T getJson(final String url, final Map<String, Object> queryParams, final HttpMethod method) {
        return getJson(url + toQueryString(queryParams), method);
	}

    public <T> T getJson(final String url) {
        return getJson(url, HttpMethod.GET);
    }

    public <T> T getJson(final String url, final HttpMethod method) {
	    ensureAccessToken();
	    return svc.genericGetJson(url, method);
    }
}
