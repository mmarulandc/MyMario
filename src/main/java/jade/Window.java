package jade;

import jade.controllers.Controller;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

  private static Window window;
  String title;
  private final int width;
  private final int height;
  private long glfwWindow;
  private float r, g, b, a;
  private boolean fadeToBlack = false;

  private Controller playerOne;


  private Window() {
    this.height = 1920;
    this.width = 1080;
    this.title = "Mario";
    this.r = 1;
    this.g = 1;
    this.b = 1;
    this.a = 1;
  }

  public static Window get() {
    if (window == null) {
      return new Window();
    }
    return Window.window;
  }

  public void run() {
    System.out.println("Hello LWJGL" + Version.getVersion());
    init();
    loop();

    // Free the memory
    glfwFreeCallbacks(glfwWindow);
    glfwDestroyWindow(glfwWindow);

    // Terminate GLFW and the free the error callback
    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  public void init() {
    GLFWErrorCallback.createPrint(System.err);

    // Initialize GLFW
    if (!glfwInit()) {
      throw new IllegalArgumentException("unable to initialize GLFW");
    }

    // Configure GLFW
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

    // Create Window
    glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

    if (glfwWindow == NULL) {
      throw new IllegalArgumentException("failed to create GLFW window");
    }

    //setting mouse listener callbacks
    glfwSetCursorPosCallback(glfwWindow, MouseListener::cursorPositionCallback);
    glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
    glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
    glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
    playerOne = new Controller(GLFW_JOYSTICK_1);

    try {
      playerOne.initialize();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }



//    glfwSetJoystickCallback(ControllerListener::joystickCallback);

    // Make the OpenGL context current
    glfwMakeContextCurrent(glfwWindow);

    // enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(glfwWindow);

    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    createCapabilities();

  }

  public void loop(){
    while(!glfwWindowShouldClose(glfwWindow)){
      // poll events
      glfwPollEvents();

      glClearColor(r, g, b, a);
      glClear(GL_COLOR_BUFFER_BIT);

      if (fadeToBlack) {
        r = Math.max(r - 0.01f, 0);
        g = Math.max(g - 0.01f, 0);
        b = Math.max(b - 0.01f, 0);
        a = Math.max(a - 0.01f, 0);
      }

//      if(ControllerListener.isButtonPressed(GLFW_GAMEPAD_BUTTON_A)) {
//        System.out.println("gay");
//      }

      if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
        System.out.println("Space key is pressed");
        fadeToBlack = true;

      } else if(KeyListener.isKeyPressed(GLFW_KEY_W)) {
        System.out.println("W key is pressed");
      }
      if(playerOne.isButtonPressed(GLFW_GAMEPAD_BUTTON_B)) {
        System.out.println("pressed");
      }




        glfwSwapBuffers(glfwWindow);


    }
  }
}
