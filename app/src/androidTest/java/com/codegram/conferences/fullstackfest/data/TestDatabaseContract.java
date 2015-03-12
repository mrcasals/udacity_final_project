package com.codegram.conferences.fullstackfest.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by marcriera on 3/12/15.
 */
public class TestDatabaseContract extends AndroidTestCase {

    // intentionally includes a slash to make sure Uri is getting quoted correctly
    private static final String TEST_TALK_ID = "/1";
    private static final long TEST_WEATHER_DATE = 1419033600L;  // December 20th, 2014

    /*
        Students: Uncomment this out to test your weather location function.
     */
    public void testBuildSpeakerTalk() {
        Uri locationUri = DatabaseContract.SpeakerEntry.buildSpeakerTalk(TEST_TALK_ID);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildSpeakerTalk in " +
                        "DatabaseContract.",
                locationUri);
        assertEquals("Error: Speaker talk not properly appended to the end of the Uri",
                "/speaker", locationUri.getLastPathSegment());
        assertEquals("Error: Speaker talk Uri doesn't match our expected result",
                locationUri.toString(),
                "content://com.codegram.conferences.fullstackfest/%2Ftalks/%2F1/%2Fspeaker");
    }
}