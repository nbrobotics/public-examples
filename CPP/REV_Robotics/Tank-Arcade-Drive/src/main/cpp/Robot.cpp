// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// CTRE
//    If you use TalonSRX or VictorSPX you will need version 5, if do not you should be good with version 6. When in doubt you could load both.
//    Version 6 - https://maven.ctr-electronics.com/release/com/ctre/phoenix6/latest/Phoenix6-frc2024-latest.json
//    Version 5 - https://maven.ctr-electronics.com/release/com/ctre/phoenix/Phoenix5-frc2024-latest.json
// NavX Library - https://dev.studica.com/releases/2024/NavX.json
// Rev Robotics - https://software-metadata.revrobotics.com/REVLib-2024.json
// Venom - https://www.playingwithfusion.com/frc/playingwithfusion2024.json

#include "rev/CANSparkMax.h"
#include "frc/TimedRobot.h"
#include "frc/smartdashboard/SmartDashboard.h"
#include <fmt/core.h>
#include <frc/Joystick.h>
#include <frc/drive/DifferentialDrive.h>
#include <frc/smartdashboard/SendableChooser.h>
#include <iostream>
#include <string>

/**
 * This is a demo program showing the use of the DifferentialDrive class.
 * Runs the motors with arcade steering.
 */
class Robot : public frc::TimedRobot
{
  frc::SendableChooser<std::string> m_chooser;

  static const int leftLeadDeviceID = 3, leftFollowDeviceID = 1, rightLeadDeviceID = 4, rightFollowDeviceID = 2;
  rev::CANSparkMax m_leftLeadMotor{leftLeadDeviceID, rev::CANSparkMax::MotorType::kBrushed};
  rev::CANSparkMax m_rightLeadMotor{rightLeadDeviceID, rev::CANSparkMax::MotorType::kBrushed};
  rev::CANSparkMax m_leftFollowMotor{leftFollowDeviceID, rev::CANSparkMax::MotorType::kBrushed};
  rev::CANSparkMax m_rightFollowMotor{rightFollowDeviceID, rev::CANSparkMax::MotorType::kBrushed};

  /**
   * In RobotInit() below, we will configure m_leftFollowMotor and m_rightFollowMotor to follow
   * m_leftLeadMotor and m_rightLeadMotor, respectively.
   *
   * Because of this, we only need to pass the lead motors to m_robotDrive. Whatever commands are
   * sent to them will automatically be copied by the follower motors
   */
  frc::DifferentialDrive m_robotDrive{m_leftLeadMotor, m_rightLeadMotor};

  frc::Joystick js_Driver{0};
  std::string m_autoSelected;

public:
  Robot()
  {
    wpi::SendableRegistry::AddChild(&m_robotDrive, &m_leftLeadMotor);
    wpi::SendableRegistry::AddChild(&m_robotDrive, &m_rightLeadMotor);
  }
  void RobotInit() override
  {
    m_chooser.SetDefaultOption("Default", "Default");
    m_chooser.AddOption("Small Drive", "Small Drive");
    frc::SmartDashboard::PutData("Auto Modes", &m_chooser);
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightLeadMotor.SetInverted(true);
    m_rightFollowMotor.SetInverted(true);
    m_leftLeadMotor.SetInverted(false);
    m_leftFollowMotor.SetInverted(false);

    /* set up followers */
    /**
     * In CAN mode, one SPARK MAX can be configured to follow another. This is done by calling
     * the Follow() method on the SPARK MAX you want to configure as a follower, and by passing
     * as a parameter the SPARK MAX you want to configure as a leader.
     *
     * This is shown in the example below, where one motor on each side of our drive train is
     * configured to follow a lead motor.
     */
    m_leftFollowMotor.Follow(m_leftLeadMotor);
    m_rightFollowMotor.Follow(m_rightLeadMotor);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p> This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  void RobotPeriodic() override
  {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the GetString line to get the
   * auto name from the text box below the Gyro.
   *
   * You can add additional auto modes by adding additional comparisons to the
   * if-else structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  void AutonomousInit() override
  {
    m_autoSelected = m_chooser.GetSelected();
    // m_autoSelected = SmartDashboard::GetString("Auto Selector",
    //     kAutoNameDefault);
    fmt::print("Auto selected: {}\n", m_autoSelected);

    if (m_autoSelected == "Small Drive")
    {
      // Custom Auto goes here
    }
    else
    {
      // Default Auto goes here
    }
  }

  void AutonomousPeriodic() override
  {
    if (m_autoSelected == "Small Drive")
    {
      // Custom Auto goes here
    }
    else
    {
      // Default Auto goes here
    }
  }

  void TeleopInit() override
  {
  }

  void TeleopPeriodic() override
  {
    // Drive with arcade style
    m_robotDrive.ArcadeDrive(js_Driver.GetY(), js_Driver.GetZ());
  }

  void TestInit() override
  {
    m_leftLeadMotor.RestoreFactoryDefaults();
    m_rightLeadMotor.RestoreFactoryDefaults();
    m_leftFollowMotor.RestoreFactoryDefaults();
    m_rightFollowMotor.RestoreFactoryDefaults();
  }

  void TestPeriodic() override
  {
    if (js_Driver.GetRawButton(1))
    {
      m_leftLeadMotor.Set(0.5);
    }
    else
    {
      m_leftLeadMotor.Set(0);
    }

    if (js_Driver.GetRawButton(2))
    {
      m_leftFollowMotor.Set(0.5);
    }
    else
    {
      m_leftFollowMotor.Set(0);
    }

    if (js_Driver.GetRawButton(3))
    {
      m_rightLeadMotor.Set(0.5);
    }
    else
    {
      m_rightLeadMotor.Set(0);
    }

    if (js_Driver.GetRawButton(4))
    {
      m_rightFollowMotor.Set(0.5);
    }
    else
    {
      m_rightFollowMotor.Set(0);
    }
  }
};

#ifndef RUNNING_FRC_TESTS
int main()
{
  return frc::StartRobot<Robot>();
}
#endif
