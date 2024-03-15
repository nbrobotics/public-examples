// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// CTRE
//    If you use TalonSRX or VictorSPX you will need version 5, if do not you should be good with version 6. When in doubt you could load both.
//    Version 6 - https://maven.ctr-electronics.com/release/com/ctre/phoenix6/latest/Phoenix6-frc2024-latest.json
//    Version 5 - https://maven.ctr-electronics.com/release/com/ctre/phoenix/Phoenix5-frc2024-latest.json
// NavX Library - https://dev.studica.com/releases/2024/NavX.json
// Rev Robotics - https://software-metadata.revrobotics.com/REVLib-2024.json
// Venom - https://www.playingwithfusion.com/frc/playingwithfusion2024.json

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private DifferentialDrive m_robotDrive;
  private Joystick js_Driver;

  // Define and assign motors
  private static final int leftLeadDeviceID = 3;
  private static final int leftFollowDeviceID = 1;
  private static final int rightLeadDeviceID = 4;
  private static final int rightFollowDeviceID = 2;
  private CANSparkMax m_leftFollowMotor;
  private CANSparkMax m_leftLeadMotor;
  private CANSparkMax m_rightFollowMotor;
  private CANSparkMax m_rightLeadMotor;

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

    m_leftFollowMotor = new CANSparkMax(leftFollowDeviceID, MotorType.kBrushed);
    m_leftLeadMotor = new CANSparkMax(leftLeadDeviceID, MotorType.kBrushed);
    m_rightFollowMotor =
      new CANSparkMax(rightFollowDeviceID, MotorType.kBrushed);
    m_rightLeadMotor = new CANSparkMax(rightLeadDeviceID, MotorType.kBrushed);

    m_leftFollowMotor.setInverted(true);
    m_leftLeadMotor.setInverted(true);
    m_rightLeadMotor.setInverted(false);
    m_rightLeadMotor.setInverted(false);

    //region Set up Followers
    // This should go in robotIni typically but as we have a testPeriodic to test wheel direction we don't want to have followers setup at the moment
    m_leftFollowMotor.follow(m_leftLeadMotor);
    m_rightFollowMotor.follow(m_rightLeadMotor);
    //endregion Set up Followers

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
    // Arcade Drive, which controls a forward and turn speed
    // Arcade Drive - Logitech F310 Controller in D Switch Mode
    // m_robotDrive.arcadeDrive(-js_Driver.getY(), -js_Driver.getZ());
    m_robotDrive.arcadeDrive(
      -js_Driver.getRawAxis(1),
      -js_Driver.getRawAxis(2)
    );
    // Arcade Drive - Logitech F310 Controller in X Switch Mode
    // m_robotDrive.arcadeDrive(js_Driver.getRawAxis(1), js_Driver.getRawAxis(4));
    // Arcade Drive - XBox Controller
    // m_robotDrive.arcadeDrive(js_Driver.getRawAxis(1), js_Driver.getRawAxis(4));
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
    /**
     * The RestoreFactoryDefaults method can be used to reset the configuration parameters
     * in the SPARK MAX to their factory default state. If no argument is passed, these
     * parameters will not persist between power cycles
     */
    m_leftLeadMotor.restoreFactoryDefaults();
    m_leftFollowMotor.restoreFactoryDefaults();
    m_rightLeadMotor.restoreFactoryDefaults();
    m_rightFollowMotor.restoreFactoryDefaults();

    m_leftFollowMotor.setInverted(true);
    m_leftLeadMotor.setInverted(true);
    m_rightLeadMotor.setInverted(false);
    m_rightLeadMotor.setInverted(false);
  }

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
