package com.chili.java_media_player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void testPlay() throws InterruptedException{
        String audio_source_test = "src/test/java/resources/야생ｋｉｎｅｔｉｃ꿈 - icy constellations [2901409654].mp3";
        player.load(audio_source_test);
        player.play();
        Thread.sleep(100);
        assertTrue(this.player.currentlyPlaying());
    }

    // what happens if an audio file is loaded in while another clip is playing?
    @Test
    void LoadingAudioFileWhileAudioIsPlaying() throws InterruptedException {
        String audio_source_test = "src/test/java/resources/야생ｋｉｎｅｔｉｃ꿈 - icy constellations [2901409654].mp3";
        String audio_source_test_second = "src/test/java/resources/야생ｋｉｎｅｔｉｃ꿈 - visible stars [4164720865].mp3";
        player.load(audio_source_test);
        player.play();
        Thread.sleep(100);
        player.load(audio_source_test_second);
    }

}
