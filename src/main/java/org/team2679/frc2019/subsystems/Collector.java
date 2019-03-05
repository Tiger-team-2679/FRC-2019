package org.team2679.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import org.team2679.TigerEye.lib.thread.Notifier;
import org.team2679.TigerEye.lib.thread.NotifierListener;
import org.team2679.TigerEye.lib.util.Timer;
import org.team2679.frc2019.wrappers.TalonSRX;

/**
 * Singleton class for the collector subsystem
 * The logic is implemented in a state based way
 *
 */
public enum Collector implements Subsystem {

    INSTANCE;

    private class COLLECTOR_SETTINGS{
        static final int LEFT_MOTOR_PORT = 1;
        static final int RIGHT_MOTOR_PORT = 2;
        static final int AUTO_COLLECT_DURATION = 10000;
        static final int AUTO_RELEASE_DURATION = 10000;
    }

    public enum COLLECTOR_STATE {
        DISABLED, RELEASE, COLLECT, DRIVER_CONTROL
    }

    private TalonSRX _LEFT_MOTOR;
    private TalonSRX _RIGHT_MOTOR;

    Collector(){
        this._LEFT_MOTOR = new TalonSRX(COLLECTOR_SETTINGS.LEFT_MOTOR_PORT);
        this._RIGHT_MOTOR = new TalonSRX(COLLECTOR_SETTINGS.RIGHT_MOTOR_PORT);
        _logger.info("Collector -> Initiated");
        this.setDisabled();
    }

    private NotifierListener _loop = this::onLoop;

    private COLLECTOR_STATE _currentState;

    public synchronized void onLoop(){
        synchronized (this) {
            switch (this._currentState) {
                case DISABLED:
                    this._LEFT_MOTOR.set(ControlMode.Disabled, 0);
                    this._RIGHT_MOTOR.set(ControlMode.Disabled, 0);
                    break;
                case RELEASE:
                    if(handlePowerForTime(COLLECTOR_SETTINGS.AUTO_RELEASE_DURATION, 1)){
                        this.setDisabled();
                    }
                    break;
                case COLLECT:
                    if(handlePowerForTime(COLLECTOR_SETTINGS.AUTO_COLLECT_DURATION, -1)){
                        this.setDisabled();
                    }
                    break;
                case DRIVER_CONTROL:
                    break;
                default:
                    _logger.error("Collector -> unexpected state");
                    break;
            }
        }
    }


    private long temp_time_holder = -1;

    /**
     * run the system for a given time (without stopping the thread or creating another)
     * @param time the time to run in millis
     * @param power the power to apply
     * @return if the function has finished or not
     */
    private synchronized boolean handlePowerForTime(double time, double power) {
        if(this.temp_time_holder == -1) {
            this.temp_time_holder = Timer.getCurrentTimeMillis();
        }
        else {
            if (Timer.getCurrentTimeMillis() - this.temp_time_holder < time){
                this._LEFT_MOTOR.set(ControlMode.PercentOutput, power);
                this._RIGHT_MOTOR.set(ControlMode.PercentOutput, power);
            }
            else {
                this._LEFT_MOTOR.set(ControlMode.PercentOutput, 0);
                this._RIGHT_MOTOR.set(ControlMode.PercentOutput, 0);
                this.resetPowerForTime();
                return true;
            }
        }
        return false;
    }

    /**
     * reset the handlePowerForTime, used when we stop the state in the middle
     */
    private synchronized void resetPowerForTime(){
        this.temp_time_holder = -1;
    }

    public synchronized void setShoot(){
        this.resetPowerForTime();
        this._currentState = COLLECTOR_STATE.RELEASE;
        _logger.debug("Collector -> Switched to state " + COLLECTOR_STATE.RELEASE);
    }

    public synchronized void setCollect(){
        this.resetPowerForTime();
        this._currentState = COLLECTOR_STATE.COLLECT;
        _logger.debug("Collector -> Switched to state " + COLLECTOR_STATE.COLLECT);
    }

    public synchronized void setDisabled(){
        this._currentState = COLLECTOR_STATE.DISABLED;
        _logger.debug("Collector -> Switched to state " + COLLECTOR_STATE.DISABLED);
    }

    public synchronized void setDriverHandled(){
        this._currentState = COLLECTOR_STATE.DRIVER_CONTROL;
        _logger.debug("Collector -> Switched to state " + COLLECTOR_STATE.DRIVER_CONTROL);
    }

    public synchronized COLLECTOR_STATE getCurrentState(){
        return this._currentState;
    }

    public synchronized void registerSubsystem(Notifier notifier) {
        notifier.registerListener(this._loop);
        _logger.info("Collector -> Registered in a notifier");
    }
}