package br.com.bruno.moviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class MovieArrayAdapter extends ArrayAdapter<MovieMovie>
 {
        public MovieArrayAdapter(Context context, List<MovieMovie> movieList){
            super(context, -1, movieList);
        }

        private static class ViewHolder {
            TextView movie;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView,
                            @NonNull ViewGroup parent) {
            MovieMovie movie = getItem(position);
            View root = null;
            ViewHolder viewHolder = null;
            Context context = getContext();
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(context);
                root = inflater.inflate(R.layout.movie_item, parent, false);
                viewHolder = new ViewHolder();
                root.setTag(viewHolder);
                viewHolder.movie = root.findViewById(R.id.movieTextView);
            }
            else{
                root = convertView;
                viewHolder = (ViewHolder) root.getTag();
            }
            viewHolder.movie.setText(context.
                    getString(R.string.movie,
                            movie.title));

            return root;
        }
}
