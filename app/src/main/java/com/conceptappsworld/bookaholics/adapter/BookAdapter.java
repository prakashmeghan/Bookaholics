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


public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> alBook) {
        super(context, 0, alBook);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Book book = getItem(position);
        TextView tvTitle = (TextView) listItemView.findViewById(R.id.tv_title);
        TextView tvDescription = (TextView) listItemView.findViewById(R.id.tv_desc);
        TextView tvPublisher = (TextView) listItemView.findViewById(R.id.tv_publisher);
        TextView tvAuthors = (TextView) listItemView.findViewById(R.id.tv_authors);

        if (book.getTitle() != null && !book.getTitle().equalsIgnoreCase(""))
            tvTitle.setText(book.getTitle());

        if (book.getDescription() != null && !book.getDescription().equalsIgnoreCase(""))
            tvDescription.setText(book.getDescription());

        if (book.getPublisher() != null && !book.getPublisher().equalsIgnoreCase(""))
            tvPublisher.setText(book.getPublisher());


        if(book.getAuthors() != null && book.getAuthors().size() > 0){
            StringBuilder sbAuthors = new StringBuilder();
            for (String strAuthor: book.getAuthors()){
                sbAuthors.append(strAuthor + ", ");
            }
            String strAuthors = sbAuthors.toString();

            tvAuthors.setText(strAuthors.trim().substring(0, strAuthors.length() - 2));
        }

        return listItemView;
    }
}
