package org.team2679.frc2019.subsystems;

import org.team2679.TigerEye.core.thread.Notifier;
import org.team2679.TigerEye.core.thread.NotifierListener;
import org.team2679.TigerEye.lib.log.Logger;
import org.team2679.frc2019.wrappers.TalonSRX;

public enum Drive {

    INSTANCE;

    private Logger _logger;

    private class DRIVE_SETTINGS{
        static final int LEFT_MOTOR_FRONT_PORT = 5;
        static final int RIGHT_MOTOR_FRONT_PORT = 6;
        static final int LEFT_MOTOR_REAR_PORT = 7;
        static final int RIGHT_MOTOR_REAR_PORT = 8;
    }

    public enum DRIVE_STATE {
        DISABLED, DRIVER_HANDLED
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
        this._logger = new Logger("Drive_Subsystem");
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
                case DRIVER_HANDLED:
                    break;
            }
        }
    }

    public synchronized void setDisabled(){
        this._currentState = DRIVE_STATE.DISABLED;
        _logger.debug("Drive -> Switched to state " + DRIVE_STATE.DISABLED);
    }

    public synchronized void setDriverHandled(){
        this._currentState = DRIVE_STATE.DRIVER_HANDLED;
        _logger.debug("Drive -> Switched to state " + DRIVE_STATE.DRIVER_HANDLED);
    }

    public synchronized DRIVE_STATE getCurrentState(){
        return this._currentState;
    }

    public synchronized void registerSubsystem(Notifier notifier) {
        notifier.registerListener(this._loop);
        _logger.info("Drive -> Registered in a notifier");
    }
}
