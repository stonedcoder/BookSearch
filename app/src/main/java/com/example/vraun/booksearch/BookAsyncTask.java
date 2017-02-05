package com.example.vraun.booksearch;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by vraun on 02-02-2017.
 */

public class BookAsyncTask extends AsyncTask<String, Void, String> {
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> authors = new ArrayList<>();

    public AsyncResponseInterface responseAsync = null;

    public BookAsyncTask(AsyncResponseInterface asyncResponse) {
        responseAsync  = asyncResponse;
    }

    @Override
    protected String doInBackground(String... url) {
        String jsonResponse = "";
        for (String searchUrl : url) {
            URL urlObject = createUrl(searchUrl);
            if (url != null) {
                try {
                    jsonResponse = makeHttpRequest(urlObject);
                } catch (IOException e) {
                    Log.e("BookAsyncTask", "Not able to get JsonResponse!!!");
                }
            }
        }
        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray booksArray = jsonObject.getJSONArray("items");
            JSONObject booksObject;
            JSONObject partObject;
            for (int i = 0; i < booksArray.length(); i++) {
                booksObject = booksArray.getJSONObject(i);
                partObject = booksObject.getJSONObject("volumeInfo");
                try {
                    title.add("Title : " + partObject.getString("title"));
                } catch (JSONException jse) {
                    title.add("Nothing");
                }
                StringBuilder authorStringBuilder = new StringBuilder("Author : ");
                try {
                    JSONArray authorArray = partObject.getJSONArray("authors");
                    for (int j = 0; j < authorArray.length(); j++) {
                        if (j > 0) {
                            authorStringBuilder.append(", ");
                        }
                        authorStringBuilder.append(authorArray.getString(j));
                    }
                    authors.add(authorStringBuilder.toString());
                } catch (JSONException jse) {
                    authors.add("N/A");
                }
            }
            responseAsync.Finish(title, authors);
        } catch (Exception e) {
            Log.e("BookAsyncTask", "Not able to create JSON Object !!!");
           responseAsync.Finish(title, authors);
        }

    }

    /**
     * Returns Json response in the form a String.
     *
     * @param url
     * @throws IOException
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("BookAsyncTask", "Response code : " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("BookAsyncTask", "IOException !");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Read the data from InputStream.
     *
     * @param inputStream
     * @throws IOException
     */
    private String readFromStream(InputStream inputStream) throws IOException {
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
     * Convert String url in URL Object and return.
     *
     * @param stringUrl
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("BookAsyncTask", "Error in converting url to URL object!!!");
            return url;
        }
        return url;
    }


}
