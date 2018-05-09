package com.example.chirag.librarybooklocator;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Render2_2 implements GLSurfaceView.Renderer {
    public volatile float mDeltaX;
    public volatile float mDeltaY;

    public int bookcasefloor;
    public float bookX;
    public float bookY;


    //private final Context mActivityContext;
    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    /**
     * Store the projection matrix. This is used to project the scene onto a 2D viewport.
     */
    private float[] mProjectionMatrix = new float[16];

    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    private float[] mMVPMatrix = new float[16];

    /** Store our model data in a float buffer. */
    private final FloatBuffer mTriangle1Vertices;
    private final FloatBuffer mTriangle2Vertices;
    private final FloatBuffer mTriangle3Vertices;
    private final FloatBuffer mTriangle4Vertices;
    private final FloatBuffer mborder;
    private final FloatBuffer myellow;
    private final FloatBuffer mred;
    private final FloatBuffer mborder2;

    /** This will be used to pass in the transformation matrix. */
    private int mMVPMatrixHandle;

    /** This will be used to pass in model position information. */
    private int mPositionHandle;

    /** This will be used to pass in model color information. */
    private int mColorHandle;

    /** How many bytes per float. */
    private final int mBytesPerFloat = 4;

    /** How many elements per vertex. */
    private final int mStrideBytes = 7 * mBytesPerFloat;

    /** Offset of the position data. */
    private final int mPositionOffset = 0;

    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;

    /** Offset of the color data. */
    private final int mColorOffset = 3;

    /** Size of the color data in elements. */
    private final int mColorDataSize = 4;

    /**
     * Initialize the model data.
     */
    public Render2_2() {
        MapData a = new MapData();

        float[] border = a.border;
        float[] yellow = a.yellow;
        float[] red = a.red;
        float[] border2= a.border2;

        float[] triangle1VerticesData = a.triangle1VerticesData;
        float[] triangle2VerticesData = a.triangle2VerticesData;
        float[] triangle3VerticesData = a.triangle3VerticesData;
        float[] triangle4VerticesData = a.triangle4VerticesData;


        // Initialize the buffers.
        mborder = ByteBuffer.allocateDirect(border.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        myellow = ByteBuffer.allocateDirect(yellow.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mred = ByteBuffer.allocateDirect(red.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mborder2 = ByteBuffer.allocateDirect(border2.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle2Vertices = ByteBuffer.allocateDirect(triangle2VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle4Vertices = ByteBuffer.allocateDirect(triangle4VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mborder.put(border).position(0);
        myellow.put(yellow).position(0);
        mred.put(red).position(0);
        mborder2.put(border2).position(0);

        mTriangle1Vertices.put(triangle1VerticesData).position(0);
        mTriangle2Vertices.put(triangle2VerticesData).position(0);
        mTriangle3Vertices.put(triangle3VerticesData).position(0);
        mTriangle4Vertices.put(triangle4VerticesData).position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(176 / 255f, 210 / 255f, 212 /255f, 0.0f);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 50.0f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        final String vertexShader =
                "uniform mat4 u_MVPMatrix;      \n"        // A constant representing the combined model/view/projection matrix.

                        + "attribute vec4 a_Position;     \n"        // Per-vertex position information we will pass in.
                        + "attribute vec4 a_Color;        \n"        // Per-vertex color information we will pass in.

                        + "varying vec4 v_Color;          \n"        // This will be passed into the fragment shader.

                        + "void main()                    \n"        // The entry point for our vertex shader.
                        + "{                              \n"
                        + "   v_Color = a_Color;          \n"        // Pass the color through to the fragment shader.
                        // It will be interpolated across the triangle.
                        + "   gl_Position = u_MVPMatrix   \n"    // gl_Position is a special variable used to store the final position.
                        + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
                        + "}                              \n";    // normalized screen coordinates.

        final String fragmentShader =
                "precision mediump float;       \n"        // Set the default precision to medium. We don't need as high of a
                        // precision in the fragment shader.
                        + "varying vec4 v_Color;          \n"        // This is the color from the vertex shader interpolated across the
                        // triangle per fragment.
                        + "void main()                    \n"        // The entry point for our fragment shader.
                        + "{                              \n"
                        + "   gl_FragColor = v_Color;     \n"        // Pass the color directly through the pipeline.
                        + "}                              \n";

        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        if (vertexShaderHandle == 0) {
            throw new RuntimeException("Error creating vertex shader.");
        }

        // Load in the fragment shader shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        if (fragmentShaderHandle == 0) {
            throw new RuntimeException("Error creating fragment shader.");
        }

        // Create a program object and store the handle to it.
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 100.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);


        //地图边框
        Matrix.setIdentityM(mModelMatrix, 0);
        //Matrix.translateM(mModelMatrix,0,11.0f,30.0f,0.0f);
        Matrix.scaleM(mModelMatrix, 0, 60.0f,80.0f,1.0f);
        drawQuad(mTriangle1Vertices);

        //书架
        //9(6+3)和4
        for (float i = 0.0f; i < 5; i++) {
            drawBookcase(i*12.5f);
        }

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, -32.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 50.0f,6.0f,1.0f);
        drawQuad(mTriangle3Vertices);

        //书本
        for (float i = 0.0f; i < 6; i++) {
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, -20 + i * 3, -22f, 0.0f);
            Matrix.rotateM(mModelMatrix, 0, 20.0f, 0, 0, 1.0f);
            Matrix.scaleM(mModelMatrix, 0, 2.0f, 7.0f, 1.0f);
            drawQuad(mTriangle2Vertices);
        }
        for (float i = 0.0f; i < 7; i++) {
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, -10 + i * 3, -9.0f, 0.0f);
            Matrix.rotateM(mModelMatrix, 0, -20.0f, 0, 0, 1.0f);
            Matrix.scaleM(mModelMatrix, 0, 2.0f, 7.0f, 1.0f);
            drawQuad(mTriangle2Vertices);
        }
        for (float i = 0.0f; i < 5; i++) {
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, 11.5f + i * 3, -9.0f, 0.0f);
            Matrix.scaleM(mModelMatrix, 0, 2.0f, 7.0f, 1.0f);
            drawQuad(mborder);
        }
        for (float i = 0.0f; i < 8; i++) {
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, 0 + i * 3, 3.75f, 0.0f);
            Matrix.scaleM(mModelMatrix, 0, 2.0f, 7.0f, 1.0f);
            drawQuad(mborder);
        }
        for (float i = 0.0f; i < 8; i++) {
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, -20 + i * 3, 16f, 0.0f);
            Matrix.rotateM(mModelMatrix, 0, 20.0f, 0, 0, 1.0f);
            Matrix.scaleM(mModelMatrix, 0, 2.0f, 7.0f, 1.0f);
            drawQuad(mborder);
        }
        for (float i = 0.0f; i < 8; i++) {
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, 0 + i * 3, 28.5f, 0.0f);
            Matrix.rotateM(mModelMatrix, 0, -20.0f, 0, 0, 1.0f);
            Matrix.scaleM(mModelMatrix, 0, 2.0f, 7.0f, 1.0f);
            drawQuad(mTriangle2Vertices);
        }


        //Y=5是3层
        //某本书具体的位置
        bookX = 3;
        bookY = 42.5f - bookcasefloor*12.5f;
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, bookX, bookY - 1.0f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 25.0f,7.0f,1.0f);
        drawQuad(myellow);
        for (float i = 0.0f; i < 6; i++) {
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, bookX - 7.5f + i * 3, bookY - 1.0f, 0.0f);
            //Matrix.rotateM(mModelMatrix,0,-20.0f,0,0,1.0f);
            Matrix.scaleM(mModelMatrix, 0, 2.0f, 6.0f, 1.0f);
            drawQuad(mred);
        }


    }

    private void drawBookcase(float position){
        // Draw the triangle facing straight on.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, position - 24.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 50.0f,3.0f,1.0f);

        drawQuad(mborder2);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, position - 20.f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 50.0f,6.0f,1.0f);
        drawQuad(mTriangle3Vertices);

    }

    private void drawQuad(final FloatBuffer aTriangleBuffer) {
        // Pass in the position information
        aTriangleBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        aTriangleBuffer.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
                mStrideBytes, aTriangleBuffer);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
    }
}

//
///**
// * Created by effy on 2018/4/23.
// */
//
//
//import android.opengl.GLES20;
//import android.opengl.GLSurfaceView;
//import android.opengl.Matrix;
//import android.os.SystemClock;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
//
//public class Render2_2 implements GLSurfaceView.Renderer {
//    public volatile float mDeltaX;
//    public volatile float mDeltaY;
//
//    public float bookcasefloor;
//    public float bookX;
//    public float bookY;
//
//
//    //private final Context mActivityContext;
//    /**
//     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
//     * of being located at the center of the universe) to world space.
//     */
//    private float[] mModelMatrix = new float[16];
//
//    /**
//     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
//     * it positions things relative to our eye.
//     */
//    private float[] mViewMatrix = new float[16];
//
//    /**
//     * Store the projection matrix. This is used to project the scene onto a 2D viewport.
//     */
//    private float[] mProjectionMatrix = new float[16];
//
//    /**
//     * Allocate storage for the final combined matrix. This will be passed into the shader program.
//     */
//    private float[] mMVPMatrix = new float[16];
//
//    /**
//     * Store our model data in a float buffer.
//     */
//    private final FloatBuffer mTriangle1Vertices;
//    private final FloatBuffer mTriangle2Vertices;
//    private final FloatBuffer mTriangle3Vertices;
//    private final FloatBuffer mTriangle4Vertices;
//    private final FloatBuffer mborder;
//    private final FloatBuffer myellow;
//    private final FloatBuffer mborder2;
//
//    /**
//     * This will be used to pass in the transformation matrix.
//     */
//    private int mMVPMatrixHandle;
//
//    /**
//     * This will be used to pass in model position information.
//     */
//    private int mPositionHandle;
//
//    /**
//     * This will be used to pass in model color information.
//     */
//    private int mColorHandle;
//
//    /**
//     * How many bytes per float.
//     */
//    private final int mBytesPerFloat = 4;
//
//    /**
//     * How many elements per vertex.
//     */
//    private final int mStrideBytes = 7 * mBytesPerFloat;
//
//    /**
//     * Offset of the position data.
//     */
//    private final int mPositionOffset = 0;
//
//    /**
//     * Size of the position data in elements.
//     */
//    private final int mPositionDataSize = 3;
//
//    /**
//     * Offset of the color data.
//     */
//    private final int mColorOffset = 3;
//
//    /**
//     * Size of the color data in elements.
//     */
//    private final int mColorDataSize = 4;
//
//    /**
//     * Initialize the model data.
//     */
//    public Render2_2() {
//        MapData a = new MapData();
//
//        float[] border = a.border;
//        float[] yellow = a.yellow;
//        float[] border2 = a.border2;
//
//        float[] triangle1VerticesData = a.triangle1VerticesData;
//        float[] triangle2VerticesData = a.triangle2VerticesData;
//        float[] triangle3VerticesData = a.triangle3VerticesData;
//        float[] triangle4VerticesData = a.triangle4VerticesData;
//
//
//        // Initialize the buffers.
//        mborder = ByteBuffer.allocateDirect(border.length * mBytesPerFloat)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        myellow = ByteBuffer.allocateDirect(yellow.length * mBytesPerFloat)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mborder2 = ByteBuffer.allocateDirect(border2.length * mBytesPerFloat)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//
//        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mTriangle2Vertices = ByteBuffer.allocateDirect(triangle2VerticesData.length * mBytesPerFloat)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.length * mBytesPerFloat)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//        mTriangle4Vertices = ByteBuffer.allocateDirect(triangle4VerticesData.length * mBytesPerFloat)
//                .order(ByteOrder.nativeOrder()).asFloatBuffer();
//
//        mborder.put(border).position(0);
//        myellow.put(yellow).position(0);
//        mborder2.put(border2).position(0);
//
//        mTriangle1Vertices.put(triangle1VerticesData).position(0);
//        mTriangle2Vertices.put(triangle2VerticesData).position(0);
//        mTriangle3Vertices.put(triangle3VerticesData).position(0);
//        mTriangle4Vertices.put(triangle4VerticesData).position(0);
//
//    }
//
//    @Override
//    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
//        // Set the background clear color to gray.
//        GLES20.glClearColor(176 / 255f, 210 / 255f, 212 / 255f, 0.0f);
//
//        // Position the eye behind the origin.
//        final float eyeX = 0.0f;
//        final float eyeY = 0.0f;
//        final float eyeZ = 50.0f;
//
//        // We are looking toward the distance
//        final float lookX = 0.0f;
//        final float lookY = 0.0f;
//        final float lookZ = -5.0f;
//
//        // Set our up vector. This is where our head would be pointing were we holding the camera.
//        final float upX = 0.0f;
//        final float upY = 1.0f;
//        final float upZ = 0.0f;
//
//        // Set the view matrix. This matrix can be said to represent the camera position.
//        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
//        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
//        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
//
//        final String vertexShader =
//                "uniform mat4 u_MVPMatrix;      \n"        // A constant representing the combined model/view/projection matrix.
//
//                        + "attribute vec4 a_Position;     \n"        // Per-vertex position information we will pass in.
//                        + "attribute vec4 a_Color;        \n"        // Per-vertex color information we will pass in.
//
//                        + "varying vec4 v_Color;          \n"        // This will be passed into the fragment shader.
//
//                        + "void main()                    \n"        // The entry point for our vertex shader.
//                        + "{                              \n"
//                        + "   v_Color = a_Color;          \n"        // Pass the color through to the fragment shader.
//                        // It will be interpolated across the triangle.
//                        + "   gl_Position = u_MVPMatrix   \n"    // gl_Position is a special variable used to store the final position.
//                        + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
//                        + "}                              \n";    // normalized screen coordinates.
//
//        final String fragmentShader =
//                "precision mediump float;       \n"        // Set the default precision to medium. We don't need as high of a
//                        // precision in the fragment shader.
//                        + "varying vec4 v_Color;          \n"        // This is the color from the vertex shader interpolated across the
//                        // triangle per fragment.
//                        + "void main()                    \n"        // The entry point for our fragment shader.
//                        + "{                              \n"
//                        + "   gl_FragColor = v_Color;     \n"        // Pass the color directly through the pipeline.
//                        + "}                              \n";
//
//        // Load in the vertex shader.
//        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
//
//        if (vertexShaderHandle != 0) {
//            // Pass in the shader source.
//            GLES20.glShaderSource(vertexShaderHandle, vertexShader);
//
//            // Compile the shader.
//            GLES20.glCompileShader(vertexShaderHandle);
//
//            // Get the compilation status.
//            final int[] compileStatus = new int[1];
//            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
//
//            // If the compilation failed, delete the shader.
//            if (compileStatus[0] == 0) {
//                GLES20.glDeleteShader(vertexShaderHandle);
//                vertexShaderHandle = 0;
//            }
//        }
//
//        if (vertexShaderHandle == 0) {
//            throw new RuntimeException("Error creating vertex shader.");
//        }
//
//        // Load in the fragment shader shader.
//        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
//
//        if (fragmentShaderHandle != 0) {
//            // Pass in the shader source.
//            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);
//
//            // Compile the shader.
//            GLES20.glCompileShader(fragmentShaderHandle);
//
//            // Get the compilation status.
//            final int[] compileStatus = new int[1];
//            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
//
//            // If the compilation failed, delete the shader.
//            if (compileStatus[0] == 0) {
//                GLES20.glDeleteShader(fragmentShaderHandle);
//                fragmentShaderHandle = 0;
//            }
//        }
//
//        if (fragmentShaderHandle == 0) {
//            throw new RuntimeException("Error creating fragment shader.");
//        }
//
//        // Create a program object and store the handle to it.
//        int programHandle = GLES20.glCreateProgram();
//
//        if (programHandle != 0) {
//            // Bind the vertex shader to the program.
//            GLES20.glAttachShader(programHandle, vertexShaderHandle);
//
//            // Bind the fragment shader to the program.
//            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
//
//            // Bind attributes
//            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
//            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");
//
//            // Link the two shaders together into a program.
//            GLES20.glLinkProgram(programHandle);
//
//            // Get the link status.
//            final int[] linkStatus = new int[1];
//            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
//
//            // If the link failed, delete the program.
//            if (linkStatus[0] == 0) {
//                GLES20.glDeleteProgram(programHandle);
//                programHandle = 0;
//            }
//        }
//
//        if (programHandle == 0) {
//            throw new RuntimeException("Error creating program.");
//        }
//
//        // Set program handles. These will later be used to pass in values to the program.
//        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
//        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
//        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");
//
//        // Tell OpenGL to use this program when rendering.
//        GLES20.glUseProgram(programHandle);
//    }
//
//    @Override
//    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
//        // Set the OpenGL viewport to the same size as the surface.
//        GLES20.glViewport(0, 0, width, height);
//
//        // Create a new perspective projection matrix. The height will stay the same
//        // while the width will vary as per aspect ratio.
//        final float ratio = (float) width / height;
//        final float left = -ratio;
//        final float right = ratio;
//        final float bottom = -1.0f;
//        final float top = 1.0f;
//        final float near = 1.0f;
//        final float far = 100.0f;
//
//        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
//    }
//
//    @Override
//    public void onDrawFrame(GL10 glUnused) {
//        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
//
//        // Do a complete rotation every 10 seconds.
//        long time = SystemClock.uptimeMillis() % 10000L;
//        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
//
//        //地图边框
//        Matrix.setIdentityM(mModelMatrix, 0);
//        //Matrix.translateM(mModelMatrix,0,11.0f,30.0f,0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 60.0f, 80.0f, 1.0f);
//        drawQuad(mTriangle1Vertices);
//
//        //书架
//        //9(6+3)和4
//        for (float i = 0.0f; i < 5; i++) {
//            drawBookcase(i * 12.5f);
//        }
//
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, 0.0f, -32.5f, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 50.0f, 6.0f, 1.0f);
//        drawQuad(mTriangle3Vertices);
//
//
//        //Y=5是3层
//        //
//        bookX = 3;
//        bookY = 42.5f - bookcasefloor * 12.5f;
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, bookX, bookY, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 25.0f, 6.0f, 1.0f);
//        drawQuad(myellow);
//
//
//    }
//
//    private void drawBookcase(float position) {
//        // Draw the triangle facing straight on.
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, 0.0f, position - 24.5f, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 50.0f, 3.0f, 1.0f);
//
//        drawQuad(mborder2);
//
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, 0.0f, position - 20.f, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 50.0f, 6.0f, 1.0f);
//        drawQuad(mTriangle3Vertices);
//
//    }
//
//    private void drawQuad(final FloatBuffer aTriangleBuffer) {
//        // Pass in the position information
//        aTriangleBuffer.position(mPositionOffset);
//        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
//                mStrideBytes, aTriangleBuffer);
//
//        GLES20.glEnableVertexAttribArray(mPositionHandle);
//
//        // Pass in the color information
//        aTriangleBuffer.position(mColorOffset);
//        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
//                mStrideBytes, aTriangleBuffer);
//
//        GLES20.glEnableVertexAttribArray(mColorHandle);
//
//        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
//        // (which currently contains model * view).
//        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
//
//        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
//        // (which now contains model * view * projection).
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
//
//        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
//    }
//}
