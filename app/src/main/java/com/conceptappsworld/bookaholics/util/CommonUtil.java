package com.conceptappsworld.bookaholics.util;

import android.text.TextUtils;
import android.util.Log;

import com.conceptappsworld.bookaholics.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

    private static final String LOG_TAG = "CommonUtil";

    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing the given JSON response.
     */
    public static List<Book> extractFeatureFromJson(String bookJson) {

        /*
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<Book>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJson);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items (or books).
            JSONArray itemsArray = baseJsonResponse.getJSONArray(ConstantUtil.NODE_ITEMS);

            // For each item in the itemsArray, create an {@link Book} object
            for (int i = 0; i < itemsArray.length(); i++) {
                // Get a single book at position i within the list of itemsArray
                JSONObject currentBook = itemsArray.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of all properties
                // for that book.
                JSONObject properties = currentBook.getJSONObject(ConstantUtil.NODE_VOLUMEINFO);

                String title = "";
                // Extract the value for the key called "title"
                if (properties.has(ConstantUtil.NODE_TITLE)) {
                    title = properties.getString(ConstantUtil.NODE_TITLE);
                }

                String publisher = "";
                // Extract the value for the key called "publisher"
                if (properties.has(ConstantUtil.NODE_PUBLISHER)) {
                    publisher = properties.getString(ConstantUtil.NODE_PUBLISHER);
                }

                String description = "";
                // Extract the value for the key called "description"
                if (properties.has(ConstantUtil.NODE_DESCRIPTION)) {
                    description = properties.getString(ConstantUtil.NODE_DESCRIPTION);
                }

                String infoLink = "";
                // Extract the value for the key called "infoLink"
                if (properties.has(ConstantUtil.NODE_INFOLINK)) {
                    infoLink = properties.getString(ConstantUtil.NODE_INFOLINK);
                }

                JSONArray arrAuthors;
                ArrayList<String> alAuthors = new ArrayList<String>();
                if (properties.has(ConstantUtil.NODE_AUTHORS)) {
                    arrAuthors = properties.getJSONArray(ConstantUtil.NODE_AUTHORS);
                    if(arrAuthors != null && arrAuthors.length() > 0){
                        for(int j=0; j < arrAuthors.length(); j++){
                            alAuthors.add(arrAuthors.getString(j));
                        }
                    }
                }

                Book book = new Book(title, description, publisher, infoLink);

                if(alAuthors.size() > 0){
                    book.setAuthors(alAuthors);
                }

                books.add(book);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }

    public static String replace(String str) {
        String[] words = str.split(" ");
        StringBuilder sentence = new StringBuilder(words[0]);

        for (int i = 1; i < words.length; ++i) {
            sentence.append("%20");
            sentence.append(words[i]);
        }

        return sentence.toString();
    }
}
