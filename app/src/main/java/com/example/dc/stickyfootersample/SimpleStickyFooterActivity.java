package com.example.dc.stickyfootersample;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

public class SimpleStickyFooterActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView;
    private ConstraintLayout footerFloatingContainer;
    private ConstraintLayout footerFixedContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_sticky_footer);

        nestedScrollView = findViewById(R.id.nestedScrollView);
        footerFloatingContainer = findViewById(R.id.footerFloatingContainer);
        footerFixedContainer = findViewById(R.id.footerFixedContainer);

        scrollListener();
    }


    private void scrollListener() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;

        final Rect scrollBounds = new Rect();
        nestedScrollView.getHitRect(scrollBounds);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // this conditions work as footerFloatingContainer is wrapped in a layout (Relative Layout id = footerFloatingOuterContainer)
                if (footerFloatingContainer.getLocalVisibleRect(scrollBounds)) {
                    if (scrollBounds.bottom == footerFloatingContainer.getBottom() && scrollBounds.top == footerFloatingContainer.getTop()) {
                        //footerFloatingContainer is fully visible on screen
                        footerFixedContainer.setVisibility(View.GONE);
                    } else if (scrollBounds.top > footerFloatingContainer.getTop()) {
                        //footerFloatingContainer is fully or partially above the screen
                        footerFixedContainer.setVisibility(View.GONE);
                    } else if (scrollBounds.bottom < footerFloatingContainer.getBottom()) {
                        //footerFloatingContainer is fully or partially below the screen
                        footerFixedContainer.setVisibility(View.VISIBLE);
                    }
                    //Log.d("if scrollCase", String.valueOf(scrollY + " " + oldScrollY + " " + scrollBounds.bottom + " " + scrollBounds.top + " " + footerFloatingContainer.getBottom() + " " + footerFloatingContainer.getTop()));
                } else if (scrollY <= height) {
                    // this is for extra checking as sometimes methods does not return exact value due to fast scrolling
                    footerFixedContainer.setVisibility(View.VISIBLE);
                    //Log.d("else scrollCase", String.valueOf(scrollY + " " + oldScrollY + " " + scrollBounds.bottom + " " + scrollBounds.top + " " + footerFloatingContainer.getBottom() + " " + footerFloatingContainer.getTop()));
                }
            }
        });
    }
}
