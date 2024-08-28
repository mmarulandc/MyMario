package jade;

import java.awt.event.KeyEvent;
import java.sql.SQLOutput;

public class LevelEditorScene extends Scene {

  private boolean changingScene = false;

  private float timeToChangeScene = 2.0f;
  public LevelEditorScene() {
    System.out.println("Creating level editor scene");

  }

  private boolean getChangingScene() {
    return this.changingScene;
  }

  private void setChangingScene(boolean changingScene) {
    this.changingScene = changingScene;
  }

  @Override
  public void update(float dt) {
     if (!this.getChangingScene() && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
      this.setChangingScene(true);
    }


    if(this.getChangingScene() && timeToChangeScene > 0) {
      timeToChangeScene -= dt;
      Window.get().fade(dt);

    }
    else if(this.getChangingScene()) {
      Window.changeScene(1);
    }
  }
}
