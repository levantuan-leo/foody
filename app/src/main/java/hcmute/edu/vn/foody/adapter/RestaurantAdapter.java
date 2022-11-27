package hcmute.edu.vn.foody.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.foody.R;
import hcmute.edu.vn.foody.RestaurantActivity;
import hcmute.edu.vn.foody.entity.Restaurant;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    private Context context;
    private int layout;
    private List<Restaurant> resList;

    public RestaurantAdapter(Context context, int layout, List<Restaurant> resList) {
        this.context = context;
        this.layout = layout;
        this.resList = resList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new RestaurantAdapter.RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.resImage.setImageResource(resList.get(position).getImage());
        holder.resName.setText(resList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RestaurantActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                i.putExtra("id", resList.get(position).getId());
                i.putExtra("name", resList.get(position).getName());
                i.putExtra("address", resList.get(position).getAddress());
                i.putExtra("image", resList.get(position).getImage());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resList.size();
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        ImageView resImage;
        TextView resName;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);

            resImage = itemView.findViewById(R.id.restaurant_image);
            resName = itemView.findViewById(R.id.restaurant_name);
        }
    }
}
