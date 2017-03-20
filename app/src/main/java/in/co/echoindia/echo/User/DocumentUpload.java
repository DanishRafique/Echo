package in.co.echoindia.echo.User;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import in.co.echoindia.echo.R;

public class DocumentUpload extends AppCompatActivity {
    ImageView imgVoterId,imgAadhaarCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgVoterId=(ImageView)findViewById(R.id.img_voter_id);
        imgAadhaarCard=(ImageView)findViewById(R.id.img_aadhaar_card);
        makeRoundCorners(imgVoterId,R.drawable.img_voter_id);
        makeRoundCorners(imgAadhaarCard,R.drawable.img_aadhaar_card);

    }
    void makeRoundCorners(ImageView imgView , int drawable){
        Bitmap mBitmap = ((BitmapDrawable) getResources().getDrawable(drawable)).getBitmap();
        Bitmap imageRounded = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
        Canvas canvas = new Canvas(imageRounded);
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect((new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight())), 25, 25, mPaint);// Round Image Corner 100 100 100 100
        imgView.setImageBitmap(imageRounded);
    }

}
