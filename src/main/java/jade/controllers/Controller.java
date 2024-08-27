package jade.controllers;

import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Controller {

  private GLFWGamepadState state;
  private String name;
  private int controllerId;



  public Controller(int controllerId)  {
    this.controllerId = controllerId;
    this.name = glfwGetGamepadName(this.controllerId);

  }

  public void initialize() throws Exception {
    boolean isControlPresent = glfwJoystickPresent(this.controllerId) && glfwJoystickIsGamepad(this.controllerId);
    ByteBuffer byteBuffer = MemoryUtil.memAlloc(GLFWGamepadState.SIZEOF);
    this.state = new GLFWGamepadState(byteBuffer);
    if(!isControlPresent || !glfwGetGamepadState(this.controllerId, this.state)) {
      throw new Exception("could not initialize controller " + name + " " + controllerId);
    }
    System.out.println("Control " + name + " " + controllerId + " connected");
  }

  public boolean isButtonPressed(int button){
    if(glfwGetGamepadState(GLFW_JOYSTICK_1, this.getState())) {
      return this.getState().buttons(button) == GLFW_PRESS;
    }
    return false;
  }

  private GLFWGamepadState getState() {
    return this.state;
  }

}
