package com.example.chirag.slidingtabsusingviewpager;

/**
 * Created by effy on 2018/4/23.
 */


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class Tab2_secSurface extends GLSurfaceView {
    private Render2_2 mRenderer;

    private float mDensity;

    public Tab2_secSurface(Context context) {
        super(context);
    }

    public Tab2_secSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    // Hides superclass method.
    public void setRenderer(Render2_2 renderer, float density) {
        mRenderer = renderer;
        mDensity = density;
        super.setRenderer(renderer);
    }
}
