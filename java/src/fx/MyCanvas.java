package fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class MyCanvas extends Application {

    @Override
    public void start(Stage stage) {

        initUI(stage);
    }

    private void initUI(Stage stage) {

        Pane root = new Pane();

        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 800, 600, Color.WHITE);

        stage.setTitle("Fractal");
        stage.setScene(scene);
        stage.show();
        //for (int i=1;i<15;i++) {
          drawTree(gc, 400, 500, -90, 10);
        //}
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void drawTree(GraphicsContext gc, int x1, int y1, double angle, int depth) {
        if (depth == 0) return;
        int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * depth * 5.0);
        int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * depth * 5.0);
        gc.setLineWidth(1.5);
        gc.strokeLine(x1, y1, x2, y2);
        drawTree(gc, x2, y2, angle + (3 * depth), depth - 1);
        drawTree(gc, x2, y2, angle - (3 * depth), depth - 1);
    }
}
