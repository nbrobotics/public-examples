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

  // Define and assign motors
  private int leftLeadDeviceID = 3, leftFollowDeviceID = 1, rightLeadDeviceID =
    4, rightFollowDeviceID = 2;

  private final CANSparkMax m_leftFollowMotor = new CANSparkMax(
    leftFollowDeviceID,
    MotorType.kBrushed
  );
  private final CANSparkMax m_leftLeadMotor = new CANSparkMax(
    leftLeadDeviceID,
    MotorType.kBrushed
  );
  private final CANSparkMax m_rightFollowMotor = new CANSparkMax(
    rightFollowDeviceID,
    MotorType.kBrushed
  );
  private final CANSparkMax m_rightLeadMotor = new CANSparkMax(
    rightLeadDeviceID,
    MotorType.kBrushed
  );

  private DifferentialDrive m_robotDrive = new DifferentialDrive(
    m_leftLeadMotor::set,
    m_rightLeadMotor::set
  );
  private Joystick js_Driver = new Joystick(0);

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

    m_leftFollowMotor.follow(m_leftLeadMotor);
    m_rightFollowMotor.follow(m_rightLeadMotor);
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
    // Only use one of the two and double check the axis you want to use within the Driver Station application

    // Tank Drive, which controls the left and right side independently
    // Tank Drive - Logitech F310 Controller in D Switch Mode
    // m_robotDrive.tankDrive(js_Driver.getRawAxis(1), js_Driver.getRawAxis(3));
    // Tank Drive - Logitech F310 Controller in X Switch Mode
    // m_robotDrive.tankDrive(js_Driver.getRawAxis(1), js_Driver.getRawAxis(5));
    // Tank Drive - XBox Controller
    // m_robotDrive.tankDrive(js_Driver.getRawAxis(1), js_Driver.getRawAxis(5));

    // Arcade Drive, which controls a forward and turn speed
    // Arcade Drive - Logitech F310 Controller in D Switch Mode
    m_robotDrive.arcadeDrive(-js_Driver.getY(), -js_Driver.getZ());
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
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
