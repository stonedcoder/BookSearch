package com.example.vraun.booksearch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vraun on 02-02-2017.
 */

public class Book implements Parcelable {

    private String title ;
    private String author;

    /**create a constructor with
     * @param booktitle
     * @param bookauthor
     */

    public Book (String booktitle , String bookauthor){
        title = booktitle ;
        author = bookauthor ;
    }

    //returns title of the book
    public String getTitle(){return title;}


    //returns author of the book
    public String getAuthor(){return author;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
