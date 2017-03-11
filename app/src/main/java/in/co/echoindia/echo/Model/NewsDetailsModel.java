package in.co.echoindia.echo.Model;

import java.io.Serializable;

/**
 * Created by Danish Rafique on 12-03-2017.
 */

public class NewsDetailsModel implements Serializable {
    private String newsTitle="";
    private String newsImage="";
    private String newsDescription="";
    private String newsVendor="";
    private String newsVendorLogo="";
    private String newsTimeline="";
    private Boolean newsHeart;

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getNewsVendor() {
        return newsVendor;
    }

    public void setNewsVendor(String newsVendor) {
        this.newsVendor = newsVendor;
    }

    public String getNewsVendorLogo() {
        return newsVendorLogo;
    }

    public void setNewsVendorLogo(String newsVendorLogo) {
        this.newsVendorLogo = newsVendorLogo;
    }

    public String getNewsTimeline() {
        return newsTimeline;
    }

    public void setNewsTimeline(String newsTimeline) {
        this.newsTimeline = newsTimeline;
    }

    public Boolean getNewsHeart() {
        return newsHeart;
    }

    public void setNewsHeart(Boolean newsHeart) {
        this.newsHeart = newsHeart;
    }

}
