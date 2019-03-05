package org.team2679.frc2019;

import edu.wpi.cscore.CameraServerJNI;
import edu.wpi.first.cameraserver.CameraServerShared;
import edu.wpi.first.cameraserver.CameraServerSharedStore;
import edu.wpi.first.hal.FRCNetComm;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import org.team2679.TigerEye.core.Tiger;

public class NetworkTablesManager {

    private static final long MAIN_THREAD_ID = Thread.currentThread().getId();
    private static DriverStation m_ds;
    private static boolean initiated;

    private static void setupCameraServerShared() {
        CameraServerShared shared = new CameraServerShared() {

            @Override
            public void reportVideoServer(int id) {
                HAL.report(FRCNetComm.tResourceType.kResourceType_PCVideoServer, id);
            }

            @Override
            public void reportUsbCamera(int id) {
                HAL.report(FRCNetComm.tResourceType.kResourceType_UsbCamera, id);
            }

            @Override
            public void reportDriverStationError(String error) {
                DriverStation.reportError(error, true);
            }

            @Override
            public void reportAxisCamera(int id) {
                HAL.report(FRCNetComm.tResourceType.kResourceType_AxisCamera, id);
            }

            @Override
            public Long getRobotMainThreadId() {
                return MAIN_THREAD_ID;
            }
        };

        CameraServerSharedStore.setCameraServerShared(shared);
    }

    public static void init(){
        if(!initiated) {
            initiated = true;
            NetworkTableInstance inst = NetworkTableInstance.getDefault();
            setupCameraServerShared();
            inst.setNetworkIdentity("Robot");
            inst.startServer("/home/lvuser/networktables.ini");
            m_ds = DriverStation.getInstance();
            inst.getTable("LiveWindow").getSubTable(".status").getEntry("LW Enabled").setBoolean(true);
            LiveWindow.setEnabled(true);
            Shuffleboard.disableActuatorWidgets();
            CameraServerJNI.enumerateSinks();
            Tiger.log().info("Network tables and camera server are up");
        }
    }

    public static DriverStation DriverStation() {
        return m_ds;
    }
}
