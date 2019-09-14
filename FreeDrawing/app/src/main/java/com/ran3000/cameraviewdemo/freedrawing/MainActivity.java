package com.ran3000.cameraviewdemo.freedrawing;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.divyanshu.draw.widget.DrawView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final float SELECTED_COLOR_SCALE = 1.2f;

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

    @BindView(R.id.image_color_black)
    ImageView black;
    @BindView(R.id.image_color_red)
    ImageView red;
    @BindView(R.id.image_color_yellow)
    ImageView yellow;
    @BindView(R.id.image_color_green)
    ImageView green;
    @BindView(R.id.image_color_blue)
    ImageView blue;
    @BindView(R.id.image_color_pink)
    ImageView pink;
    @BindView(R.id.image_color_brown)
    ImageView brown;

    ImageView currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        camera.setLifecycleOwner(this);

        camera.setVideoMaxDuration(120 * 1000); // max 2mins
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

                // refresh gallery
                MediaScannerConnection.scanFile(MainActivity.this,
                        new String[]{result.getFile().toString()}, null,
                        (filePath, uri) -> {
                            Log.i("ExternalStorage", "Scanned " + filePath + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        });
            }
        });

        forwardTouchesView.setForwardTo(drawView);
        colorRed();
        
        PermissionUtils.requestReadWriteAppPermissions(this);
    }

    @OnClick(R.id.fab_video)
    void captureVideoSnapshot() {
        if (camera.isTakingVideo()) {
            camera.stopVideo();
            fabVideo.setImageResource(R.drawable.ic_videocam_black_24dp);
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.US);
        String currentTimeStamp = dateFormat.format(new Date());

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "CameraViewFreeDrawing";
        File outputDir= new File(path);
        outputDir.mkdirs();
        File saveTo = new File(path + File.separator + currentTimeStamp + ".mp4");
        camera.takeVideoSnapshot(saveTo);

        fabVideo.setImageResource(R.drawable.ic_stop_black_24dp);
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
                fabFront.setImageResource(R.drawable.ic_camera_front_black_24dp);
                break;

            case FRONT:
                fabFront.setImageResource(R.drawable.ic_camera_rear_black_24dp);
                break;
        }
    }

    @OnClick(R.id.image_clear)
    void clearCanvas() {
        drawView.clearCanvas();
    }

    private void setColor(ImageView colorView, int color) {
        if (currentColor == colorView) {
            return;
        }

        drawView.setColor(color);

        colorView.setScaleX(SELECTED_COLOR_SCALE);
        colorView.setScaleY(SELECTED_COLOR_SCALE);

        if (currentColor != null) {
            currentColor.setScaleX(1.0f);
            currentColor.setScaleY(1.0f);
        }

        currentColor = colorView;
    }

    @OnClick(R.id.image_color_black)
    void colorBlack() {
        setColor(black, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_black,null));
    }

    @OnClick(R.id.image_color_red)
    void colorRed() {
        setColor(red, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_red,null));
    }

    @OnClick(R.id.image_color_yellow)
    void colorYellow() {
        setColor(yellow, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_yellow,null));
    }

    @OnClick(R.id.image_color_green)
    void colorGreen() {
        setColor(green, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_green,null));
    }

    @OnClick(R.id.image_color_blue)
    void colorBlue() {
        setColor(blue, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_blue,null));
    }

    @OnClick(R.id.image_color_pink)
    void colorPink() {
        setColor(pink, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_pink,null));
    }

    @OnClick(R.id.image_color_brown)
    void colorBrown() {
        setColor(brown, ResourcesCompat.getColor(MainActivity.this.getResources(), R.color.color_brown,null));
    }


}
