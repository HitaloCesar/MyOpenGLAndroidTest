package com.hitalo.mymyopenglandroidtest;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private float[] vertexesPosition = {
            -0.5f, -0.25f, 0.0f,
            0.5f, -0.25f, 0.0f,
            0.0f, 0.25f, 0.0f
    };
    private float[] vertexesColor = {
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f
    };
    private int programId;
    private int positionHandle;
    private int colorHandle;
    private final int COORDS_PER_POINT = 3;
    private final int stride = COORDS_PER_POINT * 4; // 4 bytes per vertex
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        initShaderProgram();
        setupVertexBuffer();
    }

    public void onDrawFrame(GL10 unused) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        GLES30.glUseProgram(programId);
        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glEnableVertexAttribArray(colorHandle);
        GLES30.glVertexAttribPointer(positionHandle, COORDS_PER_POINT, GLES30.GL_FLOAT,
                false, stride, vertexBuffer);
        GLES30.glVertexAttribPointer(colorHandle, 3, GLES30.GL_FLOAT, false,
                stride, colorBuffer);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, COORDS_PER_POINT);
        GLES30.glDisableVertexAttribArray(positionHandle);
        GLES30.glDisableVertexAttribArray(colorHandle);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
    }

    private void initShaderProgram() {
        String vertexShaderCode =
                "#version 300 es\n" +
                "in vec3 a_Position;\n" +
                "in vec3 a_Color;\n" +
                "out vec3 v_Color;\n" +
                "void main() {\n" +
                "    gl_Position = vec4(a_Position, 1.0);\n" +
                "    v_Color = a_Color;\n" +
                "}";

        String fragmentShaderCode =
                "#version 300 es\n" +
                "precision mediump float;\n" +
                "in vec3 v_Color;\n" +
                "out vec4 fragColor;\n" +
                "void main() {\n" +
                "    fragColor = vec4(v_Color, 1.0);\n" +
                "}";

        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        programId = GLES30.glCreateProgram();
        GLES30.glAttachShader(programId, vertexShader);
        GLES30.glAttachShader(programId, fragmentShader);
        GLES30.glLinkProgram(programId);

        positionHandle = GLES30.glGetAttribLocation(programId, "a_Position");
        colorHandle = GLES30.glGetAttribLocation(programId, "a_Color");
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }

    private void setupVertexBuffer() {
        ByteBuffer bb1 = ByteBuffer.allocateDirect(vertexesPosition.length * 4);
        bb1.order(ByteOrder.nativeOrder());
        vertexBuffer = bb1.asFloatBuffer();
        vertexBuffer.put(vertexesPosition);
        vertexBuffer.position(0);

        ByteBuffer bb2 = ByteBuffer.allocateDirect(vertexesColor.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        colorBuffer = bb2.asFloatBuffer();
        colorBuffer.put(vertexesColor);
        colorBuffer.position(0);
    }
}