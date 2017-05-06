package in.co.echoindia.echo.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import in.co.echoindia.echo.R;

public class EditAccountActivity extends AppCompatActivity {

    TextView changePasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changePasswordTextView=(TextView)findViewById(R.id.edit_account_change_password);
        changePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changePasswordIntent=new Intent(EditAccountActivity.this, ChangePasswordActivity.class);
                startActivity(changePasswordIntent);
            }
        });
    }

}
