package com.conceptappsworld.bookaholics.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.conceptappsworld.bookaholics.R;
import com.conceptappsworld.bookaholics.model.Book;

import java.util.ArrayList;

/**
 * Created by Sprim on 06-10-2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> alBook) {
        super(context, 0, alBook);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book book = getItem(position);
        TextView tvTitle = (TextView) listItemView.findViewById(R.id.tv_title);
        TextView tvDescription = (TextView) listItemView.findViewById(R.id.tv_desc);
        TextView tvPublisher = (TextView) listItemView.findViewById(R.id.tv_publisher);

        tvTitle.setText(book.getTitle());
        tvDescription.setText(book.getDescription());
        tvPublisher.setText(book.getPublisher());

        return listItemView;
    }
}
