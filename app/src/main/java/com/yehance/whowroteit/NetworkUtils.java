package com.yehance.whowroteit;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    /*using this log tag we can identify when something is coming from this class*/

    //Base URL for Books API
    private static final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";

    //parameter for the search string
    private static final String QUERY_PARAM = "q";

    //parameter that limits search results
    private static final String MAX_RESULTS = "maxResults";

    //parameter to filter by print type
    private static final String PRINT_TYPE= "printType";

    static String getBookInfo(String queryString){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;


        try{
            /*using build upon we are saying that use this base url for the front
            page and add these parameters*/

            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM,queryString)
                    .appendQueryParameter(MAX_RESULTS,"10")
                    .appendQueryParameter(PRINT_TYPE,"books")
                    .build();
            /*in above we are saying the API to filter what we want*/

            URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /*In above we are creating a URL and inside the URL we are setting the request
            method. and then connecting*/

            //get the InputStream
            /*After connecting we are getting the input stream(stream from which we can get the
            output*/
            InputStream inputStream = urlConnection.getInputStream();

            //create a buffered reader from that input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //Use a StringBuilder to hold the incoming response
            StringBuilder builder = new StringBuilder();

            /*In above what we are doing is when connecting to url connection
            we get a input stream. For that input stream we are connecting the
            reader declared above and create a buffered reader. And using the
            string builder we are going to get the feedback. it is implemented
            below.  */
            //from the buffered reader we are going to read line by line and add it to a string
            String line;
            while ((line= reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }

            /*String builder is like a string. In above code we read 'lines of reader'
            line by line and append it to the string builder with new line character.*/

            //at the end if builder length is zero we can say we did not get any feedback
            if(builder.length()==0){
                //stream was empty. No point in parsing
                return null;
            }

            //otherwise we can change it to a string
            bookJSONString = builder.toString();

        }

        catch(IOException e){
                   e.printStackTrace(); //so error is printed on the screen
        }

        finally{  //in this block we are going to close the connection
            if(urlConnection != null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }

        Log.d(LOG_TAG, bookJSONString); //printing the value on log
        return bookJSONString; //finally we are returning this

    }

}
