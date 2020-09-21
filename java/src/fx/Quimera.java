package fx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Quimera
{
private int factor = 2;
private int duration = 2;
private int number = 0;
private int generation = 0;
private double posX=0;
private double posY=0;
private GraphicsContext gc=null;

public Quimera(GraphicsContext aGc, double aPosX, double aPosY, int n, int g) {
    gc=aGc;
    posX=aPosX;
    posY=aPosY;
    number=n;
    generation=g;
    draw();
}

private void draw()
{
    new Thread() {
        @Override
        public void run() {
            double fx=0;
            double fy=0;
            try {
                for (int i = 0; i < duration;i++) {
                    gc.setStroke(Color.BLUE);
                    gc.setLineWidth(1);
                    fx=posX +(generation*i);
                    fy=posY-(number*i);
                    if (fx < 0 || fx > 300) break;
                    if (fy < 0 || fy > 300) break;
                    gc.strokeLine(posX, posY, fx, fy);
                    posX=fx;
                    posY=fy;
                    sleep(100);
                }
                int g=generation++;
                for (int i = 0; i < factor;i++) {
                    new Quimera(gc, posX, posY, i, g);
                }
                join();
            } catch (IllegalStateException e) {
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }.start();
    
}

}
