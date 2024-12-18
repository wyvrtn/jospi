package jospi.endpoints.async;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.CompletableFuture;

import jospi.client.core.InternalOsuApiClient;
import jospi.client.request.HttpMethod;
import jospi.enums.misc.Ruleset;
import jospi.models.beatmaps.Beatmap;
import jospi.models.beatmaps.BeatmapExtended;
import jospi.models.beatmaps.DifficultyAttributes;
import jospi.models.scores.BeatmapScores;
import jospi.models.scores.Score;
import jospi.models.scores.UserBeatmapScore;

public final class BeatmapsAsync {
    private static final String BASE = "/beatmaps/";

    private final InternalOsuApiClient client;

    public BeatmapsAsync(InternalOsuApiClient client) {
        this.client = client;
    }

    public CompletableFuture<Beatmap> lookupBeatmapChecksum(String checksum) {
        try {
            return lookupBeatmapInternal("checksum=" + URLEncoder.encode(checksum, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(new Beatmap());
    }

    public CompletableFuture<Beatmap> lookupBeatmapFilename(String filename) {
        return lookupBeatmapInternal("filename=" + filename);
    }

    public CompletableFuture<Beatmap> lookupBeatmapId(String id) {
        return lookupBeatmapInternal("id=" + id);
    }

    private CompletableFuture<Beatmap> lookupBeatmapInternal(String query) {
        return client.getJsonAsync(BASE + "lookup?" + query, Beatmap.class);
    }

    public CompletableFuture<UserBeatmapScore> getUserBeatmapScore(int beatmapId, int userId, Ruleset ruleset, String mods) {
        return client.getJsonAsync(BASE + beatmapId + "/scores/users/"
                +userId, map -> {
                    map.put("mode", ruleset);
                    map.put("mods", mods);
                }, UserBeatmapScore.class);
    }

    public CompletableFuture<Score[]> getUserBeatmapScores(int beatmapId, int userId, Ruleset ruleset) {
        return client.getJsonAsync(BASE + beatmapId + "/scores/users/"
                +userId+"/all", map -> {
                    map.put("map", ruleset);
                }, Score[].class);
    }

    public CompletableFuture<BeatmapScores> getBeatmapScores(int beatmapId, Ruleset ruleset, String mods) {
        return client.getJsonAsync(BASE + beatmapId + "/scores", map -> {
                    map.put("mode", ruleset);
                    map.put("mods", mods);
                }, BeatmapScores.class);

    }

    public CompletableFuture<BeatmapExtended[]> getBeatmaps(int[] ids) {
        return client.getJsonAsync("/beatmaps", map -> {
                    for (int id : ids) map.put("ids[]", id);
                }, BeatmapExtended[].class);
    }

    public CompletableFuture<BeatmapExtended> getBeatmap(int id) {
        return client.getJsonAsync(BASE + id, BeatmapExtended.class);
    }

    public CompletableFuture<DifficultyAttributes> getDifficultyAttributes(int id) {
        return client.getJsonAsync(BASE + id + "/attributes", HttpMethod.POST, DifficultyAttributes.class);
    }
}
