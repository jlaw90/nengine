package org.newbiehacker;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Copyright 2006 James Lawrence
 * Date: 01-Jun-2007
 * Time: 15:00:28
 * Modification and redistribution without explicit permission by the creator(s) is prohibited
 * This source may be modified for personal use as long as the original author is accredited
 */
public class Test extends Applet implements Runnable, Comparator<Face>, MouseListener, MouseMotionListener, MouseWheelListener {
    private static final String MODEL_F = "right.obj";
    private static final boolean drawBounds = false, drawWireframe = true;
    private static final Matrix TRANSFORM;

    private static BufferedImage buffer;
    private static Vertex camera, plane_normal;
    private static Model model;
    private static long lastFpsUpdate;
    private static int FTS, vertices, faces, mouse;
    private static String allVerts, allFaces, FPS;
    private static Point clickPoint, lastPoint;

    public static void main(String[] args) {
        JFrame jf = new JFrame("Lel, I has a face");
        jf.setPreferredSize(new Dimension(765, 503));
        Test t = new Test();
        jf.add(t);
        jf.pack();
        try {
            model = Model.loadFromObj(new FileInputStream(MODEL_F));
            allFaces = "Faces: " + model.faces.length;
            int verts = 0;
            for (Face f : model.faces)
                verts += f.vertices.length;
            allVerts = "Vertices: " + verts;
            model.transform.translate(200, 200, 200);
            model.transform.scale(100, 100, 100);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        jf.setVisible(true);
        new Thread(t).run();
    }

    public void init() {
        try {
            model = Model.loadFromObj(TRANSFORM.getClass().getClassLoader().getResourceAsStream(MODEL_F));
            allFaces = "Faces: " + model.faces.length;
            int verts = 0;
            for (Face f : model.faces)
                verts += f.vertices.length;
            allVerts = "Vertices: " + verts;
            model.transform.translate(200, 200, 200);
            model.transform.scale(100, 100, 100);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        new Thread(this).start();
    }

    public void run() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        try {
            while (true) {
                paint(getGraphics());
                Thread.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g1) {
        if (buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        FTS++;
        if (lastFpsUpdate == 0)
            lastFpsUpdate = System.currentTimeMillis();
        else if (System.currentTimeMillis() - lastFpsUpdate >= 1000) {
            FPS = "FPS: " + FTS;
            FTS = 0;
            lastFpsUpdate = System.currentTimeMillis();
        }
        Graphics g = buffer.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());

        drawModel(Model.transformThis(TRANSFORM, Model.transformFaces(TRANSFORM, Model.transformFaces(model.transform, model))), g);
        //model.transform.rotateX(ONE_DEGREE);
        //model.transform.rotateY(TWO_DEGREES);
        model.transform.rotateZ((float) Math.toRadians(1));

        g.setColor(Color.GREEN);
        g.drawString(FPS, 0, 12);

        g.drawString(allVerts, 0, 24);
        g.drawString(allFaces, 0, 36);
        g.drawString("Visible vertices: " + vertices, 0, 48);
        g.drawString("Visible faces: " + faces, 0, 60);
        g.drawString("Camera: " + camera, 0, 72);

        g1.drawImage(buffer, 0, 0, null);
    }

    public int compare(Face f, Face f1) {
        // Todo: involve camera some how =/
        return (int) (camera.z - f.normal.z - f1.normal.z);
    }

    private void drawModel(final Model model, final Graphics g) {
        final Point mP = getLocation(model);
        final int startX = mP.x;
        final int startY = mP.y;
        Arrays.sort(model.faces, this);
        Face face;
        // Calculate the number of visible faces
        int faceCount = 0;
        int vertCount = 0;
        for (int i = 0; i < model.faces.length; i++) {
            if (Vertex.dotProduct(model.faces[i].normal, plane_normal) < 0)
                continue;
            ++faceCount;
            vertCount += model.faces[i].vertices.length;
        }
        final int[] colors = new int[faceCount];
        final int colFactor = 255 / faceCount;
        for (int i = 0; i < colors.length; i++)
            colors[i] = colFactor * (i + 1);
        int curFace = 0;
        for (int i = 0; i < model.faces.length; i++) {
            face = model.faces[i];
            if (Vertex.dotProduct(face.normal, plane_normal) < 0)
                continue;
            g.translate(startX, startY);
            if (clickPoint != null && face.intersects(clickPoint.x, clickPoint.y, 1, 1))
                g.setColor(Color.RED);
            else
                g.setColor(new Color(colors[curFace]));
            curFace++;
            g.fillPolygon(face);
            if (drawWireframe) {
                g.setColor(Color.BLACK);
                g.drawPolygon(face);
            }
            if (drawBounds) {
                g.setColor(Color.RED);
                Rectangle bounds = face.getBounds();
                g.drawRect(startX + bounds.x, startY + bounds.y, bounds.width, bounds.height);
            }
            g.translate(-startX, -startY);
        }
        faces = faceCount;
        vertices = vertCount;
    }

    private static Point getLocation(final Model model) {
        final int x = (int) (model.x + (model.z / camera.z));
        final int y = (int) (model.y + (model.z / camera.z));
        return new Point(x, y);
    }

    public void mouseClicked(MouseEvent e) {
        clickPoint = e.getPoint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        lastPoint = e.getPoint();
        mouse = e.getButton();
    }

    public void mouseReleased(MouseEvent e) {
        lastPoint = null;
    }

    public void mouseDragged(MouseEvent e) {
        System.out.println(mouse);
        if (mouse != 1) {
            TRANSFORM.translate(e.getX() - lastPoint.x, e.getY() - lastPoint.y, model.y);
        } else {
            model.transform.rotateY((float) Math.toRadians(-(e.getX() - lastPoint.x)));
            model.transform.rotateX((float) Math.toRadians(-(e.getY() - lastPoint.y)));
        }
        lastPoint = e.getPoint();
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        camera = new Vertex(camera.x, camera.y, (float) Math.toRadians(Math.toDegrees(camera.z) + e.getUnitsToScroll()));
    }

    static {
        // The plane_normal is used to cull backfaces (it WILL be used in conjunction with the camera in future)
        plane_normal = new Vertex(0, 0, 1); // The front of our plane faces upwards
        // Todo: CAMERA!
        camera = new Vertex((float) Math.toRadians(0), (float) Math.toRadians(0), (float) Math.toRadians(0));
        TRANSFORM = new Matrix();
        TRANSFORM.translate(camera.x, camera.y, camera.z);
        FPS = "FPS: NaN";
    }
}