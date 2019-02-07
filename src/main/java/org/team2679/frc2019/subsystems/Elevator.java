package org.team2679.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import org.team2679.TigerEye.core.thread.Notifier;
import org.team2679.TigerEye.core.thread.NotifierListener;
import org.team2679.TigerEye.lib.log.Logger;
import org.team2679.frc2019.wrappers.TalonSRX;

public enum Elevator {

    INSTANCE;

    private Logger _logger;

    private class ELEVATOR_SETTINGS {
        static final int LEFT_MOTOR_PORT = 3;
        static final int RIGHT_MOTOR_PORT = 4;
    }

    public enum ELEVATOR_STATE {
        DISABLED, DRIVER_HANDLED, COAST, REACH_LEVEL
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

    private TalonSRX _LEFT_MOTOR;
    private TalonSRX _RIGHT_MOTOR;

    Elevator(){
        this._LEFT_MOTOR = new TalonSRX(ELEVATOR_SETTINGS.LEFT_MOTOR_PORT);
        this._RIGHT_MOTOR = new TalonSRX(ELEVATOR_SETTINGS.RIGHT_MOTOR_PORT);
        this._logger = new Logger("Elevator_Subsystem");
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
                case DRIVER_HANDLED:
                    break;
            }
        }
    }

    public synchronized void setReachTargetLevel(ELEVATOR_LEVEL targetLevel){
        this._targetLevel = targetLevel;
        this._currentState = ELEVATOR_STATE.REACH_LEVEL;
        _logger.debug("Elevator -> Switch to state: " + ELEVATOR_STATE.REACH_LEVEL + " trying to reach " + targetLevel);
    }

    public synchronized void setCoast(){
        this._currentState = ELEVATOR_STATE.COAST;
        _logger.debug("Elevator -> Switch to state: " + ELEVATOR_STATE.COAST);
    }

    public synchronized void setDisabled(){
        this._currentState = ELEVATOR_STATE.DISABLED;
        _logger.debug("Elevator -> Switch to state: " + ELEVATOR_STATE.DISABLED);
    }

    public synchronized void setDriverHandled(){
        this._currentState = ELEVATOR_STATE.DRIVER_HANDLED;
        _logger.debug("Elevator -> Switch to state: " + ELEVATOR_STATE.DRIVER_HANDLED);
    }

    public synchronized ELEVATOR_STATE getCurrentState(){
        return this._currentState;
    }

    public synchronized void registerSubsystem(Notifier notifier) {
        notifier.registerListener(this._loop);
        _logger.info("Elevator -> Registered in a notifier");
    }
}
