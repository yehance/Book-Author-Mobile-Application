package com.yehance.whowroteit;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.jar.JarException;

public class FetchBook extends AsyncTask<String, Void, String> {

    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;

    //creating constructor for class FetchBook and it has two inputs, titleText and authorText
    public FetchBook(TextView titleText, TextView authorText){
         this.mTitleText = new WeakReference<>(titleText);
         this.mAuthorText= new WeakReference<>(authorText);
    }

    @Override
    protected String doInBackground(String... strings) {
        /*after writing the code in in NetworkUtils.java file we are returning it's
        value here, input is set as string[0] because we only give one string and there are
        several strings there*/
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try{
            //this code extracts title and author part from the jason output we get
            JSONObject jasonObject = new JSONObject(s);
            //put things inside 'items' in jason output to an array
            JSONArray itemsArray = jasonObject.getJSONArray("items");

            int i=0;
            String title=null;
            String authors=null;

            //iterating inside items array
            while(i<itemsArray.length() && (authors==null && title==null)){
                JSONObject book = itemsArray.getJSONObject(i);
                //another object is created because author name and title
                // is inside volumeInfo in JASON output
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                try { //try catch because author is not available for every book
                    //then getting the data we want
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch(Exception e){
                    e.printStackTrace();
                }

                i++;

            }

            if(title != null && authors !=null){
                //then setting the values to UI
                mTitleText.get().setText(title);
                mAuthorText.get().setText(authors);
                /*get is used because these are weak references*/
            }else{
                mTitleText.get().setText("No results");
                mAuthorText.get().setText(" ");
            }
        }
        catch (JSONException e){
            //in catch also if we can say no results
            mTitleText.get().setText("No results");
            mAuthorText.get().setText(" ");
           e.printStackTrace();
        }

    }
}
