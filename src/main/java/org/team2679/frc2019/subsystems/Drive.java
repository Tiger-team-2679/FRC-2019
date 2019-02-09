package org.team2679.frc2019.subsystems;

import org.team2679.TigerEye.lib.thread.Notifier;
import org.team2679.TigerEye.lib.thread.NotifierListener;
import org.team2679.frc2019.wrappers.TalonSRX;

public enum Drive implements Subsystem {

    INSTANCE;

    private class DRIVE_SETTINGS{
        static final int LEFT_MOTOR_FRONT_PORT = 5;
        static final int RIGHT_MOTOR_FRONT_PORT = 6;
        static final int LEFT_MOTOR_REAR_PORT = 7;
        static final int RIGHT_MOTOR_REAR_PORT = 8;
    }

    public enum DRIVE_STATE {
        DISABLED, DRIVER_CONTROL
    }

    private TalonSRX _LEFT_MOTOR_FRONT;
    private TalonSRX _RIGHT_MOTOR_FRONT;
    private TalonSRX _LEFT_MOTOR_REAR;
    private TalonSRX _RIGHT_MOTOR_REAR;

    Drive(){
        this._LEFT_MOTOR_FRONT = new TalonSRX(DRIVE_SETTINGS.LEFT_MOTOR_FRONT_PORT);
        this._RIGHT_MOTOR_FRONT = new TalonSRX(DRIVE_SETTINGS.RIGHT_MOTOR_FRONT_PORT);
        this._LEFT_MOTOR_REAR = new TalonSRX(DRIVE_SETTINGS.LEFT_MOTOR_REAR_PORT);
        this._RIGHT_MOTOR_REAR = new TalonSRX(DRIVE_SETTINGS.RIGHT_MOTOR_REAR_PORT);
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

    public synchronized void setDriverHandled(){
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
