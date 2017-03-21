package in.co.echoindia.echo.User;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import in.co.echoindia.echo.R;
import in.co.echoindia.echo.Utils.AppUtil;
import in.co.echoindia.echo.Utils.MarshMallowPermission;

public class DocumentUploadActivity extends AppCompatActivity {
    ImageView imgVoterId,imgAadhaarCard;
    Button btnChooseVoterId,btnUploadVoterId,btnChooseAadhaarCard,btnUploadAadhaarCard;
    Dialog chooseImage;
    TextView chooseImageCamera , chooseImageGallery;
    public static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private Uri imageToUploadUri;
    String encodedImage;
    byte[] byteArray;
    ImageView imageView;
    Button btnUpload;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private static final String LOG_TAG =  DocumentUploadActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgVoterId=(ImageView)findViewById(R.id.img_voter_id);
        imgAadhaarCard=(ImageView)findViewById(R.id.img_aadhaar_card);
        btnChooseVoterId=(Button)findViewById(R.id.btn_choose_voter_id);
        btnUploadVoterId=(Button)findViewById(R.id.btn_upload_voter_id);
        btnChooseAadhaarCard=(Button)findViewById(R.id.btn_choose_aadhaar_card);
        btnUploadAadhaarCard=(Button)findViewById(R.id.btn_upload_aadhaar_card);
        sharedpreferences = AppUtil.getAppPreferences(this);
        editor = sharedpreferences.edit();

        btnChooseVoterId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChooseImageDialog();
                editor.putString("imageType","Voter");
                editor.commit();
            }
        });
        btnChooseAadhaarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChooseImageDialog();
                editor.putString("imageType","Aadhaar");
                editor.commit();
            }
        });

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
    void openChooseImageDialog(){
        chooseImage = new Dialog(this);
        chooseImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        chooseImage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        chooseImage.setContentView(R.layout.dialog_choose_image);

        chooseImageCamera = (TextView) chooseImage.findViewById(R.id.choose_image_camera);
        chooseImageGallery = (TextView) chooseImage.findViewById(R.id.choose_image_gallery);
        chooseImage.show();
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(DocumentUploadActivity.this);
        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage();
        }
        chooseImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImageFromCamera();
                chooseImage.dismiss();
            }
        });
        chooseImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImageFromGallery();
                chooseImage.dismiss();
            }
        });
    }


    private void ChooseImageFromCamera() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            imageToUploadUri = Uri.fromFile(f);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        } else {
            Toast.makeText(DocumentUploadActivity.this, "No activity found to perform this task", Toast.LENGTH_SHORT).show();
        }
    }

    private void ChooseImageFromGallery() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, RESULT_LOAD_IMAGE);

        } else {
            Toast.makeText(DocumentUploadActivity.this, "No activity found to perform this task", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(sharedpreferences.getString("imageType","").equals("Voter")){
            imageView=imgVoterId;
            btnUpload=btnUploadVoterId;
        }
        else if(sharedpreferences.getString("imageType","").equals("Aadhaar")){
            imageView=imgAadhaarCard;
            btnUpload=btnUploadAadhaarCard;
        }
        if (requestCode==CAMERA_REQUEST || requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Bitmap originBitmap = null;
            Uri selectedImage=null;
            if(requestCode == RESULT_LOAD_IMAGE )
                selectedImage=data.getData();
            if(imageToUploadUri != null)
                selectedImage=imageToUploadUri;
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                Log.e("CatchActivityResult",e.getMessage().toString());

            }
            if (originBitmap != null) {
                this.imageView.setImageBitmap(originBitmap);
                Log.e("ImageLoaded", "Done Loading Image");
                try {
                    Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
                    byteArray = byteArrayOutputStream.toByteArray();
                    Log.e("byteArray", byteArray.toString());
                    btnUpload.setVisibility(View.VISIBLE);
                    encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } catch (Exception e) {
                    Log.e("OnActivityResultEx", e.toString());
                }
            }
        } else {
            Log.e("ErrorOccurredActRes", "ErrorOnActRes");

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(chooseImage!=null)
            chooseImage.dismiss();
    }
}
