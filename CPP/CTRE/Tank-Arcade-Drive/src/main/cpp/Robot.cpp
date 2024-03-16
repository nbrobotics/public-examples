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
  // frc::WPI_TalonSRX  m_leftMotor{0};
  WPI_VictorSPX m_leftMotor{11};
  WPI_VictorSPX m_rightMotor{7};
  frc::DifferentialDrive m_robotDrive{
      [&](double output)
      { m_leftMotor.Set(output); },
      [&](double output)
      { m_rightMotor.Set(output); }};
  frc::Joystick m_stick{0};

  frc::Joystick js_Driver{0};
  std::string m_autoSelected;

public:
  Robot()
  {
    wpi::SendableRegistry::AddChild(&m_robotDrive, &m_leftMotor);
    wpi::SendableRegistry::AddChild(&m_robotDrive, &m_rightMotor);
  }

  void RobotInit() override
  {
    m_chooser.SetDefaultOption("Default", "Default");
    m_chooser.AddOption("Small Drive", "Small Drive");
    frc::SmartDashboard::PutData("Auto Modes", &m_chooser);
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightMotor.SetInverted(true);
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
};

#ifndef RUNNING_FRC_TESTS
int main()
{
  return frc::StartRobot<Robot>();
}
#endif
