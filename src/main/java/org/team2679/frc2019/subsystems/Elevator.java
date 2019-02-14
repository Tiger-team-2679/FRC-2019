package org.team2679.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import org.team2679.TigerEye.lib.thread.Notifier;
import org.team2679.TigerEye.lib.thread.NotifierListener;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import org.team2679.frc2019.IO;

public enum Elevator implements Subsystem {

    INSTANCE;

    private class ELEVATOR_SETTINGS {
        static final int LEFT_MOTOR_PORT = 5;
        static final int RIGHT_MOTOR_PORT = 6;
    }

    public enum ELEVATOR_STATE {
        DISABLED, DRIVER_CONTROL, COAST, REACH_LEVEL
    }

    public enum ELEVATOR_LEVEL {
        FLOOR(0),
        CARGO_LEVEL_1(0), REACH__LEVEL_2(1), REACH__LEVEL_3(2),
        HATCH_PANEL_LEVEL_1(0), HATCH_PANEL_LEVEL_2(1), HATCH_PANEL_LEVEL_3(3);

        private double height;

        ELEVATOR_LEVEL(double height){
            this.height = height;
        }

        public double getHeight() {
            return height;
        }
    }

    private WPI_TalonSRX _LEFT_MOTOR;
    private WPI_TalonSRX _RIGHT_MOTOR;

    Elevator(){
        this._LEFT_MOTOR = new WPI_TalonSRX(ELEVATOR_SETTINGS.LEFT_MOTOR_PORT);
        this._RIGHT_MOTOR = new WPI_TalonSRX(ELEVATOR_SETTINGS.RIGHT_MOTOR_PORT);
        _logger.info("Elevator -> Initiated");
        this.setDisabled();
    }

    private NotifierListener _loop = this::onLoop;

    private ELEVATOR_STATE _currentState;
    private ELEVATOR_LEVEL _targetLevel;

    public synchronized void onLoop(){
        synchronized (this) {
            switch (this._currentState) {
                case DISABLED:
                    this._LEFT_MOTOR.set(ControlMode.Disabled, 0);
                    this._RIGHT_MOTOR.set(ControlMode.Disabled, 0);
                    break;
                case REACH_LEVEL:
                    this.setCoast();
                    break;
                case COAST:
                    break;
                case DRIVER_CONTROL:
                    this._LEFT_MOTOR.set(ControlMode.Disabled, -IO.XBOX.getRawAxis(5));
                    this._RIGHT_MOTOR.set(ControlMode.Disabled, -IO.XBOX.getRawAxis(5));
                    break;
                default:
                    _logger.error("Elevator -> unexpected state");
                    break;
            }
        }
    }

    public synchronized void setReachTargetLevel(ELEVATOR_LEVEL targetLevel){
        this._targetLevel = targetLevel;
        this._currentState = ELEVATOR_STATE.REACH_LEVEL;
        _logger.debug("Elevator -> Switched to state " + ELEVATOR_STATE.REACH_LEVEL + " trying to reach " + targetLevel);
    }

    public synchronized void setCoast(){
        this._currentState = ELEVATOR_STATE.COAST;
        _logger.debug("Elevator -> Switched to state " + ELEVATOR_STATE.COAST);
    }

    public synchronized void setDisabled(){
        this._currentState = ELEVATOR_STATE.DISABLED;
        _logger.debug("Elevator -> Switched to state " + ELEVATOR_STATE.DISABLED);
    }

    public synchronized void setDriverControl(){
        this._currentState = ELEVATOR_STATE.DRIVER_CONTROL;
        _logger.debug("Elevator -> Switched to state " + ELEVATOR_STATE.DRIVER_CONTROL);
    }

    public synchronized ELEVATOR_STATE getCurrentState(){
        return this._currentState;
    }

    public synchronized void registerSubsystem(Notifier notifier) {
        notifier.registerListener(this._loop);
        _logger.info("Elevator -> Registered in a notifier");
    }
}
