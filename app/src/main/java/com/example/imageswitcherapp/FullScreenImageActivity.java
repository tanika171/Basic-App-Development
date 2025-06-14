package com.example.imageswitcherapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullScreenImage = findViewById(R.id.fullScreenImage);
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        int[] imageList = getIntent().getIntArrayExtra("imageList");
        int positionFromIntent = getIntent().getIntExtra("position", 0);

        // Check for null to avoid NullPointerException
        if (imageList == null || imageList.length == 0) return;

        // Make position mutable
        final int[] position = {positionFromIntent};

        // Set initial image
        fullScreenImage.setImageResource(imageList[position[0]]);

        // NEXT
        findViewById(R.id.nextButton).setOnClickListener(v -> {
            if (position[0] < imageList.length - 1) {
                position[0]++;
                fullScreenImage.setImageResource(imageList[position[0]]);
            }
        });

        // PREVIOUS
        findViewById(R.id.prevButton).setOnClickListener(v -> {
            if (position[0] > 0) {
                position[0]--;
                fullScreenImage.setImageResource(imageList[position[0]]);
            }
        });
    }
}
