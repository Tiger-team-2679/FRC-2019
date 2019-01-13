package org.team2679.frc2019;

import org.team2679.TigerEye.core.Setup;
import org.team2679.TigerEye.core.Tiger;
import org.team2679.frc2019.subsystems.Grabber;

public class Main extends Setup {

    @Override
    public void preinit() {
        // TODO write code to be executed before HAL is told the robot has started
    }

    @Override
    public void init() {
        Grabber.INSTANCE.registerSubsystem(Tiger.get_main_notifier_50ms());
    }
}
