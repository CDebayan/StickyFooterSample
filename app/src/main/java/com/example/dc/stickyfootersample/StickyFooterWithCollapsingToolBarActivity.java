package com.example.dc.stickyfootersample;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

public class StickyFooterWithCollapsingToolBarActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView;
    private ConstraintLayout footerFloatingContainer;
    private ConstraintLayout footerFixedContainer;
    private int currentScrollY;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky_footer_with_collapsing_tool_bar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBarLayout);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        footerFloatingContainer = findViewById(R.id.footerFloatingContainer);
        footerFixedContainer = findViewById(R.id.footerFixedContainer);
        setSupportActionBar(toolbar);

        scrollListener();
    }

    private void scrollListener() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;

        final Rect scrollBounds = new Rect();
        nestedScrollView.getHitRect(scrollBounds);
        appBarLayout.getHitRect(scrollBounds);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
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

                currentScrollY = scrollY;
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (currentScrollY == 0) {
                    if (!footerFloatingContainer.getLocalVisibleRect(scrollBounds) || scrollBounds.height() < footerFloatingContainer.getHeight()) {
                        // imageView is not within or only partially within the visible window
                        footerFixedContainer.setVisibility(View.VISIBLE);
                    } else {
                        // imageView is completely visible
                        footerFixedContainer.setVisibility(View.GONE);
                    }
                }
            }
        });

    }
}
