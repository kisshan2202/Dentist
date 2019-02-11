package mu.astek.database.khadundentalcare;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by shaulkory on 8/18/2016.
 */
public class PdfAdapter extends RecyclerView.Adapter {

    final private List<Uri> list;

    final private Context context;

    public PdfAdapter(final List<Uri> imageUrIList, final Context context) {

        this.list = imageUrIList; // list
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {

        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewholder_pdf, viewGroup, false);

        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ImageViewHolder view = (ImageViewHolder) holder;
        view.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });


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
            this.txt_foodlabel = v.findViewById(R.id.txt_foodlabel);
            this.imgDelete = v.findViewById(R.id.imgDelete);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
