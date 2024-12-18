package jospi.endpoints.async;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import jospi.client.core.InternalOsuApiClient;
import jospi.enums.matches.MatchBundleSort;
import jospi.iterator.AsyncLazyEnumerable;
import jospi.iterator.ExitToken;
import jospi.models.matches.Match;
import jospi.models.matches.MatchBundle;
import jospi.models.matches.MatchesBundle;

public final class MatchesAsync {
    private static final String BASE = "/matches/";

    private final InternalOsuApiClient client;

    protected MatchesAsync(InternalOsuApiClient client) {
        this.client = client;
    }

    public AsyncLazyEnumerable<String, Match[]> getMatches(int limit, MatchBundleSort sort) {
        ExitToken<String> token = new ExitToken<>("");
        Function<ExitToken<String>, CompletableFuture<Match[]>> func = t ->
            CompletableFuture.supplyAsync(() -> {
                MatchesBundle packs = client.getJson(BASE, map -> {
                    map.put("limit", limit);
                    map.put("sort", sort);
                }, MatchesBundle.class);
                token.setNext(packs.getCursorString());
                return packs.getMatches();
            });
        return new AsyncLazyEnumerable<>(func, token);
    }

    public CompletableFuture<MatchBundle> getMatch(int matchId, int before, int after, int limit) {
        return client.getJsonAsync(BASE + matchId, map -> {
                map.put("before", before);
                map.put("after", after);
                map.put("limit", limit);
            }, MatchBundle.class);
    }
}
