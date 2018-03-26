package org.usfirst.frc.team4571.robot.subsystems;

import org.usfirst.frc.team4571.robot.RobotMap;
import org.usfirst.frc.team4571.robot.subsystems.pid.TurnOutput;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This subsystem contains all the components of the drive system.
 * Such as:
 * 
 * <li> The motors for the transmissions
 * <li> The encoders
 * <li> A gyroscope
 * 
 * @author Mahim
 */
public class DriveSystem extends Subsystem {
	private WPI_TalonSRX 	    topLeftMotor,
							    bottomLeftMotor,
						        topRightMotor,
							    bottomRightMotor;
	private DifferentialDrive   differentialDrive;
	private final AHRS		    navX;
	private final TurnOutput    turnOutput;
	private final PIDController turnController;
	private static final double rotate_K = 0.0,
								rotate_I = 0.0,
								rotate_D = 0.0;
								
	public DriveSystem() {
		this.topLeftMotor 	  = new WPI_TalonSRX(RobotMap.TOP_LEFT_MOTOR);
		this.bottomLeftMotor  = new WPI_TalonSRX(RobotMap.BOTTOM_LEFT_MOTOR);
		this.topRightMotor 	  = new WPI_TalonSRX(RobotMap.TOP_RIGHT_MOTOR);
		this.bottomRightMotor = new WPI_TalonSRX(RobotMap.BOTTOM_RIGHT_MOTOR);
		
		this.topLeftMotor.setExpiration(0.1);
		this.bottomLeftMotor.setExpiration(0.1);
		this.topRightMotor.setExpiration(0.1);
		this.bottomRightMotor.setExpiration(0.1);
		
		this.topLeftMotor.setSafetyEnabled(false);
		this.bottomLeftMotor.setSafetyEnabled(false);
		this.topRightMotor.setSafetyEnabled(false);
		this.bottomRightMotor.setSafetyEnabled(false);
		
		topLeftMotor.setNeutralMode(NeutralMode.Brake);
		bottomLeftMotor.setNeutralMode(NeutralMode.Brake);
		topRightMotor.setNeutralMode(NeutralMode.Brake);
		bottomRightMotor.setNeutralMode(NeutralMode.Brake);
		
		topLeftMotor.setInverted(true);
		bottomLeftMotor.setInverted(true);
		topRightMotor.setInverted(true);
		bottomRightMotor.setInverted(true);
		
		SpeedControllerGroup leftMotors  = new SpeedControllerGroup(topLeftMotor, bottomLeftMotor);
		SpeedControllerGroup rightMotors = new SpeedControllerGroup(topRightMotor, bottomRightMotor);
		
		this.differentialDrive = new DifferentialDrive(leftMotors, rightMotors);
		this.differentialDrive.setExpiration(0.1);
		this.differentialDrive.setSafetyEnabled(false);
		
		this.navX 		    = new AHRS(Port.kMXP);
		this.turnOutput     = new TurnOutput(differentialDrive);
		this.turnController = new PIDController(rotate_K, rotate_I, rotate_D, navX, turnOutput);
	}
	
	public void initDefaultCommand() {}
	
	public enum TransmissionState {
		HighGear, LowGear;
		
		private TransmissionState transmissionState;
		
		public TransmissionState getTransmissionState() {
			return transmissionState;
		}

		public void setTransmissionState(TransmissionState transmissionState) {
			this.transmissionState = transmissionState;
		}
	}
	
	/**
	 * This method is used to drive the robot. It can also be used to directly set
	 * the power of the motors during autonomous if wanted.
	 * 
	 * @param left	The robot left side's speed along the X axis [-1.0..1.0]. Forward is
	 *              positive.
	 * @param right	The robot right side's speed along the X axis [-1.0..1.0]. Forward is
	 *              positive.
	 */
	public void drive(double left, double right) {
		this.differentialDrive.tankDrive(left, right);
	}
	
	public double getTopLeftMotorSpeed() {
		return this.topLeftMotor.get();
	}
	
	public double getBottomLeftMotorSpeed() {
		return this.bottomLeftMotor.get();
	}
	
	public double getTopRightMotorSpeed() {
		return this.topRightMotor.get();
	}
	
	public double getBottomRightMotorSpeed() {
		return this.bottomRightMotor.get();
	}
	
	public void stop() {
		this.drive(0.0, 0.0);
	}
	
	public void resetNavX() {
		this.navX.reset();
	}
	
	public double getAngle() {
		return this.navX.getAngle();
	}
	
	public boolean isAngleOnTarget() {
		return this.turnController.onTarget();
	}
	
	public PIDController getTurnController() {
		return this.turnController;
	}
	
	public void setAnglePIDParameter(double angleSetPoint) {
		turnController.reset();	
		turnController.setInputRange(-180.0f, 180.0f);
		turnController.setOutputRange(-0.6, 0.6);
		turnController.setSetpoint(angleSetPoint);
		turnController.setAbsoluteTolerance(5.0f);
		turnController.enable();
	}
	
	public void disablePID() {
		this.turnController.disable();
	}
}