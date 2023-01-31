package com.example.sender;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback {

    Camera camera;
    Camera.PictureCallback jpegCallback;        //should work

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;

    final int CAMERA_REQUEST_CODE = 1;
    public static CameraFragment newInstance(){
        CameraFragment fragment = new CameraFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        mSurfaceView = view.findViewById(R.id.surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();

        //CAMERA permission for user tp manually agree
        //request outside of app
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }else{
            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        Button mLogout = view.findViewById(R.id.logout);
        Button mCapture = view.findViewById(R.id.capture);
        Button mFindUser = view.findViewById(R.id.findUsers);
        mLogout.setOnClickListener(new View.OnClickListener(){      //might change
            @Override
                    public void onClick(View view){
                LogOut();
            }
        });
        mCapture.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        captureImage();
    }
});
        mCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFindUser();
            }
        });

            jpegCallback = new Camera.PictureCallback(){
                @Override
                public void onPictureTaken(byte[] bytes, Camera camera) {
        Intent intent = new Intent(getActivity(), ShowCaputreActivity.class);
        intent.putExtra("capture", bytes);
        startActivity(intent);
        return;
                }
            };

        return view;
    }


    private void captureImage() {
        camera.takePicture(null, null, jpegCallback);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        camera= Camera.open();

        Camera.Parameters parameters;
        parameters = camera.getParameters();       //dk y is deprecated what is not

        camera.setDisplayOrientation(90);       //might change degrees & fps
        parameters.setPreviewFormat(40);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);   //OBJECT FOCUSING

        //finds sizes and getting the biggest ones since we use fullscreen to match the whole phone
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
        bestSize = sizeList.get(0);
        for(int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) > (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }
        parameters.setPreviewSize(bestSize.width, bestSize.height);
        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();


    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode){
        case CAMERA_REQUEST_CODE:{
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mSurfaceHolder.addCallback(this);
                mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }else{
                Toast.makeText(getContext(), "Provide the permission or I won't work( ", Toast.LENGTH_LONG).show();
            }
            break;
        }
    }
    }

    private void mFindUser() {
        Intent intent = new Intent(getContext(), FindUsersActivity.class);       //when a user logsout it goes to SplashActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return;
    }


    private void LogOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), SplashScreenActivity.class);       //when a user logsout it goes to SplashActivity
   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return;
    }
}
