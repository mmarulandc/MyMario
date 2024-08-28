package jade;

import jade.controllers.Controller;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import util.SceneFactory;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

  private static Window instance;

  String title;
  private final int width;
  private final int height;
  private long glfwWindow;
  public float r, g, b, a;

  private static Scene currentScene = null;

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

  public static void changeScene(int newScene){
    currentScene = SceneFactory.createScene(newScene);
    assert currentScene != null : "unknown scene '" + newScene + "'";
  }


  public static Window get() {
    if (instance == null) {
      instance = new Window();
    }
    return instance;
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

  public void fade(float dt) {
    this.r -= dt * 5.0f;
    this.g -= dt * 5.0f;
    this.b -= dt * 5.0f;
//    this.a -= dt * 5.0f;
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
    Window.changeScene(0);

  }

  public void loop(){

    float beginTime = Time.getTime();
    float endTime = Time.getTime();
    float dt = -1.0f;
    while(!glfwWindowShouldClose(glfwWindow)){



      // poll events
      glfwPollEvents();

      glClearColor(r, g, b, a);
      glClear(GL_COLOR_BUFFER_BIT);
      if(dt >= 0){

        currentScene.update(dt);
      }
//      System.out.printf("r %s g %s b %s%n", r, g, b);
//      System.out.println(currentScene);

      glfwSwapBuffers(glfwWindow);
      endTime = Time.getTime();
      dt = endTime - beginTime;
      beginTime = endTime;
//      float fps = 1/dt;
//      System.out.println(fps);

    }
  }
}
