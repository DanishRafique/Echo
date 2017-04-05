package in.co.echoindia.echo.Model;

/**
 * Created by Danish Rafique on 03-04-2017.
 */

public class PollDetailsModel {

    private String pollId="";
    private String pollTitle="";
    private String pollImage="";
    private String pollDescription="";
    private String pollOptionOneText="";
    private int pollOptionOneVote;
    private String pollOptionTwoText="";
    private int pollOptionTwoVote;
    private String pollVendor="";
    private String pollVendorLogo="";
    private String pollStartDate="";
    private String pollEndDate="";
    private int pollOptionOneColor;
    private int pollOptionTwoColor;

    public int getPollOptionOneColor() {
        return pollOptionOneColor;
    }

    public void setPollOptionOneColor(int pollOptionOneColor) {
        this.pollOptionOneColor = pollOptionOneColor;
    }

    public int getPollOptionTwoColor() {
        return pollOptionTwoColor;
    }

    public void setPollOptionTwoColor(int pollOptionTwoColor) {
        this.pollOptionTwoColor = pollOptionTwoColor;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }

    public String getPollImage() {
        return pollImage;
    }

    public void setPollImage(String pollImage) {
        this.pollImage = pollImage;
    }

    public String getPollDescription() {
        return pollDescription;
    }

    public void setPollDescription(String pollDescription) {
        this.pollDescription = pollDescription;
    }

    public String getPollOptionOneText() {
        return pollOptionOneText;
    }

    public void setPollOptionOneText(String pollOptionOneText) {
        this.pollOptionOneText = pollOptionOneText;
    }

    public int getPollOptionOneVote() {
        return pollOptionOneVote;
    }

    public void setPollOptionOneVote(int pollOptionOneVote) {
        this.pollOptionOneVote = pollOptionOneVote;
    }

    public String getPollOptionTwoText() {
        return pollOptionTwoText;
    }

    public void setPollOptionTwoText(String pollOptionTwoText) {
        this.pollOptionTwoText = pollOptionTwoText;
    }

    public int getPollOptionTwoVote() {
        return pollOptionTwoVote;
    }

    public void setPollOptionTwoVote(int pollOptionTwoVote) {
        this.pollOptionTwoVote = pollOptionTwoVote;
    }

    public String getPollVendor() {
        return pollVendor;
    }

    public void setPollVendor(String pollVendor) {
        this.pollVendor = pollVendor;
    }

    public String getPollVendorLogo() {
        return pollVendorLogo;
    }

    public void setPollVendorLogo(String pollVendorLogo) {
        this.pollVendorLogo = pollVendorLogo;
    }

    public String getPollStartDate() {
        return pollStartDate;
    }

    public void setPollStartDate(String pollStartDate) {
        this.pollStartDate = pollStartDate;
    }

    public String getPollEndDate() {
        return pollEndDate;
    }

    public void setPollEndDate(String pollEndDate) {
        this.pollEndDate = pollEndDate;
    }



}
