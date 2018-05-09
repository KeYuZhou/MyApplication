package com.example.chirag.librarybooklocator;


import java.util.ArrayList;

/**
 * Created by jo on 2018/4/18.
 */

public class MapData {
    private float[] red_color = {221 / 255f, 70 / 255f, 56 / 255f, 1.0f,};
    private float[] yellow_color = {250 / 255f, 171 / 255f, 33 / 255f, 1.0f,};
    private float[] border_color = {113 / 255f, 170 / 255f, 174 / 255f, 1.0f,};
    private float[] border2_color = {65 / 255f, 95 / 255f, 121 / 255f, 1.0f,};
    private float[] right_color = {122 / 255f, 187 / 255f, 191 / 255f, 1.0f,};
    private float[] down_color = {160 / 255f, 199 / 255f, 201 / 255f, 1.0f,};
    private float[] triangle1_color = {12 / 255f, 53 / 255f, 85 / 255f, 1.0f,};//最深
    private float[] triangle2_color = {159 / 255f, 199 / 255f, 201 / 255f, 1.0f,};
    private float[] triangle3_color = {214 / 255f, 228 / 255f, 237 / 255f, 1.0f};
    private float[] triangle4_color = {1f, 1f, 1f, 1.0f,};//最浅

    private float[] create_circle(float[] b, float rad) {
        ArrayList<Float> data = new ArrayList<>();
        data.add(0.0f);             //设置圆心坐标
        data.add(0.0f);
        data.add(0.0f);
        data.add(b[0]);
        data.add(b[1]);
        data.add(b[2]);
        data.add(b[3]);
        float angDegSpan = 360f / 50;
        for (float i = 0; i < 360 + angDegSpan; i += angDegSpan) {
            data.add((float) (rad * Math.sin(i * Math.PI / 180f)));
            data.add((float) (rad * Math.cos(i * Math.PI / 180f)));
            data.add(0.0f);
            data.add(b[0]);
            data.add(b[1]);
            data.add(b[2]);
            data.add(b[3]);
        }
        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    private float[] create_square(float[] b) {
        ArrayList<Float> data = new ArrayList<>();
        data.add(-0.5f);
        data.add(0.5f);
        data.add(0.0f);
        data.add(b[0]);
        data.add(b[1]);
        data.add(b[2]);
        data.add(b[3]);

        data.add(-0.5f);
        data.add(-0.5f);
        data.add(0.0f);
        data.add(b[0]);
        data.add(b[1]);
        data.add(b[2]);
        data.add(b[3]);

        data.add(0.5f);
        data.add(-0.5f);
        data.add(0.0f);
        data.add(b[0]);
        data.add(b[1]);
        data.add(b[2]);
        data.add(b[3]);

        data.add(0.5f);
        data.add(0.5f);
        data.add(0.0f);
        data.add(b[0]);
        data.add(b[1]);
        data.add(b[2]);
        data.add(b[3]);

        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    private float[] create_triangle(float[] b) {
        ArrayList<Float> data = new ArrayList<>();
        data.add(-0.5f);
        data.add(0.4375f);
        data.add(0.0f);
        data.add(b[0]);
        data.add(b[1]);
        data.add(b[2]);
        data.add(b[3]);

        data.add(-0.5f);
        data.add(0.4375f);
        data.add(0.0f);
        data.add(b[0]);
        data.add(b[1]);
        data.add(b[2]);
        data.add(b[3]);

        data.add(0.0f);
        data.add(-0.4375f);
        data.add(0.0f);
        data.add(b[0]);
        data.add(b[1]);
        data.add(b[2]);
        data.add(b[3]);


        float[] f = new float[data.size()];
        for (int i = 0; i < f.length; i++) {
            f[i] = data.get(i);
        }
        return f;
    }

    float[] down = create_square(down_color);
    float[] right = create_square(right_color);
    float[] border = create_square(border_color);
    float[] border2 = create_square(border2_color);
    float[] triangle1VerticesData = create_square(triangle1_color);
    float[] triangle2VerticesData = create_square(triangle2_color);
    float[] triangle3VerticesData = create_square(triangle3_color);
    float[] triangle4VerticesData = create_square(triangle4_color);
    float[] yellow = create_square(yellow_color);
    float[] red = create_square(red_color);
    final float[] circle_blue3 = create_circle(triangle3_color, 1);
    final float[] circle_blue4 = create_circle(triangle4_color, 1);
    final float[] circle_yellow = create_circle(yellow_color, 1);
    final float[] circle_red = create_circle(red_color, 1);
    final float[] triangle_red = create_triangle(red_color);
    //颜色：
    //背景：176,210,212
    //地图边框：113,170,174
    //由浅到深：
    //255,255,255
    //214,228,237
    //159,199,201
    //12,53,85
    //右边的：
    //122,187,191
    //黄色：250,171,33
    //红色：221,70,56


}


//
//
//
///**
// * Created by jo on 2018/4/18.
// */
//
//
//public class MapData {
//    //颜色：
//    //背景：176,210,212
//    //地图边框：113,170,174
//    //由浅到深：
//    //255,255,255
//    //214,228,237
//    //159,199,201
//    //12,53,85
//    //右边的：
//    //122,187,191
//    //黄色：250,171,33
//    //红色：221,70,56
//    // Define points for equilateral triangles.
//
//    // This triangle is red, green, and blue.
//
//    // This triangle is yellow, cyan, and magenta.
//    final float[] border = {
//            // X, Y, Z,
//            // R, G, B, A
//            //地图边框：113,170,174
//            -0.5f, 0.5f, 0.0f,
//            113 / 255f, 170 / 255f, 174 / 255f, 1.0f,
//
//            -0.5f, -0.5f, 0.0f,
//            113 / 255f, 170 / 255f, 174 / 255f, 1.0f,
//
//            0.5f, -0.5f, 0.0f,
//            113 / 255f, 170 / 255f, 174 / 255f, 1.0f,
//
//            0.5f, 0.5f, 0.0f,
//            113 / 255f, 170 / 255f, 174 / 255f, 1.0f,
//    };
//    final float[] border2 = {
//            // X, Y, Z,
//            // R, G, B, A
//            //地图边框：113,170,174
//            -0.5f, 0.5f, 0.0f,
//            65 / 255f, 95 / 255f, 121 / 255f, 1.0f,
//
//            -0.5f, -0.5f, 0.0f,
//            65 / 255f, 95 / 255f, 121 / 255f, 1.0f,
//
//            0.5f, -0.5f, 0.0f,
//            65 / 255f, 95 / 255f, 121 / 255f, 1.0f,
//
//            0.5f, 0.5f, 0.0f,
//            65 / 255f, 95 / 255f, 121 / 255f, 1.0f,
//    };
//    final float[] triangle1VerticesData = {
//            // X, Y, Z,
//            // R, G, B, A
//            //第一深蓝色矩形
//            -0.5f, 0.5f, 0.0f,
//            12 / 255f, 53 / 255f, 85 / 255f, 1.0f,
//
//            -0.5f, -0.5f, 0.0f,
//            12 / 255f, 53 / 255f, 85 / 255f, 1.0f,
//
//            0.5f, -0.5f, 0.0f,
//            12 / 255f, 53 / 255f, 85 / 255f, 1.0f,
//
//            0.5f, 0.5f, 0.0f,
//            12 / 255f, 53 / 255f, 85 / 255f, 1.0f
//    };
//
//    final float[] triangle2VerticesData = {
//
//            // X, Y, Z,
//            // R, G, B, A
//            //第二深蓝色矩形
//            -0.5f, 0.5f, 0.0f,
//            159 / 255f, 199 / 255f, 201 / 255f, 1.0f,
//
//            -0.5f, -0.5f, 0.0f,
//            159 / 255f, 199 / 255f, 201 / 255f, 1.0f,
//
//            0.5f, -0.5f, 0.0f,
//            159 / 255f, 199 / 255f, 201 / 255f, 1.0f,
//
//            0.5f, 0.5f, 0.0f,
//            159 / 255f, 199 / 255f, 201 / 255f, 1.0f,
//    };
//
//
//    // This triangle is white, gray, and black.
//    final float[] triangle3VerticesData = {
//            // X, Y, Z,
//            // R, G, B, A
//            //第二浅蓝色矩形
//            -0.5f, 0.5f, 0.0f,
//            214 / 255f, 228 / 255f, 237 / 255f, 1.0f,
//
//            -0.5f, -0.5f, 0.0f,
//            214 / 255f, 228 / 255f, 237 / 255f, 1.0f,
//
//            0.5f, -0.5f, 0.0f,
//            214 / 255f, 228 / 255f, 237 / 255f, 1.0f,
//
//            0.5f, 0.5f, 0.0f,
//            214 / 255f, 228 / 255f, 237 / 255f, 1.0f
//    };
//    final float[] triangle4VerticesData = {
//            // X, Y, Z,
//            // R, G, B, A
//            //第一浅蓝色矩形(白色)
//            -0.5f, 0.5f, 0.0f,
//            1f, 1f, 1f, 1.0f,
//
//            -0.5f, -0.5f, 0.0f,
//            1f, 1f, 1f, 1.0f,
//
//            0.5f, -0.5f, 0.0f,
//            1f, 1f, 1f, 1.0f,
//
//            0.5f, 0.5f, 0.0f,
//            1f, 1f, 1f, 1.0f,
//    };
//    final float[] yellow = {
//            // X, Y, Z,
//            // R, G, B, A
//            //黄色 //黄色：250,171,33
//            -0.5f, 0.5f, 0.0f,
//            250 / 255f, 171 / 255f, 33 / 255f, 1.0f,
//
//            -0.5f, -0.5f, 0.0f,
//            250 / 255f, 171 / 255f, 33 / 255f, 1.0f,
//
//            0.5f, -0.5f, 0.0f,
//            250 / 255f, 171 / 255f, 33 / 255f, 1.0f,
//
//            0.5f, 0.5f, 0.0f,
//            250 / 255f, 171 / 255f, 33 / 255f, 1.0f,
//    };
//    final float[] red = {
//            // X, Y, Z,
//            // R, G, B, A
//            //红色：221,70,56
//            -0.5f, 0.5f, 0.0f,
//            221 / 255f, 70 / 255f, 56 / 255f, 1.0f,
//
//            -0.5f, -0.5f, 0.0f,
//            221 / 255f, 70 / 255f, 56 / 255f, 1.0f,
//
//            0.5f, -0.5f, 0.0f,
//            221 / 255f, 70 / 255f, 56 / 255f, 1.0f,
//
//            0.5f, 0.5f, 0.0f,
//            221 / 255f, 70 / 255f, 56 / 255f, 1.0f,
//    };
//}
