package in.co.echoindia.echo.Model;

/**
 * Created by Danish Rafique on 30-04-2017.
 */

public class PollVoteModel {
    private String pollId = "";
    private String pollVoteOption="";

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getPollVoteOption() {
        return pollVoteOption;
    }

    public void setPollVoteOption(String pollVoteOption) {
        this.pollVoteOption = pollVoteOption;
    }
}
