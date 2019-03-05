package org.team2679.frc2019;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;

public class IO {

    private enum PORTS{
        XBOX(0),
        JOYSTICK(1);

        private final int _port;
        PORTS(int port){
            this._port = port;
        }
        int getPort(){
            return this._port;
        }
    }

    public static final XboxController XBOX = new XboxController(PORTS.XBOX.getPort());
    public static final Joystick JOYSTICK = new Joystick(PORTS.JOYSTICK.getPort());
}
