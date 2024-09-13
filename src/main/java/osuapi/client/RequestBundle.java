package osuapi.client;

import lombok.Getter;

@Getter
public final class RequestBundle {
    private RequestProperties properties = RequestProperties.createInstance(1000, 1000);
    private RestTemplate apiRestTemplate;

    public RequestBundle(int readTimeout, int connectTimeout) {
        properties = RequestProperties.createInstance(readTimeout, connectTimeout);
        apiRestTemplate = (new RestTemplateBuilder()).uriTemplateHandler(new DefaultUriBuilderFactory(RequestProperties.getGateway()))
                .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .setConnectTimeout(Duration.ofMillis(properties.getConnectTimeout()))
                .build();
    }

    public void setBundle(int readTimeout, int connectTimeout) {
        properties = RequestProperties(readTimeout, connectTimeout);
        apiRestTemplate = (new RestTemplateBuilder()).uriTemplateHandler(new DefaultUriBuilderFactory(RequestProperties.getGateway()))
                .setReadTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .setConnectTimeout(Duration.ofMillis(properties.getConnectTimeout()))
                .build();
    }
}