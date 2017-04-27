package in.co.echoindia.echo.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import in.co.echoindia.echo.R;
import mehdi.sakout.aboutpage.AboutPage;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View aboutPage = new AboutPage(this)
                .setImage(R.mipmap.icon)
                .setDescription("Feel free to Echo your valuable feedback to us using other social sites")
                .addGroup("Connect with us")
                .addEmail("elmehdi.sakout@gmail.com")
                .addWebsite("http://medyo.github.io/")
                .addFacebook("echo")
                .addTwitter("medyo80")
                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA","Subscribe our channel on YouTube")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addInstagram("echo")
                .create();
        setContentView(aboutPage);
    }
}
