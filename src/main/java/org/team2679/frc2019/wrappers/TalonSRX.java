package org.team2679.frc2019.wrappers;

import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * This class is a thin wrapper around the CANTalon that reduces CAN bus / CPU overhead by skipping duplicate set
 * commands. (By default the Talon flushes the Tx buffer on every set call).
 * Credit to FRC team 254.
 */
public class TalonSRX{

    private double mLastSet = Double.NaN;
    private ControlMode mLastControlMode = null;

    public TalonSRX(int deviceNumber) {
        //super(deviceNumber);
    }

    public double getLastSet() {
        return mLastSet;
    }


    public void set(ControlMode mode, double value) {
        if (value != mLastSet || mode != mLastControlMode) {
            mLastSet = value;
            mLastControlMode = mode;
            //super.set(mode, value);
        }
    }

    public boolean isAlive() {
        return true;
    }
}
