package jospi.client.core;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public final class AbstractApiAuthorizationContainer {
    private AbstractApiAuthorization instance;

    private AbstractApiAuthorizationContainer(
            final AbstractApiAuthorization authorization) {
        this.instance = authorization;
    }

    protected static AbstractApiAuthorizationContainer newInstance(
            final AbstractApiAuthorization authorization) {
        return new AbstractApiAuthorizationContainer(authorization);
    }
}
