package com.example.decom_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton Cam = findViewById(R.id.imageButton);
        ImageButton Gall = findViewById(R.id.imageButton2);


        Cam.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("MANZI", Integer.toString(requestCode));
        try {
            switch (requestCode) {
                case 1: {
                    if (resultCode == RESULT_OK) {

                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(this.getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            imageView =  findViewById(R.id.imageView);
                            imageView.setImageBitmap(bitmap);
                            TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                            Frame imageFrame = new Frame.Builder()

                                    .setBitmap(bitmap)                 // your image bitmap
                                    .build();
                            String imageText = "";


                            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

                            for (int i = 0; i < textBlocks.size(); i++) {
                                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                                imageText += textBlock.getValue();                   // return string
                            }
                            TextView imageTextView = findViewById(R.id.textView);
                            imageTextView.setText(imageText);
                            imageTextView.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                    TextView t =  findViewById(R.id.textView);
                    t.setText("kya hai ye");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.decom_android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


}
