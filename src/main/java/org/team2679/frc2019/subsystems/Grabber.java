package org.team2679.frc2019.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.team2679.TigerEye.core.thread.Notifier;
import org.team2679.TigerEye.core.thread.NotifierListener;
import org.team2679.frc2019.IO;

public enum Grabber implements Subsystem {

    INSTANCE;

    private enum PORTS{
        LEFT_MOTOR(1), RIGHT_MOTOR(2);

        private final int _port;
        PORTS(int port){
            this._port = port;
        }
        int getPort(){
            return this._port;
        }
    }

    NotifierListener _loop = new NotifierListener() {
        @Override
        public void onUpdate() {
            onLoop();
        }
    };

    private TalonSRX LEFT_MOTOR = new TalonSRX(PORTS.LEFT_MOTOR.getPort());
    private TalonSRX RIGHT_MOTOR = new TalonSRX(PORTS.RIGHT_MOTOR.getPort());

    public void onLoop(){
        LEFT_MOTOR.set(ControlMode.PercentOutput, IO.XBOX.getRawAxis(1));
        RIGHT_MOTOR.set(ControlMode.PercentOutput, IO.XBOX.getRawAxis(5));
    }

    @Override
    public void registerSubsystem(Notifier notifier) {
        notifier.registerListener(this._loop);
    }
}
