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

#include "ctre/Phoenix.h"
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

  WPI_VictorSPX m_leftFollowMotor{12};
  WPI_VictorSPX m_leftLeadMotor{11};
  WPI_VictorSPX m_rightFollowMotor{9};
  WPI_VictorSPX m_rightLeadMotor{7};
  // WPI_TalonSRX m_leftFollowMotor{12};
  // WPI_TalonSRX m_leftLeadMotor{11};
  // WPI_TalonSRX m_rightFollowMotor{9};
  // WPI_TalonSRX m_rightLeadMotor{7};

  frc::DifferentialDrive m_robotDrive{
      [&](double output)
      { m_leftLeadMotor.Set(output); },
      [&](double output)
      { m_rightLeadMotor.Set(output); }};
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
    m_rightFollowMotor.Follow(m_rightLeadMotor);
    m_leftFollowMotor.Follow(m_leftLeadMotor);
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
    m_robotDrive.ArcadeDrive(-js_Driver.GetY(), -js_Driver.GetZ());
  }

  void TestInit() override
  {
    /* factory default values */
    m_leftLeadMotor.ConfigFactoryDefault();
    m_leftFollowMotor.ConfigFactoryDefault();
    m_rightLeadMotor.ConfigFactoryDefault();
    m_rightFollowMotor.ConfigFactoryDefault();
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
