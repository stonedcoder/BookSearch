package com.example.vraun.booksearch;

/**
 * Created by vraun on 02-02-2017.
 */

public class Book {

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
}
