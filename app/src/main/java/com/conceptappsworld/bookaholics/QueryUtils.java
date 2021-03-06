package com.conceptappsworld.bookaholics;

import android.util.Log;

import com.conceptappsworld.bookaholics.model.Book;
import com.conceptappsworld.bookaholics.util.CommonUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = "QueryUtils";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }

    /**
     * Query the Google Book API and return a list of {@link Book} objects.
     */
    public static List<Book> fetchBooksData(String requestUrl) {
        // Create URL object
        URL url = CommonUtil.createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = CommonUtil.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> books = CommonUtil.extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Book}s
        return books;
    }
}
