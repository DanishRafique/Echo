package in.co.echoindia.echo.Model;

/**
 * Created by Danish Rafique on 05-05-2017.
 */

public class NotificationModel {
    private String postID = "";
    private String notificationID = "";
    private String notificationBody = "";
    private String notificationImage = "";
    private String notificationTitle = "";
    private String notificationMessage = "";
    private String notificationTime = "";
    private String notificationDate = "";
    private String notificationPostType = "";
    private String notificationSeen="";

    public String getNotificationSeen() {
        return notificationSeen;
    }

    public void setNotificationSeen(String notificationSeen) {
        this.notificationSeen = notificationSeen;
    }

    public String getNotificationPostType() {
        return notificationPostType;
    }

    public void setNotificationPostType(String notificationPostType) {
        this.notificationPostType = notificationPostType;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(String notificationBody) {
        this.notificationBody = notificationBody;
    }

    public String getNotificationImage() {
        return notificationImage;
    }

    public void setNotificationImage(String notificationImage) {
        this.notificationImage = notificationImage;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }
}
