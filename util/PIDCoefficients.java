package frc.robot.util;

public class PIDCoefficients {

    public double kP;
    public double kI;
    public double kD;
    public double kF;

    public PIDCoefficients(double _kP, double _kI, double _kD, double _kF) {

        kP = _kP;
        kI = _kI;
        kD = _kD;
        kF = _kF;
    }

    public double getP() {
        return kP;
    }

    public void setP(double kP) {
        this.kP = kP;
    }

    public double getI() {
        return kI;
    }

    public void setI(double kI) {
        this.kI = kI;
    }

    public double getD() {
        return kD;
    }

    public void setD(double kD) {
        this.kD = kD;
    }

    public double getF() {
        return kF;
    }

    public void setF(double kF) {
        this.kF = kF;
    }
}