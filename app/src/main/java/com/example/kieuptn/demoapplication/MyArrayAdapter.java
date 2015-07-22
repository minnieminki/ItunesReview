package com.example.kieuptn.demoapplication;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by KieuPTN on 7/9/2015.
 */
public class MyArrayAdapter extends ArrayAdapter<ObjectItem> {

    static final String TABLE_FAVORIES = "Favories";
    static final String TABLE_PLAYLIST = "Playlist";

    ViewHolder viewHolder;
    Context context;
    int resource;
    ArrayList<ObjectItem> objectItem;

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public ObjectItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getPosition(ObjectItem item) {
        return super.getPosition(item);
    }

    public MyArrayAdapter(Context context, int resource, ArrayList<ObjectItem> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objectItem = objects;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ObjectItem aObjectItem = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (resource == R.layout.custom_grid_item) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_grid_item, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent, false);
                viewHolder.imageButton = (ImageButton)convertView.findViewById(R.id.menuImageButton);
                viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenuInView(view, (int)view.getTag());
                    }
                });
            }

            viewHolder.cellImageView = (ImageView)convertView.findViewById(R.id.cellImageView);
            viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.titleTextView);
            viewHolder.artistTextView = (TextView)convertView.findViewById(R.id.artistTextView);
            viewHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // load image from image URL
        LoadImage loadImage = new LoadImage(viewHolder.cellImageView, viewHolder.progressBar);
        loadImage.execute(aObjectItem.getImageURLString());

        viewHolder.titleTextView.setText(aObjectItem.getSong());
        viewHolder.artistTextView.setText(aObjectItem.getArtist());

        if (viewHolder.imageButton != null) {
            viewHolder.imageButton.setTag(position);
        }

        return convertView;
    }

    // class ViewHolder
    public static class ViewHolder {
        ImageView cellImageView;
        TextView titleTextView;
        TextView artistTextView;
        ProgressBar progressBar;
        ImageButton imageButton;
    }

    // show popup menu
    public void showPopupMenuInView (View view, int positionItem) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.custom_popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Database database = new Database(context);
                String tableName, playlistName;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                switch (menuItem.getItemId()) {
                    case R.id.menu_add_favories:
                        tableName = TABLE_FAVORIES;
                        return true;
                    case R.id.menu_add_playlist:
                        tableName = TABLE_PLAYLIST;

                        LayoutInflater factory = LayoutInflater.from(context);
                        final View customDialogView = factory.inflate(R.layout.custom_alert_dialog, null);
                        builder.setView(customDialogView);

                        builder.show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

}
