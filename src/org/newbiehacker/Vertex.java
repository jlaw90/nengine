package org.newbiehacker;

/**
 * Copyright 2006 James Lawrence
 * Date: 01-Jun-2007
 * Time: 14:30:29
 * Modification and redistribution without explicit permission by the creator(s) is prohibited
 * This source may be modified for personal use as long as the original author is accredited
 */
public final class Vertex {
    public final float x;
    public final float y;
    public final float z;

    public Vertex(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vertex add(final Vertex a, final Vertex b) {
        return new Vertex(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vertex substract(final Vertex a, final Vertex b) {
        return new Vertex(a.x - b.x, a.y - b.y,  a.z - b.z);
    }

    public static float dotProduct(final Vertex v, final Vertex v1) {
        return v.x * v1.x + v.y * v1.y + v.z * v1.z;
    }

    public static Vertex crossProduct(final Vertex v, final Vertex v1) {
        return new Vertex(v.y * v1.z - v.z * v1.y, v.z * v1.x - v.x * v1.z, v.x * v1.y - v.y * v1.x);
    }

    public static float length(final Vertex v) {
        return (v.x * v.x + v.y * v.y + v.z * v.z) / 2;
    }

    public static Vertex normal(final Vertex v) {
        final float length = length(v);
        return new Vertex(v.x / length, v.y / length, v.z / length);
    }

    public String toString() {
        return "Vertex [" + x + ", " +  y + ", " + z + "]";
    }
}