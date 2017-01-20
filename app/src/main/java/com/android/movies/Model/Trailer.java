package com.android.movies.Model;

/**
 * Model to store Trailer details for a particular moviw
 */

public class Trailer {
    private String trailerTitle;
    private String trailerKey;
    private String trailerSite;

    public Trailer(String title, String key, String site) {
        trailerTitle = title;
        trailerKey = key;
        trailerSite = site;
    }

    public String getTrailerTitle() {
        return trailerTitle;
    }

    public String getTrailerKey() {
        return trailerKey;
    }

    public String getTrailerSite() {
        return trailerSite;
    }
}
