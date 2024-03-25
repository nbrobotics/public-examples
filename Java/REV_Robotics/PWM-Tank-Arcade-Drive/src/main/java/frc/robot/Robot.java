// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {

  private final PWMSparkMax m_leftBack = new PWMSparkMax(1);
  private final PWMSparkMax m_leftFront = new PWMSparkMax(3);
  private final PWMSparkMax m_rightBack = new PWMSparkMax(4);
  private final PWMSparkMax m_rightFront = new PWMSparkMax(2);

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(
    m_leftFront::set,
    m_rightFront::set
  );
  private final Joystick js_Driver = new Joystick(0);

  public Robot() {
    SendableRegistry.addChild(m_robotDrive, m_leftFront);
    SendableRegistry.addChild(m_robotDrive, m_rightFront);
  }

  @Override
  public void robotInit() {
    m_leftBack.addFollower(m_leftFront);
    m_rightBack.addFollower(m_rightFront);

    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightFront.setInverted(true);
  }

  @Override
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.
    m_robotDrive.arcadeDrive(-js_Driver.getY(), -js_Driver.getX());
  }
}
