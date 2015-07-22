package org.newbiehacker;

/**
 * Copyright 2006 James Lawrence
 * Date: 01-Jun-2007
 * Time: 14:33:11
 * Modification and redistribution without explicit permission by the creator(s) is prohibited
 * This source may be modified for personal use as long as the original author is accredited
 */
public final class Matrix {
    public float[][] matrix;
    private float[][] trans;

    public Matrix() {
        this.matrix = new float[4][4];
        this.trans = new float[4][4];
        matrix[0][0] = 1;
        matrix[1][1] = 1;
        matrix[2][2] = 1;
        matrix[3][3] = 1;
    }

    public Matrix(final float[][] matrix) {
        this.matrix = matrix;
    }

    public void multiply(final Matrix b) {
        multiply(b.matrix);
    }

    public void multiply(final float[][] b) {
        float[][] n = new float[4][4];
        int i;
        for(i = 0; i < 3; i++) {
            n[i][0] = matrix[i][0] * b[0][0] + matrix[i][1] * b[1][0] + matrix[i][2] * b[2][0] + matrix[i][3] * b[3][0];
            n[i][1] = matrix[i][0] * b[0][1] + matrix[i][1] * b[1][1] + matrix[i][2] * b[2][1] + matrix[i][3] * b[3][1];
            n[i][2] = matrix[i][0] * b[0][2] + matrix[i][1] * b[1][2] + matrix[i][2] * b[2][2] + matrix[i][3] * b[3][2];
            n[i][3] = matrix[i][0] * b[0][3] + matrix[i][1] * b[1][3] + matrix[i][2] * b[2][3] + matrix[i][3] * b[3][3];
        }
        this.matrix = n;
    }

    public void translate(final float x, final float y, final float z) {
        trans = new float[][] {
                {1, 0, 0, x},
                {0, 1, 0, y},
                {0, 0, 1, z},
                {0, 0, 0, 1}
        };
        multiply(trans);
    }

    public void scale(final float x, final float y, final float z) {
        trans = new float[][] {
                {x, 0, 0, 0},
                {0, y, 0, 0},
                {0, 0, z, 0},
                {0, 0, 0, 1}
        };
        multiply(trans);
    }

    public void rotateX(final float theta) {
        final float cos = (float) StrictMath.cos(theta);
        final float sin = (float) StrictMath.sin(theta);
        trans = new float[][] {
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}
        };
        multiply(trans);
    }

    public void rotateY(final float theta) {
        final float cos = (float) StrictMath.cos(theta);
        final float sin = (float) StrictMath.sin(theta);
        trans = new float[][] {
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}
        };
        multiply(trans);
    }

    public void rotateZ(final float theta) {
        final float cos = (float) StrictMath.cos(theta);
        final float sin = (float) StrictMath.sin(theta);
        trans = new float[][] {
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        multiply(trans);
    }

    public static Vertex applyTransform(final Matrix m, final Vertex v) {
        final float[][] matrix = m.matrix;
        final float x = (v.x * matrix[0][0]) + (v.y * matrix[0][1]) + (v.z * matrix[0][2]) + (matrix[0][3]);
        final float y = (v.x * matrix[1][0]) + (v.y * matrix[1][1]) + (v.z * matrix[1][2]) + (matrix[1][3]);
        final float z = (v.x * matrix[2][0]) + (v.y * matrix[2][1]) + (v.z * matrix[2][2]) + (matrix[2][3]);
        return new Vertex(x, y, z);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Matrix {\n");
        for (int i = 0; i < 4; i++) {
            sb.append("\t{");
            for (int j = 0; j < 4; j++)
                sb.append(matrix[i][j]).append(j == 3 ? "" : ", ");
            sb.append("}\n");
        }
        sb.append("}");
        return sb.toString();
    }
}