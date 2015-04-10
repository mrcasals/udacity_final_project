package com.codegram.conferences.fullstackfest.config;

import android.graphics.Color;

import com.codegram.conferences.fullstackfest.models.Talk;

import java.util.Arrays;

/**
 * Created by marc on 2/11/15.
 */
public class FullStackFestConfig {
    public static final String BARUCO_COLOR = "#FFC107";
    public static final String FUTUREJS_COLOR = "#009688";
    public static final String BARUCO_COLOR_DARK = "#FFA000";
    public static final String FUTUREJS_COLOR_DARK = "#00796B";
    public static final String API_ENDPOINT = "http://conferences.codegram.com/api/full-stack-fest/talks/";
    public static final String LATLONG = "41.389280,2.137002";
    public static final String VENUE_NAME = "Auditori AXA";
    public static final String[] TRACKS = new String[] {"ruby", "javascript"};
    public static final String HASHTAG = "fullstackfest";

    public static int getConfColor(Talk talk) {
        if(Arrays.asList(talk.getTags()).contains("ruby"))
            return Color.parseColor(FullStackFestConfig.BARUCO_COLOR);
        return Color.parseColor(FullStackFestConfig.FUTUREJS_COLOR);
    }

    public static int getConfDarkColor(Talk talk) {
        if(Arrays.asList(talk.getTags()).contains("ruby"))
            return Color.parseColor(FullStackFestConfig.BARUCO_COLOR_DARK);
        return Color.parseColor(FullStackFestConfig.FUTUREJS_COLOR_DARK);
    }
}
