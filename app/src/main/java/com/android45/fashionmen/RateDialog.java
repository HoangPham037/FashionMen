package com.android45.fashionmen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

public class RateDialog extends Dialog {
    private float userRate = 0;
    public RateDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_dialog);

        final AppCompatButton rateNowbtn = findViewById(R.id.btnRateNow);
        final AppCompatButton maybeLetter = findViewById(R.id.MaybeLetter);
        final RatingBar ratingbar = findViewById(R.id.RatingBar);
        final ImageView imageView = findViewById(R.id.imgRate);

        rateNowbtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Thank you for rating", Toast.LENGTH_SHORT).show();
            dismiss();
        });
        maybeLetter.setOnClickListener(v -> {
            dismiss();
        });
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating <= 1){
                    imageView.setImageResource(R.drawable.tuc);
                }else if (rating <= 3){
                    imageView.setImageResource(R.drawable.cuoi);
                }else if (rating <=5){
                    imageView.setImageResource(R.drawable.tim);
                }

                animateImage(imageView);
                userRate = rating;

            }
        });
    }
    private  void animateImage(ImageView ratingImage){
        ScaleAnimation animation = new ScaleAnimation(0,1f,0f,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setFillAfter(true);
        animation.setDuration(200);
        ratingImage.startAnimation(animation);
    }
}
