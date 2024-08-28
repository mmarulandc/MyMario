package util;

import jade.LevelEditorScene;
import jade.LevelScene;
import jade.Scene;

public class SceneFactory {
  public static Scene createScene(int type) {
    return switch (type) {
      case 1 -> new LevelScene();
      case 0 -> new LevelEditorScene();
      default -> null;
    };
  }

}
