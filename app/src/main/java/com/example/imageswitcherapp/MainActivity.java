package com.example.imageswitcherapp;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.HashMap;
import java.util.Random;
import android.content.Intent;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import com.example.imageswitcherapp.FullScreenImageActivity;


public class MainActivity extends AppCompatActivity {

    private ImageSwitcher imageSwitcher;
    private TextView imageCaption, likeCounter, imageInfo;
    private ImageButton likeButton;
    private ProgressBar imageProgress;
    private Button toggleSlideshow, toggleMode;
    private int currentIndex = 0;
    private int likeCount = 0;
    private boolean isSlideshowRunning = false;
    private final Handler handler = new Handler();
    private Runnable slideshowRunnable;
    private final Random random = new Random();
    private final int totalImages;
    private final HashMap<Integer, Boolean> likedImages = new HashMap<>();

    private final int[] imageIds = {
            R.drawable.animal1, R.drawable.animal2, R.drawable.animal3, R.drawable.animal4,
            R.drawable.animal5, R.drawable.animal6, R.drawable.animal7, R.drawable.animal8,
            R.drawable.animal9, R.drawable.animal10, R.drawable.animal11, R.drawable.animal12,
            R.drawable.animal13, R.drawable.animal14, R.drawable.animal15, R.drawable.animal16,
            R.drawable.animal17, R.drawable.animal18, R.drawable.animal19, R.drawable.animal20,
            R.drawable.animal21, R.drawable.animal22, R.drawable.animal23, R.drawable.animal24,
            R.drawable.animal25, R.drawable.animal26, R.drawable.animal27, R.drawable.animal28,
            R.drawable.animal29, R.drawable.animal30
    };

    private final String[] captions = {
            "Squad goals in the wild!", "Stripes never go out of style!", "Big ears, bigger personality!",
            "Tiny but mighty!", "Too tired to panda today…", "Walkin’ with wisdom!", "Fluffy and fabulous!",
            "Mom’s kisses are the best!", "Nutty and loving it!", "Who’s a good boy? (Hint: Me!)",
            "Golden smiles and endless cuddles!", "Feeling fabulous, naturally!", "Serious face, soft heart!",
            "Bambi vibes all day!", "Whiskers on point!", "Did someone say ‘whoo’?", "Just keep swimming!",
            "Squad goals: Capybara edition!", "Fluffy but fierce!", "Cuteness? Nailed it!",
            "Peek-a-boo! Did I surprise you?", "Tiny but mighty explorers!",
            "Staring into your soul... and asking for treats!", "Squad goals: Puffin edition!",
            "Smiling because life is awesome!", "Hump day? More like family day!",
            "Koality time in the eucalyptus lounge!", "Big love comes in big packages!",
            "Silent but deadly... and fabulous!", "The start of a paw-some friendship!"
    };

    public MainActivity() {
        this.totalImages = imageIds.length;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageSwitcher = findViewById(R.id.imageSwitcher);
        imageCaption = findViewById(R.id.imageCaption);
        likeCounter = findViewById(R.id.likeCounter);
        likeButton = findViewById(R.id.likeButton);
        imageProgress = findViewById(R.id.imageProgress);
        imageInfo = findViewById(R.id.imageInfo);
        toggleSlideshow = findViewById(R.id.toggleSlideshow);
        toggleMode = findViewById(R.id.toggleMode);

        imageSwitcher.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FullScreenImageActivity.class);
            intent.putExtra("imageList", imageIds);
            intent.putExtra("position", currentIndex);
            startActivity(intent);

        });

        imageSwitcher.setFactory(() -> {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageView;
        });

        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.in_animation));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.out_animation));

        findViewById(R.id.nextButton).setOnClickListener(v -> nextImage());
        findViewById(R.id.prevButton).setOnClickListener(v -> prevImage());
        findViewById(R.id.firstButton).setOnClickListener(v -> goToFirstImage());
        findViewById(R.id.lastButton).setOnClickListener(v -> goToLastImage());
        toggleSlideshow.setOnClickListener(v -> toggleSlideshow());
        toggleMode.setOnClickListener(v -> toggleDisplayMode()); // ✅ Fixed listener issue

        likeButton.setOnClickListener(v -> toggleLike());

        updateImage();
    }


    private void nextImage() {
        boolean isShuffleMode = toggleMode.getText().toString().equals(getString(R.string.shuffle_mode));
        currentIndex = isShuffleMode ? random.nextInt(imageIds.length) : (currentIndex + 1) % imageIds.length;
        updateImage();
    }

    private void prevImage() {
        currentIndex = (currentIndex - 1 + imageIds.length) % imageIds.length;
        updateImage();
    }

    private void goToFirstImage() {
        currentIndex = 0;
        updateImage();
    }

    private void goToLastImage() {
        currentIndex = imageIds.length - 1;
        updateImage();
    }

    private void updateImage() {
        imageSwitcher.setImageResource(imageIds[currentIndex]);
        imageCaption.setText(captions[currentIndex]);
        int progress = ((currentIndex + 1) * 100) / imageIds.length;
        imageProgress.setProgress(progress);
        imageInfo.setText(getString(R.string.image_status, currentIndex + 1, totalImages));

        // ✅ Fixed NullPointerException
        boolean liked = likedImages.containsKey(currentIndex) && Boolean.TRUE.equals(likedImages.get(currentIndex));
        likeButton.setImageResource(liked ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);
        likeButton.setColorFilter(ContextCompat.getColor(this, liked ? R.color.red : R.color.black));
    }

    private void toggleSlideshow() {
        if (isSlideshowRunning) {
            handler.removeCallbacks(slideshowRunnable);
            toggleSlideshow.setText(R.string.start_slideshow);
            Toast.makeText(this, "Slideshow Stopped", Toast.LENGTH_SHORT).show();
        } else {
            slideshowRunnable = new Runnable() {
                @Override
                public void run() {
                    nextImage();
                    if (isSlideshowRunning) {
                        handler.postDelayed(this, 3000);
                    }
                }
            };
            handler.postDelayed(slideshowRunnable, 3000);
            toggleSlideshow.setText(R.string.stop_slideshow);
            Toast.makeText(this, "Slideshow Started", Toast.LENGTH_SHORT).show();
        }
        isSlideshowRunning = !isSlideshowRunning;
    }

    private void toggleLike() {
        boolean liked = likedImages.containsKey(currentIndex) && Boolean.TRUE.equals(likedImages.get(currentIndex));
        likedImages.put(currentIndex, !liked);
        likeCount += liked ? -1 : 1;
        likeCounter.setText(getString(R.string.likes_count, likeCount));
        updateImage();
        Toast.makeText(this, liked ? "Disliked" : "Liked", Toast.LENGTH_SHORT).show();
    }

    private void toggleDisplayMode() {
        String currentText = toggleMode.getText().toString();
        if (currentText.equals(getString(R.string.normal_mode))) {
            toggleMode.setText(R.string.shuffle_mode);
            Toast.makeText(this, "Shuffle Mode Activated", Toast.LENGTH_SHORT).show();
        } else {
            toggleMode.setText(R.string.normal_mode);
            Toast.makeText(this, "Normal Mode Activated", Toast.LENGTH_SHORT).show();
        }
    }
}
