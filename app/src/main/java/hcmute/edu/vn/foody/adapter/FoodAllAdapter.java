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

import hcmute.edu.vn.foody.FoodDetailsActivity;
import hcmute.edu.vn.foody.R;
import hcmute.edu.vn.foody.entity.Food;

public class FoodAllAdapter extends RecyclerView.Adapter<FoodAllAdapter.FoodViewHolder> {

    private Context context;
    private int layout;
    private List<Food> foodList;

    public FoodAllAdapter(Context context, int layout, List<Food> foodList) {
        this.context = context;
        this.layout = layout;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodAllAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new FoodAllAdapter.FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAllAdapter.FoodViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.foodName.setText(foodList.get(position).getName());
        holder.foodRes.setText(foodList.get(position).getRestaurant().getName());
        holder.foodRating.setText("4.7");
        holder.foodCharges.setText("DeliveryCharges");
        holder.foodDeliveryTime.setText("45 mins");
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
                i.putExtra("image_res", foodList.get(position).getRestaurant().getImage());

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
        TextView foodName, foodRes, foodRating, foodDeliveryTime, foodCharges, foodPrice;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodRes = itemView.findViewById(R.id.food_res);
            foodRating = itemView.findViewById(R.id.food_rating);
            foodDeliveryTime = itemView.findViewById(R.id.food_deliverytime);
            foodCharges = itemView.findViewById(R.id.food_delivery_charge);
            foodPrice = itemView.findViewById(R.id.food_price);

        }
    }


}