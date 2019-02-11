package mu.astek.database.khadundentalcare;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;


/**
 * Created by shaulkory on 8/18/2016.
 */
public class ImageAdapter extends RecyclerView.Adapter {

    final private List<Uri> list;

    final private Context context;
    boolean viewOnly = false;

    public ImageAdapter(final List<Uri> imageUrIList, final Context context,Boolean view) {

        this.list = imageUrIList; // list
        this.context = context;
        this.viewOnly = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {

        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_imagefood, viewGroup, false);

        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ImageViewHolder view = (ImageViewHolder) holder;
        final Uri image = list.get(position);
        view.img_foto.setVisibility(View.VISIBLE);

        Glide.with(context).load(image)
                .into(view.img_foto);
        if(viewOnly){
            view.imgDelete.setVisibility(View.GONE);
        }

        view.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        view.img_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri pdfPath = list.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(pdfPath, "application/pdf");

                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //if user doesn't have pdf reader instructing to download a pdf reader
                }
            }

        });


       // Picasso.with(context).load(image).into(view.img_foto);
    }

    public List<Uri> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View v) {
            super(v);
        }
    }

    private class ImageViewHolder extends ViewHolder {

        ImageView img_foto,imgDelete;
        TextView txt_foodlabel;

        ImageViewHolder(final View v) {
            super(v);
            this.img_foto = v.findViewById(R.id.img_foto);
            this.imgDelete = v.findViewById(R.id.imgDelete);
            this.txt_foodlabel = v.findViewById(R.id.txt_foodlabel);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public final static File getOutputMediaDirectoryProfilePic() {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".Dentist");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return mediaStorageDir;
    }
}
