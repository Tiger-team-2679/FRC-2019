package org.team2679.frc2019.subsystems;

import org.team2679.TigerEye.lib.thread.Notifier;
import org.team2679.TigerEye.lib.thread.NotifierListener;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.team2679.frc2019.IO;

public enum Drive implements Subsystem {

    INSTANCE;

    private class DRIVE_SETTINGS{
        static final int LEFT_MOTOR_FRONT_PORT = 2;
        static final int RIGHT_MOTOR_FRONT_PORT = 4;
        static final int LEFT_MOTOR_REAR_PORT = 1;
        static final int RIGHT_MOTOR_REAR_PORT = 3;
    }

    public enum DRIVE_STATE {
        DISABLED, DRIVER_CONTROL
    }

    private WPI_TalonSRX _LEFT_MOTOR_FRONT;
    private WPI_TalonSRX _RIGHT_MOTOR_FRONT;
    private WPI_TalonSRX _LEFT_MOTOR_REAR;
    private WPI_TalonSRX _RIGHT_MOTOR_REAR;

    private SpeedControllerGroup _LEFT_MOTOR_GROUP;
    private SpeedControllerGroup _RIGHT_MOTOR_GROUP;

    private DifferentialDrive _DIFFERENTIAL_DRIVE;

    Drive(){
        this._LEFT_MOTOR_FRONT = new WPI_TalonSRX(DRIVE_SETTINGS.LEFT_MOTOR_FRONT_PORT);
        this._RIGHT_MOTOR_FRONT = new WPI_TalonSRX(DRIVE_SETTINGS.RIGHT_MOTOR_FRONT_PORT);
        this._LEFT_MOTOR_REAR = new WPI_TalonSRX(DRIVE_SETTINGS.LEFT_MOTOR_REAR_PORT);
        this._RIGHT_MOTOR_REAR = new WPI_TalonSRX(DRIVE_SETTINGS.RIGHT_MOTOR_REAR_PORT);

        _LEFT_MOTOR_GROUP = new SpeedControllerGroup(_LEFT_MOTOR_REAR, _LEFT_MOTOR_FRONT);
        _RIGHT_MOTOR_GROUP = new SpeedControllerGroup(_RIGHT_MOTOR_REAR, _RIGHT_MOTOR_FRONT);

        _DIFFERENTIAL_DRIVE =  new DifferentialDrive(_LEFT_MOTOR_GROUP, _RIGHT_MOTOR_GROUP);

        _logger.info("Drive -> Initiated");
        this.setDisabled();
    }

    private NotifierListener _loop = this::onLoop;

    private DRIVE_STATE _currentState;

    public synchronized void onLoop(){
        synchronized (this) {
            switch (this._currentState) {
                case DISABLED:
                    break;
                case DRIVER_CONTROL:
                    _DIFFERENTIAL_DRIVE.arcadeDrive(-IO.JOYSTICK.getY(), IO.JOYSTICK.getX());
                    break;
                default:
                    _logger.error("Drive -> unexpected state");
                    break;
            }
        }
    }

    public synchronized void setDisabled(){
        this._currentState = DRIVE_STATE.DISABLED;
        _logger.debug("Drive -> Switched to state " + DRIVE_STATE.DISABLED);
    }

    public synchronized void setDriverControl(){
        this._currentState = DRIVE_STATE.DRIVER_CONTROL;
       _logger.debug("Drive -> Switched to state " + DRIVE_STATE.DRIVER_CONTROL);
    }

    public synchronized DRIVE_STATE getCurrentState(){
        return this._currentState;
    }

    public synchronized void registerSubsystem(Notifier notifier) {
        notifier.registerListener(this._loop);
        _logger.info("Drive -> Registered in a notifier");
    }
}
