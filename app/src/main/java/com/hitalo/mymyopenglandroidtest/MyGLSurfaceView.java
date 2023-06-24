package com.hitalo.mymyopenglandroidtest;

import android.content.Context;
import android.opengl.GLSurfaceView;

class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer renderer;
    public MyGLSurfaceView(Context context){
        super(context);
        setEGLContextClientVersion(3);
        renderer = new MyGLRenderer();
        setRenderer(renderer);
    }
}
