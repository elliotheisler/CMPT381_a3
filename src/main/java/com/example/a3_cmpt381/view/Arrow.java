package com.example.a3_cmpt381.view;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.shape.*;

import static java.lang.Math.*;

public class Arrow {
    private static final double HEAD_LENGTH = 20;
    private static final double HEAD_WIDTH = 12;
    private static final double WEIRD_OFFSET = 10;
    private Line line = new Line();
    private Polygon head = createHead();
    private double angle;

    public Arrow() {};

    public Arrow(Point2D start, Point2D end) {
        setStartPos(start);
        setEndPos(end);
    }

    private Polygon createHead() {
        Polygon head = new Polygon(
                -HEAD_LENGTH, -HEAD_WIDTH / 2,
                0, 0,
                -HEAD_LENGTH, +HEAD_WIDTH / 2
        );
//        head.setRotationAxis();
//        head.setLayoutX(0);
//        head.setLayoutY(0);
        return head;
    }

    public void setStartPos(Point2D p) {
        line.setStartX(p.getX());
        line.setStartY(p.getY());
        setAngle();
    }

    public void setEndPos(Point2D p) {
        line.setEndX(p.getX());
        line.setEndY(p.getY());
        // head renders 10px to the left of line end without this. it just started
        // happening and i dont know why.
        head.setTranslateX(line.getEndX() + WEIRD_OFFSET);
        head.setTranslateY(line.getEndY());
        setAngle();
    }

    private void setAngle() {
        angle = toDegrees(atan2(
                line.getEndY() - line.getStartY(),
                line.getEndX() - line.getStartX()));
//        angle = angle < 0 ? 360 + angle : angle;
        head.setRotate(angle);
    }

    public Node[] getShapes() {
        Node[] shapes = { line, head };
        return shapes;
    }

    public void setVisible(boolean b) {
        line.setVisible(b);
        head.setVisible(b);
    }

    public void setStartX(double x) {
        setStartPos(new Point2D(
                x,
                line.getStartY()
        ));
    }

    public void setStartY(double y) {
        setStartPos(new Point2D(
                line.getStartX(),
                y
        ));
    }

    public void setEndX(double x) {
        setEndPos(new Point2D(
                x,
                line.getEndY()
        ));
    }

    public void setEndY(double y) {
        setEndPos(new Point2D(
                line.getEndX(),
                y
        ));
    }
}
