package com.chili.java_media_player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Deterministic shuffle test using a fixed seed to ensure reproducible order.
 */
public class PlaylistShuffleTest {

    private Playlist playlist;

    @BeforeEach
    public void setUp() {
        playlist = new Playlist();
    }

    @Test
    public void testShuffleWithSeedProducesDeterministicOrder() {
        List<String> original = Arrays.asList("song1.mp3", "song2.mp3", "song3.mp3", "song4.mp3", "song5.mp3");
        for (String s : original) {
            playlist.addTrack(s);
        }

    long seed = 0L;

        // Create expected order by shuffling a copy with the same seed
        List<String> expected = new ArrayList<>(original);
        Collections.shuffle(expected, new Random(seed));

        playlist.shuffle(seed);

        List<String> actual = playlist.getAllTracks();

        assertEquals(expected, actual, "Playlist.shuffle(seed) should produce the same deterministic order as Collections.shuffle with the same seed");

        assertEquals(expected.get(0), playlist.getCurrentTrack());
    }
}
