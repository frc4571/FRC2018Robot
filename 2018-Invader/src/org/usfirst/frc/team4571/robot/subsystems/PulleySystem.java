package org.usfirst.frc.team4571.robot.subsystems;

import org.usfirst.frc.team4571.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class PulleySystem extends Subsystem {
	private WPI_TalonSRX pulleyMotor;
	private final static double UP_SPEED   =  0.5,
							    DOWN_SPEED = -0.5;

    public void initDefaultCommand() {
    }
    
    public PulleySystem() {
    	this.pulleyMotor = new WPI_TalonSRX(RobotMap.PULLEY_MOTOR);
    }
    
    public void setPulley(double speed) {
    	this.pulleyMotor.set(speed);
    }
    
    public void goUp() {
    	setPulley(UP_SPEED);
    }
    
    public void goDown() {
    	setPulley(DOWN_SPEED);
    }
}

