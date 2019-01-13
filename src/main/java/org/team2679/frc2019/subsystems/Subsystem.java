package org.team2679.frc2019.subsystems;

import org.team2679.TigerEye.core.thread.Notifier;
import org.team2679.TigerEye.core.thread.NotifierListener;

public interface Subsystem {
    public void registerSubsystem(Notifier notifier);
}
