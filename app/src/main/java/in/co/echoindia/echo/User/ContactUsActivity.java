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
                .addEmail("echoindiaconnect@gmail.com")
                .addWebsite("http://www.github.com/DanishRafique/Echo/")
                .addFacebook("echo.connect.7")
                .addTwitter("echo_connect")
                .addYoutube("UCwiScH1HLXS01PPyyrL_JDAc","Subscribe our channel on YouTube")
                .addPlayStore("")
                .addInstagram("echoconnect")
                .create();
        setContentView(aboutPage);
    }
}
