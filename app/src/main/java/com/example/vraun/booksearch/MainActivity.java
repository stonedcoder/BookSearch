package com.example.vraun.booksearch;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import static com.example.vraun.booksearch.R.id.button;

public class MainActivity extends AppCompatActivity {

    //Initialise all the used variables and list views .
    EditText editText;
    Button search;
    ListView listView;
    ArrayList<Book> books;
    BookAdapter adapter;
    BookAsyncTask bookAsyncTask;
    TextView textview ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.text_edit);
        search = (Button) findViewById(button);
        listView = (ListView) findViewById(R.id.list);
        books = new ArrayList<>();
        adapter = new BookAdapter(this, books);
        textview = (TextView) findViewById(R.id.empty_list_view);


        //Setting on click listener to that button .
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Url();
                if (url != "") {
                    bookAsyncTask = new BookAsyncTask(new AsyncResponseInterface() {
                        @Override
                        public void Finish(ArrayList<String> title, ArrayList<String> author) {
                            clearList();
                            if (title.size() != 0) {
                                textview.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < title.size(); i++) {
                                    add(title.get(i), author.get(i));
                                }
                            } else {
                                listView.setVisibility(View.GONE);
                                textview.setVisibility(View.VISIBLE);
                            }
                            listView.setAdapter(adapter);
                        }
                    });
                    bookAsyncTask.execute(url);
                }
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Clear books ArrayList.
     */
    private void clearList() {
        books.clear();
    }

    /**
     * Add title and author to the books ArrayList.
     *
     * @param title
     * @param author
     */
    private void add(String title, String author) {
        books.add(new Book(title, author));
    }

    //URL method.

    private String Url() {
        String keyword = "";
        if (!editText.getText().toString().equals("")) {
            keyword = editText.getText().toString();
            Toast.makeText(MainActivity.this, "Search in process ..", Toast.LENGTH_SHORT).show();
            return "https://www.googleapis.com/books/v1/volumes?" + "q=" + keyword;
        } else {
            Toast.makeText(MainActivity.this, "No Results", Toast.LENGTH_SHORT).show();
        }
        return keyword;
    }




    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

