package in.co.echoindia.echo.Model;

/**
 * Created by Danish Rafique on 03-04-2017.
 */

public class PollCommentModel {
    private int pollCommentId;
    private int pollId;
    private String pollCommentText="";
    private String pollCommentUserName="";
    private String pollCommentTime="";
    private String pollCommentDate="";

    public int getPollCommentId() {
        return pollCommentId;
    }

    public void setPollCommentId(int pollCommentId) {
        this.pollCommentId = pollCommentId;
    }

    public int getPollId() {
        return pollId;
    }

    public void setPollId(int pollId) {
        this.pollId = pollId;
    }

    public String getPollCommentText() {
        return pollCommentText;
    }

    public void setPollCommentText(String pollCommentText) {
        this.pollCommentText = pollCommentText;
    }

    public String getPollCommentUserName() {
        return pollCommentUserName;
    }

    public void setPollCommentUserName(String pollCommentUserName) {
        this.pollCommentUserName = pollCommentUserName;
    }

    public String getPollCommentTime() {
        return pollCommentTime;
    }

    public void setPollCommentTime(String pollCommentTime) {
        this.pollCommentTime = pollCommentTime;
    }

    public String getPollCommentDate() {
        return pollCommentDate;
    }

    public void setPollCommentDate(String pollCommentDate) {
        this.pollCommentDate = pollCommentDate;
    }
}
