package org.team2679.frc2019.subsystems;

import org.team2679.TigerEye.core.thread.Notifier;
import org.team2679.TigerEye.core.thread.NotifierListener;
import org.team2679.TigerEye.lib.log.Logger;

public enum SuperStructure {

    INSTANCE;

    private Logger _logger;

    public enum SUPERSTRUCTURE_STATE {
        DISABLED, DRIVER_HANDLED, PUT_CARGO_AT_LEVEL
    }

    SuperStructure(){
        this._logger = new Logger("SuperStructure_Subsystem");
        _logger.info("SuperStructure -> Initiated");
        this.setDisabled();
    }

    private NotifierListener _loop = this::onLoop;

    private SUPERSTRUCTURE_STATE _currentState;

    public synchronized void onLoop(){
        synchronized (this) {
            switch (this._currentState) {
                case DISABLED:
                    if(Elevator.INSTANCE.getCurrentState() != Elevator.ELEVATOR_STATE.DISABLED) {
                        Elevator.INSTANCE.setDisabled();
                    }
                    if(Collector.INSTANCE.getCurrentState() != Collector.COLLECTOR_STATE.DISABLED) {
                        Collector.INSTANCE.setDisabled();
                    }
                    break;
                case DRIVER_HANDLED:
                    if(Elevator.INSTANCE.getCurrentState() != Elevator.ELEVATOR_STATE.DRIVER_HANDLED) {
                        Elevator.INSTANCE.setDriverHandled();
                    }
                    if(Collector.INSTANCE.getCurrentState() != Collector.COLLECTOR_STATE.DRIVER_HANDLED) {
                        Collector.INSTANCE.setDriverHandled();
                    }
                case PUT_CARGO_AT_LEVEL:
                    if(handlePutCargoAtLevel()){
                        this.setDriverHandled();
                    }
                    break;
            }
        }
    }

    private boolean called_elevator_reached_level;
    private boolean called_collector_shoot;
    private Elevator.ELEVATOR_LEVEL _targetLevel;

    private synchronized boolean handlePutCargoAtLevel(){
        if (!called_elevator_reached_level){
            Elevator.INSTANCE.setReachTargetLevel(this._targetLevel);
            called_elevator_reached_level = true;
        }
        if(Elevator.INSTANCE.getCurrentState() == Elevator.ELEVATOR_STATE.COAST){
            if(!called_collector_shoot) {
                Collector.INSTANCE.setShoot();
                called_collector_shoot = true;
            }
            if(Collector.INSTANCE.getCurrentState() == Collector.COLLECTOR_STATE.DISABLED){
                return true;
            }
        }
        return false;
    }

    private synchronized void resetHandlePutCargoAtLevel(){
        called_collector_shoot = false;
        called_elevator_reached_level = false;
    }

    public synchronized void setPutCargoAtLevel(Elevator.ELEVATOR_LEVEL targetLevel){
        this.resetHandlePutCargoAtLevel();
        this._targetLevel = targetLevel;
        this._currentState = SUPERSTRUCTURE_STATE.PUT_CARGO_AT_LEVEL;
        _logger.debug("SuperStructure -> Switch to state: " + SUPERSTRUCTURE_STATE.PUT_CARGO_AT_LEVEL);
    }

    public synchronized void setDisabled(){
        this._currentState = SUPERSTRUCTURE_STATE.DISABLED;
        _logger.debug("SuperStructure -> Switch to state: " + SUPERSTRUCTURE_STATE.DISABLED);
    }

    public synchronized void setDriverHandled(){
        this._currentState = SUPERSTRUCTURE_STATE.DRIVER_HANDLED;
        _logger.debug("SuperStructure -> Switch to state: " + SUPERSTRUCTURE_STATE.DRIVER_HANDLED);
    }

    public synchronized SUPERSTRUCTURE_STATE getCurrentState(){
        return this._currentState;
    }

    public synchronized void registerSubsystem(Notifier notifier) {
        notifier.registerListener(this._loop);
        _logger.info("SuperStructure -> Registered in a notifier");
    }
}
