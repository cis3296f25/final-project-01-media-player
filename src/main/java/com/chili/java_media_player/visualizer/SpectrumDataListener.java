package com.chili.java_media_player.visualizer;

public interface SpectrumDataListener {
    /**
     * Function called from the JMPAudioPlayer when spectrum data is generated. it's
     * sent to this class to digest the magnitude data and to draw the waveform.
     * 
     * @param ts
     * @param duration
     * @param mag
     * @param phases
     */
    void onSpectrum(double ts, double duration, float[] mag, float[] phases);
}
