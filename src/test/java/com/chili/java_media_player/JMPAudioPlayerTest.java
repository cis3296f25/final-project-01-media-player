package com.chili.java_media_player;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;

public class JMPAudioPlayerTest {

    private AudioPlayerInterface player;

    public JMPAudioPlayerTest() {
        this.player = new JMPAudioPlayer();
    }

    // does audio player load() work?
    // is the filepath loaded()
    @Test
    public void TestLoad() {
        String audio_source_test = "src/test/java/resources/야생ｋｉｎｅｔｉｃ꿈 - icy constellations [2901409654].mp3";
        player.load(audio_source_test);
        assertEquals(player.getCurrentTrack(), audio_source_test);

    }

    // Does pausing work?
    @Test
    void testPause() {
        String audio_source_test = "src/test/java/resources/야생ｋｉｎｅｔｉｃ꿈 - icy constellations [2901409654].mp3";
        player.load(audio_source_test);
        player.play();
        player.pause();
        assertFalse(this.player.currentlyPlaying());

    }

    // Does Playing work?
    @Test
    void testPlay() {
        String audio_source_test = "src/test/java/resources/야생ｋｉｎｅｔｉｃ꿈 - icy constellations [2901409654].mp3";
        player.load(audio_source_test);
        player.play();
        assertTrue(this.player.currentlyPlaying());
    }

}
