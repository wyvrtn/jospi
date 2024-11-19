package jospi.models.discussions;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiscussionVoters {
	
	@JsonProperty("down")
	private int[] down;
	
	@JsonProperty("up")
	private int[] up;
}