package hcmute.edu.vn.foody.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.foody.FoodDetailsActivity;
import hcmute.edu.vn.foody.R;
import hcmute.edu.vn.foody.entity.Food;

public class FoodRecommendedAdapter extends RecyclerView.Adapter<FoodRecommendedAdapter.FoodViewHolder> {

    private Context context;
    private int layout;
    private List<Food> foodList;

    public FoodRecommendedAdapter(Context context, int layout, List<Food> foodList) {
        this.context = context;
        this.layout = layout;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodRecommendedAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new FoodRecommendedAdapter.FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRecommendedAdapter.FoodViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.foodName.setText(foodList.get(position).getName());
        holder.foodRating.setText("4.7");
        holder.foodPrice.setText(String.format("$%s", foodList.get(position).getPrice()));

        holder.foodImage.setImageResource(foodList.get(position).getImage());
        // Glide.with(context).load(foodList.get(position).getImageUrl()).into(holder.foodImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FoodDetailsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                i.putExtra("id", foodList.get(position).getId());
                i.putExtra("name", foodList.get(position).getName());
                i.putExtra("name_res", foodList.get(position).getRestaurant().getName());
                i.putExtra("address_res", foodList.get(position).getRestaurant().getAddress());
                i.putExtra("price", foodList.get(position).getPrice());
                i.putExtra("rating", "4.7");
                i.putExtra("image", foodList.get(position).getImage());

                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView foodName, foodRating, foodPrice;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodRating = itemView.findViewById(R.id.food_rating);
            foodPrice = itemView.findViewById(R.id.food_price);

        }
    }


}