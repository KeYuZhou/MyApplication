package com.example.chirag.librarybooklocator;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

/**
 * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
 * renderers -- the static class GLES20 is used instead.
 */
public class Render2 implements GLSurfaceView.Renderer {
    public volatile float mDeltaX;
    public volatile float mDeltaY;

    public int floornumber;//楼层数
    public int casenumber1;//整体书架数目（1-26）
    public int casenumber2;//单个书架编号（1-10）


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

    private final FloatBuffer mTriangle3Vertices;

    private final FloatBuffer mborder;
    private final FloatBuffer myellow;
    private final FloatBuffer mdown;
    private final FloatBuffer mright;
    private final FloatBuffer mblue3;
    private final FloatBuffer mcircle_yellow;
    private final FloatBuffer mcircle_blue3;
    private final FloatBuffer mcircle_blue4;
    private final FloatBuffer mcircle_red;
    private final FloatBuffer mtriangle_red;

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
    public Render2() {
        MapData a = new MapData();

        float[] border = a.border;
        float[] yellow = a.yellow;
        float[] down = a.down;
        float[] right = a.right;
        float[] blue3 = a.triangle3VerticesData;
        float[] triangle3VerticesData = a.triangle3VerticesData;
        float[] circle_blue3 = a.circle_blue3;
        float[] circle_blue4 = a.circle_blue4;
        float[] circle_red = a.circle_red;
        float[] circle_yellow = a.circle_yellow;

        float[] triangle_red = a.triangle_red;


        // Initialize the buffers.
        mborder = ByteBuffer.allocateDirect(border.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        myellow = ByteBuffer.allocateDirect(yellow.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mdown = ByteBuffer.allocateDirect(down.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mright = ByteBuffer.allocateDirect(right.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mcircle_yellow = ByteBuffer.allocateDirect(circle_yellow.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mcircle_blue3 = ByteBuffer.allocateDirect(circle_blue3.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mcircle_blue4 = ByteBuffer.allocateDirect(circle_blue4.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mcircle_red = ByteBuffer.allocateDirect(circle_red.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mblue3 = ByteBuffer.allocateDirect(blue3.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mtriangle_red = ByteBuffer.allocateDirect(triangle_red.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mborder.put(border).position(0);
        myellow.put(yellow).position(0);
        mdown.put(down).position(0);
        mright.put(right).position(0);


        mtriangle_red.put(triangle_red).position(0);
        mcircle_red.put(circle_red).position(0);
        mblue3.put(blue3).position(0);
        mcircle_yellow.put(circle_yellow).position(0);
        mcircle_blue3.put(circle_blue3).position(0);
        mcircle_blue4.put(circle_blue4).position(0);
        mTriangle3Vertices.put(triangle3VerticesData).position(0);

    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(176 / 255f, 210 / 255f, 212 /255f, 0.0f);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 100.0f;

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
        final float far = 200.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);


        //地图边框
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 145.0f, 197.0f,1.0f);
        drawQuad(mborder);


        //书架群
        for (int i = 0; i < 4; i++) {
            float p = 89 - i * 13.5f;
            drawBookcase(p);
        }
        for (int i = 0; i < 9; i++) {
            float p2 = -91.5f + i * 13.5f;
            drawBookcase(p2);
        }
        //圆形服务台
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -19.0f, 33.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 8.0f, 8.0f, 1.0f);
        draw_circle(mcircle_yellow);
        //学习区域
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -50.0f, 47.0f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 33.0f, 93.0f, 1.0f);
        drawQuad(mTriangle3Vertices);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -41.0f, -42.0f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 15.0f, 93.0f, 1.0f);
        drawQuad(mTriangle3Vertices);
        //墙壁+右边区域
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 13.0f, -2.0f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 3.0f, 191.0f, 1.0f);
        drawQuad(mright);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 36.5f, 73.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 44.0f, 38.0f, 1.0f);
        drawQuad(mright);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 49.0f, 12.0f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 32.0f, 18.5f, 1.0f);
        drawQuad(mright);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 49.0f, -31.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 32.0f, 47.5f, 1.0f);
        drawQuad(mright);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 50.75f, -86.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 35.5f, 20.0f, 1.0f);
        drawQuad(mright);


        draw_wc_sign(26.5f, 76.5f);

        draw_wc_sign(42.5f, -82.5f);

        //门
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 13.0f, 33.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 3.0f, 14.0f, 1.0f);
        drawQuad(myellow);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 13.0f, -28.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 3.0f, 14.0f, 1.0f);
        drawQuad(myellow);

        //下楼梯
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 35.0f, -3f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 4f, 10.5f, 1.0f);
        drawQuad(myellow);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 40.5f, -3f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 3.0f, 10.5f, 1.0f);
        drawQuad(myellow);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 44.25f, -3f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 1.5f, 10.5f, 1.0f);
        drawQuad(myellow);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 46.5f, -3f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 0.5f, 10.5f, 1.0f);
        drawQuad(myellow);


        //画出目标图书的位置
        draw_position(casenumber1, casenumber2, floornumber);

    }

    //    /**
//     * Draws a triangle from the given vertex data.
//     *
//     * @param aTriangleBuffer The buffer containing the vertex data.
//     */
    private void draw_position(int casenumber1, int casenumber2, int floornumber) {
        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
//        floornumber=9;
//        casenumber1=21;
//        casenumber2=7;
        float positionX = 0;
        float positionY = 0;
        if (casenumber1 == 1 || casenumber1 == 2) {
            positionY = 89.25f;
        } else if (casenumber1 == 3 || casenumber1 == 4) {
            positionY = 75.75f;
        } else if (casenumber1 == 5 || casenumber1 == 6) {
            positionY = 62.25f;
        } else if (casenumber1 == 7 || casenumber1 == 8) {
            positionY = 48.75f;
        } else if (casenumber1 == 9 || casenumber1 == 10) {
            positionY = 16.75f;
        } else if (casenumber1 == 11 || casenumber1 == 12) {
            positionY = 3.25f;
        } else if (casenumber1 == 13 || casenumber1 == 14) {
            positionY = -10.25f;
        } else if (casenumber1 == 15 || casenumber1 == 16) {
            positionY = -23.75f;
        } else if (casenumber1 == 17 || casenumber1 == 18) {
            positionY = -37.35f;
        } else if (casenumber1 == 19 || casenumber1 == 20) {
            positionY = -50.75f;
        } else if (casenumber1 == 21 || casenumber1 == 22) {
            positionY = -64.25f;
        } else if (casenumber1 == 23 || casenumber1 == 24) {
            positionY = -77.75f;
        } else {
            positionY = -91.25f;
        }

        if (casenumber2 == 1 || casenumber2 == 6) {
            positionX = -23.0f;
        } else if (casenumber2 == 2 || casenumber2 == 7) {
            positionX = -17.0f;
        } else if (casenumber2 == 3 || casenumber2 == 8) {
            positionX = -11.0f;
        } else if (casenumber2 == 4 || casenumber2 == 9) {
            positionX = -5.0f;
        } else {
            positionX = 1.0f;
        }

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, positionX, positionY + 3, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 6.0f, 6.0f, 1.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0, 1, 0);
        draw_circle(mcircle_red);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, positionX, positionY + 3, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 4.5f, 4.5f, 1.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0, 1, 0);
        draw_circle(mcircle_blue4);

    }

    private void draw_wc_sign(float X, float Y) {
        //女
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, X, Y, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 7.0f, 7.0f, 1.0f);
        draw_circle(mcircle_blue3);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, X, Y, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.0f, 5.0f, 1.0f);
        draw_circle(mcircle_blue4);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, X, Y - 9.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 3.0f, 8.0f, 1.0f);
        drawQuad(mblue3);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, X, Y - 9.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 8.0f, 3.0f, 1.0f);
        drawQuad(mblue3);


        //男
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, X + 20.0f, Y, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 7.0f, 7.0f, 1.0f);
        draw_circle(mcircle_blue3);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, X + 20.0f, Y, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.0f, 5.0f, 1.0f);
        draw_circle(mcircle_blue4);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, X + 20, Y - 9.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 3.0f, 8.0f, 1.0f);
        drawQuad(mblue3);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, X + 18, Y - 13.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, -20.0f, 0, 0, 1);
        Matrix.scaleM(mModelMatrix, 0, 5.0f, 2.0f, 1.0f);
        drawQuad(mblue3);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, X + 22, Y - 13.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, 20.0f, 0, 0, 1);
        Matrix.scaleM(mModelMatrix, 0, 5.0f, 2.0f, 1.0f);
        drawQuad(mblue3);


    }

    private void drawBookcase(float position) {
        // 下
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -11.0f, position, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 33f, 9f, 1.0f);
        drawQuad(mdown);

        //上
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -11.0f, position + 6.0f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 33.0f,3.0f,1.0f);
        drawQuad(mTriangle3Vertices);

        //书架1【-12】
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -23.0f, position + 0.25f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.5f, 7.5f, 1.0f);
        drawQuad(mright);
        //书架2
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -17.0f, position + 0.25f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.5f, 6.5f, 1.0f);
        drawQuad(mright);
        //书架3
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -11.0f, position + 0.25f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.5f, 6.5f, 1.0f);
        drawQuad(mright);
        //书架4
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -5.0f, position + 0.25f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.5f, 6.5f, 1.0f);
        drawQuad(mright);
        //书架5
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 1.0f, position + 0.25f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.5f, 6.5f, 1.0f);
        drawQuad(mright);

        //书架底
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -23.0f, position - 3.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.5f, 1.0f, 1.0f);
        drawQuad(mborder);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -17.0f, position - 3.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.5f, 1.0f, 1.0f);
        drawQuad(mborder);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -11.0f, position - 3.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.5f, 1.0f, 1.0f);
        drawQuad(mborder);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, -5.0f, position - 3.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.5f, 1.0f, 1.0f);
        drawQuad(mborder);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 1.0f, position - 3.5f, 0.0f);
        Matrix.scaleM(mModelMatrix, 0, 5.5f, 1.0f, 1.0f);
        drawQuad(mborder);


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

    private void draw_circle(final FloatBuffer aTriangleBuffer) {
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
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 52);
    }
}
//
///**
// * Created by effy on 2018/4/23.
// */
//
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//
//import javax.microedition.khronos.egl.EGLConfig;
//import javax.microedition.khronos.opengles.GL10;
//
//import android.opengl.GLES20;
//import android.opengl.GLSurfaceView;
//import android.opengl.Matrix;
//import android.os.SystemClock;
//
///**
// * This class implements our custom renderer. Note that the GL10 parameter passed in is unused for OpenGL ES 2.0
// * renderers -- the static class GLES20 is used instead.
// */
//public class Render2 implements GLSurfaceView.Renderer {
//    public volatile float mDeltaX;
//    public volatile float mDeltaY;
//
//    public float floornumber;
//    public float casenumber1;
//    public int casenumber2;
//    private float bookpositionX;
//    private float bookpositionY;
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
//    private final FloatBuffer mred;
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
//    public Render2() {
//        MapData a = new MapData();
//
//        float[] border = a.border;
//        float[] yellow = a.yellow;
//        float[] red = a.red;
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
//        mred = ByteBuffer.allocateDirect(red.length * mBytesPerFloat)
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
//        mred.put(red).position(0);
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
//        final float eyeX = 12.0f;
//        final float eyeY = 30.0f;
//        final float eyeZ = 40.0f;
//
//        // We are looking toward the distance
//        final float lookX = 12.0f;
//        final float lookY = 30.0f;
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
//        Matrix.translateM(mModelMatrix, 0, 11.0f, 30.0f, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 56.0f, 70.0f, 1.0f);
//        drawQuad(mborder);
//
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, -2.0f, 30.0f, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 28.0f, 66.0f, 1.0f);
//        drawQuad(mTriangle2Vertices);
//
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, 12.0f, 3.0f, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 8.0f, 12.0f, 1.0f);
//        drawQuad(mTriangle2Vertices);
//
//
//        if (floornumber == 9) {
//            //书柜群9楼
//            for (float i = 0; i < 32; i++) {
//
//                Matrix.setIdentityM(mModelMatrix, 0);
//                //Matrix.translateM(mModelMatrix,0,0.0f,i*2.0f,0.0f);
//                drawBookcase(i * 2);
//
//            }
//            Matrix.setIdentityM(mModelMatrix, 0);
//            Matrix.translateM(mModelMatrix, 0, 13.0f, 4.0f, 0.0f);
//            Matrix.scaleM(mModelMatrix, 0, 1.0f, 9.0f, 1.0f);
//            drawQuad(mTriangle1Vertices);
//
//            Matrix.setIdentityM(mModelMatrix, 0);
//            Matrix.translateM(mModelMatrix, 0, 13.0f, 4.0f, 0.0f);
//            Matrix.scaleM(mModelMatrix, 0, 0.25f, 9.0f, 1.0f);
//            drawQuad(mTriangle3Vertices);
//
//            Matrix.setIdentityM(mModelMatrix, 0);
//            Matrix.translateM(mModelMatrix, 0, 9.0f, 4.0f, 0.0f);
//            Matrix.scaleM(mModelMatrix, 0, 1.0f, 9.0f, 1.0f);
//            drawQuad(mTriangle1Vertices);
//
//            Matrix.setIdentityM(mModelMatrix, 0);
//            Matrix.translateM(mModelMatrix, 0, 9.0f, 4.0f, 0.0f);
//            Matrix.scaleM(mModelMatrix, 0, 0.25f, 9.0f, 1.0f);
//            drawQuad(mTriangle3Vertices);
//
//            //！标志物-门（9楼）
//            Matrix.setIdentityM(mModelMatrix, 0);
//            Matrix.translateM(mModelMatrix, 0, 12.0f, 46.0f, 0.0f);
//            Matrix.scaleM(mModelMatrix, 0, 3.0f, 4.0f, 1.0f);
//            drawQuad(myellow);
//
//            Matrix.setIdentityM(mModelMatrix, 0);
//            Matrix.translateM(mModelMatrix, 0, 12.0f, 16.0f, 0.0f);
//            Matrix.scaleM(mModelMatrix, 0, 3.0f, 4.0f, 1.0f);
//            drawQuad(myellow);
//
//        }
//        if (floornumber == 8) {
//            //书柜群(8楼）
//            for (float i = 0; i < 32; i++) {
//
//                Matrix.setIdentityM(mModelMatrix, 0);
//                //Matrix.translateM(mModelMatrix,0,0.0f,i*2.0f,0.0f);
//                drawBookcase(i * 2);
//
//            }
//
//        }
//
//
//        //学习区域
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, -11.5f, 30.0f, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 6.0f, 65.0f, 1.0f);
//        drawQuad(mTriangle3Vertices);
//
//        //书本所在的书架定位
//        bookpositionY = casenumber1 * 2.1875f;
//        switch (casenumber2) {
//            case 1:
//                bookpositionX = -4.5f;
//            case 2:
//                bookpositionX = -1.5f;
//            case 3:
//                bookpositionX = 1.5f;
//            case 4:
//                bookpositionX = 4.5f;
//            case 5:
//                bookpositionX = -4.5f;
//            case 6:
//                bookpositionX = -1.5f;
//            case 7:
//                bookpositionX = 1.5f;
//            case 8:
//                bookpositionX = 4.5f;
//        }
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, bookpositionX, bookpositionY, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 3.0f, 0.375f, 1.0f);
//        drawQuad(mred);
//
//
//        //8楼9楼右边的标志区域(还未结束)
//
////        // Draw one translated a bit down and rotated to be flat on the ground.
////        Matrix.setIdentityM(mModelMatrix, 0);
////        Matrix.translateM(mModelMatrix, 0, 0.0f, -1.0f, 0.0f);
////        Matrix.rotateM(mModelMatrix, 0, 90.0f, 1.0f, 0.0f, 0.0f);
////        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
////        drawTriangle(mTriangle2Vertices);
//
////        // Draw one translated a bit to the right and rotated to be facing to the left.
////        Matrix.setIdentityM(mModelMatrix, 0);
////        Matrix.translateM(mModelMatrix, 0, 1.0f, 0.0f, 0.0f);
////        Matrix.rotateM(mModelMatrix, 0, 90.0f, 0.0f, 1.0f, 0.0f);
////        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
////        drawTriangle(mTriangle3Vertices);
//
//
//    }
//
//    //    /**
////     * Draws a triangle from the given vertex data.
////     *
////     * @param aTriangleBuffer The buffer containing the vertex data.
////     */
////    private void drawTriangle(final FloatBuffer aTriangleBuffer)
////    {
////        // Pass in the position information
////        aTriangleBuffer.position(mPositionOffset);
////        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
////                mStrideBytes, aTriangleBuffer);
////
////        GLES20.glEnableVertexAttribArray(mPositionHandle);
////
////        // Pass in the color information
////        aTriangleBuffer.position(mColorOffset);
////        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false,
////                mStrideBytes, aTriangleBuffer);
////
////        GLES20.glEnableVertexAttribArray(mColorHandle);
////
////        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
////        // (which currently contains model * view).
////        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
////
////        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
////        // (which now contains model * view * projection).
////        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
////
////        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
////        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
////    }
//    private void drawBookcase(float position) {
//        // Draw the triangle facing straight on.
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, 0.0f, position - 0.5f, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 12.0f, 1.0f, 1.0f);
//
//        drawQuad(mTriangle1Vertices);
//
//        Matrix.setIdentityM(mModelMatrix, 0);
//        Matrix.translateM(mModelMatrix, 0, 0.0f, position - 0.5f, 0.0f);
//        Matrix.scaleM(mModelMatrix, 0, 12.0f, 0.2f, 1.0f);
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