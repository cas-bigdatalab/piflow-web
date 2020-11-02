package cn.cnic.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cn.cnic.base.TextureEnumSerializer;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum ArrowDirection {
    UP_DIRECTION(3.5, 0, 3.5, 5, 0, 7, 7, 7),
    DOWN_DIRECTION(3.5, 7, 3.5, 2, 0, 0, 7, 0),
    RIGHT_DIRECTION(7, 3.5, 2, 3.5, 0, 0, 0, 7),
    LEFT_DIRECTION(0, 3.5, 5, 3.5, 7, 0, 7, 7);
    private final double upX;
    private final double upY;
    private final double downX;
    private final double downY;
    private final double rightX;
    private final double rightY;
    private final double lfetX;
    private final double lfetY;

    private ArrowDirection(double upX, double upY, double downX, double downY, double rightX, double rightY, double lfetX, double lfetY) {
        this.upX = upX;
        this.upY = upY;
        this.downX = downX;
        this.downY = downY;
        this.rightX = rightX;
        this.rightY = rightY;
        this.lfetX = lfetX;
        this.lfetY = lfetY;
    }

    public double getUpX() {
        return upX;
    }

    public double getUpY() {
        return upY;
    }

    public double getDownX() {
        return downX;
    }

    public double getDownY() {
        return downY;
    }

    public double getRightX() {
        return rightX;
    }

    public double getRightY() {
        return rightY;
    }

    public double getLfetX() {
        return lfetX;
    }

    public double getLfetY() {
        return lfetY;
    }
}
