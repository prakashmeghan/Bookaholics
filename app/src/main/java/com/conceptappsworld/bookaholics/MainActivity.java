package com.conceptappsworld.bookaholics;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.conceptappsworld.bookaholics.util.CommonUtil;
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
    private TextView tvEmpty, tvSearchResult;

    private BookAdapter bookAdapter;
    private ConnectionDetector connectionDetector;
    private static final int DEFAUL_MAX_RESULT = 10;
    private static final int LOADER_ID = 1;
    private LoaderManager loaderManager;
    private ArrayList<Book> alBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "TEST onCreate");

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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "TEST protected onSaveInstanceState");
        outState.putSerializable("d", alBooks);
        outState.putString("search_result", etSearch.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST onRestoreInstanceState");
        if (savedInstanceState != null) {
            if(alBooks != null){
                alBooks.clear();
            }
            String searchResult = savedInstanceState.getString("search_result");
            tvSearchResult.setText(getResources().getString(R.string.search_result) +
                    ": " +
                    searchResult);
            ArrayList<Book> alBooksRestored = (ArrayList<Book>) savedInstanceState.getSerializable("d");
            alBooks.addAll(alBooksRestored);
            bookAdapter.notifyDataSetChanged();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "TEST onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "TEST onResume");
    }

    private void setClickListeners() {
        lvBook.setOnItemClickListener(this);
        btSearch.setOnClickListener(this);
    }

    private void lvBookSetup() {
        Log.i(LOG_TAG, "TEST lvBookSetup");

        alBooks = new ArrayList<Book>();

        // Create a new {@link ArrayAdapter} of book
        bookAdapter = new BookAdapter(this, alBooks);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        lvBook.setAdapter(bookAdapter);
    }

    private void initLoader() {
        Log.i(LOG_TAG, "TEST initLoader");
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
        tvSearchResult = (TextView) findViewById(R.id.tv_search_result);
        loadingIndicator = (View) findViewById(R.id.loading_indicator);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST onCreateLoader");
        String finalUrl = ConstantUtil.BOOK_URL +
                ConstantUtil.QUERYSTR_PARAM1 +
                CommonUtil.replace(etSearch.getText().toString()) +
                ConstantUtil.QUERYSTR_PARAM2 +
                DEFAUL_MAX_RESULT;
        return new BookLoader(MainActivity.this, finalUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        Log.i(LOG_TAG, "TEST onLoadFinished");

        loadingIndicator.setVisibility(View.GONE);
        lvBook.setVisibility(View.VISIBLE);
        if(alBooks != null){
            alBooks.clear();
        }

        if (data != null && !data.isEmpty()) {
            if(data.size() > 0){
                alBooks.addAll(data);
                bookAdapter.notifyDataSetChanged();
            } else {
                tvEmpty.setText(getResources().getString(R.string.no_books));
            }

        }else {
            Log.i(LOG_TAG, "TEST data empty");
            tvEmpty.setText(getResources().getString(R.string.no_books));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.i(LOG_TAG, "TEST onLoaderReset");
        bookAdapter.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Book book = bookAdapter.getItem(i);

        if (book != null && book.getInfoLink() != null && !book.getInfoLink().equalsIgnoreCase("")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getInfoLink()));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_search:
                tvEmpty.setText("");
                lvBook.setVisibility(View.INVISIBLE);
                if (etSearch.getText().toString().isEmpty()) {
                    tvSearchResult.setText(getResources().getString(R.string.empty_validation));
                } else {
                    hideKeyboard();
                    tvSearchResult.setText(getResources().getString(R.string.search_result) +
                            ": " +
                            etSearch.getText().toString());

                    loadingIndicator.setVisibility(View.VISIBLE);

                    if (!connectionDetector.isConnectingToInternet()) {
                        //No Internet
                        loadingIndicator.setVisibility(View.GONE);
                        tvEmpty.setText(getResources().getString(R.string.no_internet));
                        return;
                    } else {
                        initLoader();
                    }
                }

                break;
            default:

                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
