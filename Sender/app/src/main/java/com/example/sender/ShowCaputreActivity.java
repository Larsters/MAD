package com.example.sender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ShowCaputreActivity extends AppCompatActivity {

    String Uid;
    Bitmap rotateBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_caputre);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        byte[] b = extras.getByteArray("capture");

        if(b!=null){                                    //making sure it is not null again
            ImageView image = findViewById(R.id.imageCapture);

            //fun part- converting bytes into bitmap
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

            Bitmap rotateBitmap = rotate(decodedBitmap);

            //for not making the image android looking :>
            image.setImageBitmap(rotateBitmap);
        }

        Uid = FirebaseAuth.getInstance().getUid();

        Button mStory = findViewById(R.id.story);
        mStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToStories();
            }
        });

    }

    private void saveToStories() {
       final DatabaseReference usersStoryDb = FirebaseDatabase.getInstance().getReference().child("users").child(Uid).child("story");
       final String key = usersStoryDb.push().getKey();

        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("captures").child(key);
        //converting image into byteArray to store in db
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotateBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] dataToUpload = baos.toByteArray();
        UploadTask uploadTask = filePath.putBytes(dataToUpload);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> imageUrl = taskSnapshot.getStorage().getDownloadUrl();      //redo if doesn't work

                Long currentTimestamp = System.currentTimeMillis();
                Long endTimestamp = currentTimestamp + (24*60*60*1000);         //hrs, mins, secs, milisecs

                Map<String, Object> mapToUpload = new HashMap<>();
                mapToUpload.put("imageUrl", imageUrl.toString());
                mapToUpload.put("timestampStart", currentTimestamp);
                mapToUpload.put("timestampEnd", endTimestamp);

                usersStoryDb.child(key).setValue(mapToUpload);
                finish();
                return;
            }
        });


        //retake the pic
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                finish();
                return;
            }
        });

    }

    private Bitmap rotate(Bitmap decodedBitmap) {
        int w = decodedBitmap.getWidth();
        int h = decodedBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodedBitmap, 0, 0, w, h, matrix, true);
    }
}