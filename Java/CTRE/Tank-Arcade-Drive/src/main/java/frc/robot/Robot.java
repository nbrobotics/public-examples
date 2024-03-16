// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// This example is based on https://github.com/CrossTheRoadElec/Phoenix5-Examples/tree/master/Java%20General/DifferentialDrive

// CTRE
//    If you use TalonSRX or VictorSPX you will need version 5, if do not you should be good with version 6. When in doubt you could load both.
//    Version 6 - https://maven.ctr-electronics.com/release/com/ctre/phoenix6/latest/Phoenix6-frc2024-latest.json
//    Version 5 - https://maven.ctr-electronics.com/release/com/ctre/phoenix/Phoenix5-frc2024-latest.json
// NavX Library - https://dev.studica.com/releases/2024/NavX.json
// Rev Robotics - https://software-metadata.revrobotics.com/REVLib-2024.json
// Venom - https://www.playingwithfusion.com/frc/playingwithfusion2024.json

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
// import com.ctre.phoenix.motorcontrol.can.WPI_TalonSPX;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the DifferentialDrive class. Runs the motors with
 * arcade steering.
 */
public class Robot extends TimedRobot {

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private DifferentialDrive m_robotDrive;
  private Joystick js_Driver;
  private static final int leftLeadDeviceID = 11;
  private static final int leftFollowDeviceID = 12;
  private static final int rightLeadDeviceID = 7;
  private static final int rightFollowDeviceID = 9;
  WPI_VictorSPX m_leftFollowMotor;
  WPI_VictorSPX m_leftLeadMotor;
  WPI_VictorSPX m_rightFollowMotor;
  WPI_VictorSPX m_rightLeadMotor;
  // WPI_TalonSRX m_leftFollowMotor;
  // WPI_TalonSRX m_leftLeadMotor;
  // WPI_TalonSRX m_rightFollowMotor;
  // WPI_TalonSRX m_rightLeadMotor;

  Timer timer = new Timer();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // If you have Talons instead of Victor make sure to uncomment as needed.
    m_leftFollowMotor = new WPI_VictorSPX(leftFollowDeviceID);
    m_leftLeadMotor = new WPI_VictorSPX(leftLeadDeviceID);
    m_rightFollowMotor = new WPI_VictorSPX(rightFollowDeviceID);
    m_rightLeadMotor = new WPI_VictorSPX(rightLeadDeviceID);
    // m_leftFollowMotor = new WPI_TalonSRX(leftFollowDeviceID);
    // m_leftLeadMotor = new WPI_TalonSRX(leftLeadDeviceID);
    // m_rightFollowMotor = new WPI_TalonSRX(rightFollowDeviceID);
    // m_rightLeadMotor = new WPI_TalonSRX(rightLeadDeviceID);

    /* factory default values */
    // m_rightLeadMotor.configFactoryDefault();
    //   m_rightFollowMotor.configFactoryDefault();
    //  m_leftLeadMotor.configFactoryDefault();
    //  m_leftFollowMotor.configFactoryDefault();

    /* set up followers */
    m_rightFollowMotor.follow(m_rightLeadMotor);
    m_leftFollowMotor.follow(m_leftLeadMotor);

    /* [3] flip values so robot moves forward when stick-forward/LEDs-green */
    m_rightLeadMotor.setInverted(true); // !< Update this
    m_leftLeadMotor.setInverted(false); // !< Update this

    /*
     * set the invert of the followers to match their respective master controllers
     */
    m_rightFollowMotor.setInverted(InvertType.FollowMaster);
    m_leftFollowMotor.setInverted(InvertType.FollowMaster);

    m_robotDrive = new DifferentialDrive(m_leftLeadMotor, m_rightLeadMotor);

    js_Driver = new Joystick(0);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here

        // Drive forward for 4 seconds
        if (timer.get() < 4) {
          m_robotDrive.arcadeDrive(0.5, 0);
        } else {
          m_leftLeadMotor.set(0);
          m_rightLeadMotor.set(0);
        }

        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    /* get gamepad stick values */
    double forw = -1 * js_Driver.getRawAxis(1);/* positive is forward */
    double turn = -1 * js_Driver.getRawAxis(2);/* positive is right */

    /* deadband gamepad 10% */
    if (Math.abs(forw) < 0.10) {
      forw = 0;
    }
    if (Math.abs(turn) < 0.10) {
      turn = 0;
    }

    /* drive robot */
    m_robotDrive.arcadeDrive(forw, turn);
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    //region Test Wheel Direction
    if (js_Driver.getRawButton(1)) {
      // X Button
      m_leftLeadMotor.set(0.2);
    } else {
      m_leftLeadMotor.set(0);
    }
    if (js_Driver.getRawButton(2)) {
      // A Button
      m_leftFollowMotor.set(0.2);
    } else {
      m_leftFollowMotor.set(0);
    }
    if (js_Driver.getRawButton(3)) {
      // B Button
      m_rightLeadMotor.set(0.2);
    } else {
      m_rightLeadMotor.set(0);
    }
    if (js_Driver.getRawButton(4)) {
      // Y Button
      m_rightFollowMotor.set(0.2);
    } else {
      m_rightFollowMotor.set(0);
    }
    //endregion Test Wheel Direction
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
