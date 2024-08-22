package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

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

  private Window() {
    this.height = 1920;
    this.width = 1080;
    this.title = "Mario";
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

      glClearColor(1.0f, 0.0f, 0.f, 1.0f);
      glClear(GL_COLOR_BUFFER_BIT);

      glfwSwapBuffers(glfwWindow);
    }
  }
}
