package org.newbiehacker;

import java.awt.*;
import java.util.Vector;

/**
 * Copyright 2006 James Lawrence
 * Date: 01-Jun-2007
 * Time: 17:04:52
 * Modification and redistribution without explicit permission by the creator(s) is prohibited
 * This source may be modified for personal use as long as the original author is accredited
 */
public final class Face extends Polygon {
    public final Vertex[] vertices;
    public final Vertex normal;

    public Face(final Vertex... vertices) {
        this.vertices = vertices;
        npoints = vertices.length;
        xpoints = new int[vertices.length];
        ypoints = new int[vertices.length];
        for(int i = 0; i < vertices.length; i++) {
            xpoints[i] = (int) vertices[i].x;
            ypoints[i] = (int) vertices[i].y;
        }
        if(vertices.length < 3)
            throw new RuntimeException("A Face must have more than 2 vertices");
        normal = getNormal(this);
    }

    public static Face applyTransform(final Matrix m, final Face f) {
        final Vertex[] nVerts = new Vertex[f.vertices.length];
        for(int i = 0; i < nVerts.length; i++)
            nVerts[i] = Matrix.applyTransform(m, f.vertices[i]);
        return new Face(nVerts);
    }

    public static Vertex getNormal(Face f) {
        return Vertex.crossProduct(Vertex.substract(f.vertices[0], f.vertices[1]), Vertex.substract(f.vertices[2], f.vertices[1]));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Face {");
        for(Vertex v: vertices)
            sb.append(v).append(", ");
        if(sb.length() > 2)
            sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }
}