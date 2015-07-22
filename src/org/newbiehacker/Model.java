package org.newbiehacker;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Copyright 2006 James Lawrence
 * Date: 01-Jun-2007
 * Time: 17:32:48
 * Modification and redistribution without explicit permission by the creator(s) is prohibited
 * This source may be modified for personal use as long as the original author is accredited
 */
public final class Model {
    public final Face[] faces;
    public final Matrix transform;
    public float x;
    public float y;
    public float z;

    public Model(final float x, final float y, final float z, final Face[] faces) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.faces = faces;
        this.transform = new Matrix();
    }

    public Model(final float x, final float y, final float z, final List<Face> faces) {
        this(x, y, z, faces.toArray(new Face[faces.size()]));
    }

    public static Model transformFaces(final Matrix m, final Model mod) {
        final Face[] nFaces = new Face[mod.faces.length];
        for(int i = 0; i < nFaces.length; i++)
            nFaces[i] = Face.applyTransform(m, mod.faces[i]);
        return new Model(mod.x, mod.y, mod.z, nFaces);
    }

    public static Model transformThis(final Matrix m, final Model mod) {
        final Vertex trans = Matrix.applyTransform(m, new Vertex(mod.x, mod.y, mod.z));
        return new Model(trans.x, trans.y, trans.z, mod.faces);
    }

    public static Model loadFromObj(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        final HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
        final ArrayList<Face> faces = new ArrayList<Face>();
        int verticesPos = 1;
        String line;
        while((line = br.readLine()) != null) {
            line = line.trim();
            if(line.startsWith("#") || line.equals(""))
                continue;
            String[] data = line.split("\\s+");
            if(data[0].equals("g"))
                System.out.println("Group is unsupported xD: " + line);
            else if(data[0].equals("vn"))
                System.out.println("Vector normal is unsupported (wtf does it mean?): " + line);
            else if(data[0].equals("vt"))
                System.out.println("Vector texture is unsupported: " + line);
            else if(data[0].equals("v")) {
                float x = Float.parseFloat(data[1]);
                float y = Float.parseFloat(data[2]);
                float z = Float.parseFloat(data[3]);
                vertices.put(verticesPos++, new Vertex(x, y, z));
            } else if(data[0].equals("f")) {
                String[] fData;
                final ArrayList<Vertex> ourFace = new ArrayList<Vertex>();
                for(int i = 1; i < data.length; i++) {
                    fData = data[i].split("/");
                    ourFace.add(vertices.get(Integer.parseInt(fData[0])));
                }
                faces.add(new Face(ourFace.toArray(new Vertex[ourFace.size()])));
            }
        }
        br.close();
        Model m = new Model(0, 0, 0, faces);
        vertices.clear();
        faces.clear();
        return m;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Model [" + x + ", " + y + ", " + z + "] {");
        for(Face f: faces)
            sb.append(f).append(", ");
        if(sb.length() > 2)
            sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }
}