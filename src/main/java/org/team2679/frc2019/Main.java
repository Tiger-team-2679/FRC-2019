package org.team2679.frc2019;

import org.team2679.TigerEye.core.Setup;
import org.team2679.TigerEye.core.Tiger;
import org.team2679.frc2019.subsystems.Collector;
import org.team2679.frc2019.subsystems.Drive;
import org.team2679.frc2019.subsystems.Elevator;
import org.team2679.frc2019.subsystems.SuperStructure;

public class Main extends Setup {

    @Override
    public void preinit() {
        // TODO write code to be executed before HAL is told the robot has started
    }

    @Override
    public void init() {
        // Register all of the subsystems
        Drive.INSTANCE.registerSubsystem(Tiger.get_main_notifier_50ms());
        Collector.INSTANCE.registerSubsystem(Tiger.get_main_notifier_100ms());
        Elevator.INSTANCE.registerSubsystem(Tiger.get_main_notifier_20ms());
        SuperStructure.INSTANCE.registerSubsystem(Tiger.get_main_notifier_50ms());

        // run the super structure to control all of the subsystems
        SuperStructure.INSTANCE.setPutCargoAtLevel(Elevator.ELEVATOR_LEVEL.HATCH_PANEL_LEVEL_3);
    }
}
