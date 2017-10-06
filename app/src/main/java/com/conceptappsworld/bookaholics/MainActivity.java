package com.conceptappsworld.bookaholics;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.conceptappsworld.bookaholics.adapter.BookAdapter;
import com.conceptappsworld.bookaholics.loader.BookLoader;
import com.conceptappsworld.bookaholics.model.Book;
import com.conceptappsworld.bookaholics.util.ConnectionDetector;
import com.conceptappsworld.bookaholics.util.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>, AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String LOG_TAG = MainActivity.class.getName();

    private EditText etSearch;
    private Button btSearch;
    private ListView lvBook;
    private View loadingIndicator;
    private TextView tvEmpty;

    private BookAdapter bookAdapter;
    private ConnectionDetector connectionDetector;
    private static final int DEFAUL_MAX_RESULT = 10;
    private static final int LOADER_ID = 1;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewByIds();
        lvBookSetup();
        setClickListeners();

        // Get a reference to the LoaderManager, in order to interact with loaders.
        loaderManager = getSupportLoaderManager();

        connectionDetector = new ConnectionDetector(MainActivity.this);

        if (!connectionDetector.isConnectingToInternet()) {
            //No Internet
            loadingIndicator.setVisibility(View.GONE);
            tvEmpty.setText(getResources().getString(R.string.no_internet));
            etSearch.setVisibility(View.GONE);
            btSearch.setVisibility(View.GONE);
            return;
        } else {
            loadingIndicator.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
            btSearch.setVisibility(View.VISIBLE);
        }
    }

    private void setClickListeners() {
        lvBook.setOnItemClickListener(this);
        btSearch.setOnClickListener(this);
    }

    private void lvBookSetup() {
        lvBook.setEmptyView(tvEmpty);

        // Create a new {@link ArrayAdapter} of book
        bookAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        lvBook.setAdapter(bookAdapter);
    }

    private void initLoader() {
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
//        loaderManager.initLoader(LOADER_ID, null, this);

        loaderManager.restartLoader(LOADER_ID, null, this);
    }

    private void findViewByIds() {
        etSearch = (EditText) findViewById(R.id.et_searh);
        btSearch = (Button) findViewById(R.id.bt_search);
        lvBook = (ListView) findViewById(R.id.list);
        tvEmpty = (TextView) findViewById(R.id.empty_view);
        loadingIndicator = (View) findViewById(R.id.loading_indicator);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        String finalUrl = ConstantUtil.BOOK_URL +
                ConstantUtil.QUERYSTR_PARAM1 +
                etSearch.getText().toString() +
                ConstantUtil.QUERYSTR_PARAM2 +
                DEFAUL_MAX_RESULT;
        return new BookLoader(MainActivity.this, finalUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        Log.i(LOG_TAG, "onLoadFinished");

        loadingIndicator.setVisibility(View.GONE);
        tvEmpty.setText(R.string.no_books);
        // Clear the adapter of previous earthquake data
        bookAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            bookAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.i(LOG_TAG, "onLoaderReset");
        bookAdapter.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_search:

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                /*bookAdapter.clear();
                bookAdapter.notifyDataSetChanged();*/

                loadingIndicator.setVisibility(View.VISIBLE);

                if (!connectionDetector.isConnectingToInternet()) {
                    //No Internet
                    loadingIndicator.setVisibility(View.GONE);
                    tvEmpty.setText(getResources().getString(R.string.no_internet));
                    return;
                } else {
                    initLoader();
                }
                break;
            default:

                break;
        }
    }
}
