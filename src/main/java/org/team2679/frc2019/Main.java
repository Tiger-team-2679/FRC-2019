package org.team2679.frc2019;

import edu.wpi.first.cameraserver.CameraServer;
import org.team2679.TigerEye.core.Setup;
import org.team2679.TigerEye.core.Tiger;
import org.team2679.frc2019.subsystems.Collector;
import org.team2679.frc2019.subsystems.Drive;
import org.team2679.frc2019.subsystems.Elevator;
import org.team2679.frc2019.subsystems.SuperStructure;

public class Main extends Setup {

    @Override
    public void preinit() {
        NetworkTablesManager.init();
        CameraServer.getInstance().startAutomaticCapture();
        Drive.INSTANCE.registerSubsystem(Tiger.get_main_notifier_50ms());
        Collector.INSTANCE.registerSubsystem(Tiger.get_main_notifier_100ms());
        Elevator.INSTANCE.registerSubsystem(Tiger.get_main_notifier_20ms());
        //SuperStructure.INSTANCE.registerSubsystem(Tiger.get_main_notifier_50ms());
    }

    @Override
    public void init() {
        // run the super structure to control all of the subsystems
        //SuperStructure.INSTANCE.setPutCargoAtLevel(Elevator.ELEVATOR_LEVEL.HATCH_PANEL_LEVEL_3);
        Drive.INSTANCE.setDriverControl();
        Elevator.INSTANCE.setDriverControl();
        Collector.INSTANCE.setDriverControl();
    }
}
