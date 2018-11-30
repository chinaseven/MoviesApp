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

class MovieGenresArrayAdapter extends ArrayAdapter<MovieGenre>
 {
        public MovieGenresArrayAdapter (Context context, List<MovieGenre> movieGenreList){
            super(context, -1, movieGenreList);
        }


        private static class ViewHolder {
            TextView movieGenre;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView,
                            @NonNull ViewGroup parent) {
            MovieGenre movieGenre = getItem(position);
            View root = null;
            ViewHolder viewHolder = null;
            Context context = getContext();
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(context);
                root = inflater.inflate(R.layout.genre_item, parent, false);
                viewHolder = new ViewHolder();
                root.setTag(viewHolder);
                viewHolder.movieGenre = root.findViewById(R.id.genreTextView);
            }
            else{
                root = convertView;
                viewHolder = (ViewHolder) root.getTag();
            }
            viewHolder.movieGenre.setText(context.
                    getString(R.string.genre,
                            movieGenre.genre));

            return root;
        }
}
