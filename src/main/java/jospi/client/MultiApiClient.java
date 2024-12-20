package jospi.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jospi.client.authorization.ClientCredentialsGrant;
import jospi.client.core.AbstractApiAuthorization;
import jospi.client.core.OsuApiClient;
import jospi.client.request.HttpClientCreator;
import lombok.Getter;
import lombok.experimental.Delegate;

public final class MultiApiClient<K> {
    @Delegate(excludes = OsuApiClientDelgationExclusions.class)
    private final OsuApiClient client;

    @Getter
    private final ConcurrentMap<K, AbstractApiAuthorization> authorizationInstances;

    public MultiApiClient(int clientId, String clientSecret) {
        this(new ClientCredentialsGrant(clientId, clientSecret));
    }

    public MultiApiClient(AbstractApiAuthorization auth) {
        this(auth, new HttpClientCreator());
    }

    public MultiApiClient(AbstractApiAuthorization auth, HttpClientCreator bundle) {
        client = new OsuApiClient(auth, bundle);
        authorizationInstances = new ConcurrentHashMap<>();
    }

    public void switchInstance(K key) {
        client.updateAuthorization(authorizationInstances.get(key));
    }

    private interface OsuApiClientDelgationExclusions {
        void updateAuthorization(AbstractApiAuthorization newAuth);
    }
}
