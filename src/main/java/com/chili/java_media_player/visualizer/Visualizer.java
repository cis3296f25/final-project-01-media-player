package com.chili.java_media_player.visualizer;

// import java.aswt.Color;
import java.util.ArrayList;
import java.util.List;

import com.chili.java_media_player.AudioPlayerInterface;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
// import javafx.scene.paint.Paint;
// import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Visualizer implements SpectrumDataListener {

    private Canvas canvas;
    private GraphicsContext graphics_context;

    public Visualizer(AudioPlayerInterface player, Canvas visualizerCanvas) {
        player.setSpectrumListener(this);
        this.canvas = visualizerCanvas;
        this.graphics_context = this.canvas.getGraphicsContext2D();
        // System.err.println("Listener added?");
    }

    @Override
    public void onSpectrum(double ts, double duration, float[] mag, float[] phases) {
        // for (float f : mag) {
        // System.out.print(f + " ");
        // }
        // System.err.println("");
        drawCanvas(mag);
    }

    /**
     * Function that draws to the visualizer canvas
     * 
     * @param mag waveform data from spectrum listener
     */
    private void drawCanvas(float[] mag) {

        // calculate the number of bars in the graph - since some magnitude values are
        // zero
        // find the mag floats that are not zero, put in list and get the length
        List<Float> active_magFloats = new ArrayList<>();
        for (Float magnitudeFloat : mag) {
            if (magnitudeFloat > -60.0) {
                active_magFloats.add(magnitudeFloat);
            }
        }

        int number_of_bars = active_magFloats.size();

        // calculate the width of the bars - use live code reloading for tweaking
        // visualization
        // Clear Graphics Buffer
        this.graphics_context.setFill(Color.WHITE);
        graphics_context.fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        this.graphics_context.setFill(Color.BLUE);
        this.graphics_context.setStroke(Color.DODGERBLUE);
        // draw each line

        double barWidth = canvas.getWidth() / number_of_bars;
        // this.graphics_context.setLineWidth(barWidth * 0.70);
        for (int i = 0; i < number_of_bars; i++) {
            // the magnitude of the frequency
            float magValue = active_magFloats.get(i);

            // determining x and y coords
            double x = i * barWidth;
            double scaledHeight = (magValue + 60) / 60 * canvas.getHeight(); // map -60..0 dB to 0..canvasHeight
            double y = canvas.getHeight() - scaledHeight;

            // primary line
            this.graphics_context.setLineWidth(barWidth * 0.70);
            graphics_context.strokeLine(x, canvas.getHeight(), x, y);
            // smaller line for more visual flare
            this.graphics_context.setLineWidth(barWidth * 1.00);
            graphics_context.strokeLine(x, canvas.getHeight(), x, y + (canvas.getHeight() / 6));
        }

    }

}
