package com.ran3000.cameraviewdemo.freedrawing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.divyanshu.draw.widget.DrawView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.camera)
    CameraView camera;
    @BindView(R.id.fab_video)
    FloatingActionButton fabVideo;
    @BindView(R.id.fab_front)
    FloatingActionButton fabFront;
    @BindView(R.id.forwardTouches)
    ForwardTouchesView forwardTouchesView;
    @BindView(R.id.draw_view)
    DrawView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        camera.setLifecycleOwner(this);

        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                PicturePreviewActivity.setPictureResult(result);
                Intent intent = new Intent(MainActivity.this, PicturePreviewActivity.class);
                startActivity(intent);
            }

            @Override
            public void onVideoTaken(@NonNull VideoResult result) {
                super.onVideoTaken(result);
                VideoPreviewActivity.setVideoResult(result);
                Intent intent = new Intent(MainActivity.this, VideoPreviewActivity.class);
                startActivity(intent);
            }
        });

        forwardTouchesView.setForwardTo(drawView);

    }

    @OnClick(R.id.camera)
    void tap() {
        Toast.makeText(this, "Tap.", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fab_video)
    void captureVideoSnapshot() {
        if (camera.isTakingVideo()) {
            Toast.makeText(this, "Already taking video.", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Recording snapshot for 5 seconds...", Toast.LENGTH_SHORT).show();
        camera.takeVideoSnapshot(new File(getFilesDir(), "video.mp4"), 5000);
    }

    @OnClick(R.id.fab_picture)
    void capturePictureSnapshot() {
        if (camera.isTakingVideo()) {
            Toast.makeText(this, "Already taking video.", Toast.LENGTH_SHORT).show();
            return;
        }
        camera.takePictureSnapshot();
    }

    @OnClick(R.id.fab_front)
    void toggleCamera() {
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                fabFront.setImageResource(R.drawable.ic_camera_front_white_24dp);
                break;

            case FRONT:
                fabFront.setImageResource(R.drawable.ic_camera_rear_white_24dp);
                break;
        }
    }

}
