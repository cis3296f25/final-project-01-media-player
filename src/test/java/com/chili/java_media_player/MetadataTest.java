package com.chili.java_media_player;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for the all metadata related functions
 */
public class MetadataTest {

    private Controller controller;
    private JMPAudioPlayer player;


     //Setup functions:
    @BeforeEach
    void setUp() {
        // Initialize the components we are testing
        controller = new Controller();
        // Needed for metadata functions in JMPAudioPlayer
        player = new JMPAudioPlayer();
    }

    //Tests:

    // Test proceesMetadataForDisplay with a full map
    @Test
    void testProcessMetadataFullData() {
        // Create an example map
        ObservableMap<String, Object> testMetadata = FXCollections.observableHashMap();
        testMetadata.put("artist", "Test Artist");
        testMetadata.put("title", "Test Song");
        testMetadata.put("album", "Test Album");
        testMetadata.put("year", 2025); // Integer/Number type (prob will never be an in type since id3 is weird)
        testMetadata.put("track", 5);
        testMetadata.put("comment-0", "Test Comment");

        // Run it through the helper function
        List<String> result = controller.processMetadataForDisplay(testMetadata);

        // Verify all 6 expected fields are present and correctly formatted
        assertEquals(6, result.size(), "Result list should have 6 entries.");
        assertEquals("Artist: Test Artist", result.get(0));
        assertEquals("Track Title: Test Song", result.get(1));
        assertEquals("Album: Test Album", result.get(2));
        assertEquals("Date: 2025", result.get(3));
        assertEquals("Track Number: 5", result.get(4));
        assertEquals("Comment: Test Comment", result.get(5));
    }

    // Test proceesMetadataForDisplay with a partial map
    @Test
    void testProcessMetadataMissingData() {
        // Map with only artist and album, others missing
        ObservableMap<String, Object> testMetadata = FXCollections.observableHashMap();
        testMetadata.put("artist", "Test Artist");
        testMetadata.put("album", "Test Album");

        // Run it through the helper function
        List<String> result = controller.processMetadataForDisplay(testMetadata);

        // Verify present fields are correct, and missing fields are empty
        assertEquals(6, result.size());
        assertEquals("Artist: Test Artist", result.get(0)); // Present
        assertEquals("Track Title: ", result.get(1));              // Missing
        assertEquals("Album: Test Album", result.get(2));           // Present
        assertEquals("Date: ", result.get(3));                    // Missing
        assertEquals("Track Number: ", result.get(4));             // Missing
        assertEquals("Comment: ", result.get(5));                 // Missing
    }

    // Test proceesMetadataForDisplay with an empty map
    @Test
    void testProcessMetadataEmptyMap() {
        // Empty map
        ObservableMap<String, Object> testMetadata = FXCollections.observableHashMap();

        // Run it through the helper function
        List<String> result = controller.processMetadataForDisplay(testMetadata);

        // All fields should be present but empty
        assertEquals(6, result.size());
        assertEquals("Artist: ", result.get(0));
        assertEquals("Track Title: ", result.get(1));
    }

    // Test if the map is null, should never happen but its an error backup
    @Test
    void testProcessMetadataNullInput() {
        // Run null through the helper function
        List<String> result = controller.processMetadataForDisplay(null);

        // Should return the "No Metadata Available" message

        assertEquals(1, result.size());
        assertEquals("No Metadata Available", result.get(0));
    }

    @Test
    void testProcessMetadataCommentRemoval() {
        // Test case for comment language format "[eng]=actual comment"
        ObservableMap<String, Object> testMetadata = FXCollections.observableHashMap();
        testMetadata.put("comment-0", "[eng]=This is the hidden comment.");

        // Run through processMetadataForDisplay
        List<String> result = controller.processMetadataForDisplay(testMetadata);

        // Check the comment field (index 5) has the prefix removed
        assertEquals("Comment: This is the hidden comment.", result.get(5));
    }

    // Test that stuff isnt being removed that shouldnt be
    @Test
    void testProcessMetadataCommentNoRemoval() {
        // Test case for a standard comment that should not be modified
        ObservableMap<String, Object> testMetadata = FXCollections.observableHashMap();
        testMetadata.put("comment-0", "Standard comment format.");

        // Run through processMetadataForDisplay
        List<String> result = controller.processMetadataForDisplay(testMetadata);

        // Check the comment field (index 5) is unchanged
        assertEquals("Comment: Standard comment format.", result.get(5));
    }
}