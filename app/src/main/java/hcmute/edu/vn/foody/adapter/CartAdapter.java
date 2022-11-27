package hcmute.edu.vn.foody.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hcmute.edu.vn.foody.CartActivity;
import hcmute.edu.vn.foody.R;
import hcmute.edu.vn.foody.entity.OrderFood;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<OrderFood> cartList;

    public CartAdapter(Context context, int layout, ArrayList<OrderFood> cartList) {
        this.context = context;
        this.layout = layout;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class ViewHolder {
        ImageView imageView, imageViewIncrease, imageViewDecrease, imageViewRemove;
        TextView textViewName, textViewPrice, textViewQuantity;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            viewHolder.imageView = view.findViewById(R.id.food_image);
            viewHolder.textViewName = view.findViewById(R.id.textView_name);
            viewHolder.textViewPrice = view.findViewById(R.id.textView_price);
            viewHolder.textViewQuantity = view.findViewById(R.id.textView_quantity);
            viewHolder.imageViewIncrease = view.findViewById(R.id.imageView_increase);
            viewHolder.imageViewDecrease = view.findViewById(R.id.imageView_decrease);
            viewHolder.imageViewRemove = view.findViewById(R.id.imageView_remove);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();

        OrderFood cart = cartList.get(i);
        if (cart != null) {
            viewHolder.textViewName.setText(cart.getFood().getName());
            viewHolder.textViewPrice.setText(String.format("$%d", cart.getFood().getPrice()));
            viewHolder.textViewQuantity.setText(String.format("%02d", cart.getQuantity()));

            viewHolder.imageView.setImageResource(cart.getFood().getImage());
        }
        //Glide.with(context).load(cart.getFood().getImageUrl()).into(viewHolder.imageView);

        viewHolder.imageViewIncrease.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                if (context instanceof CartActivity) {
                    assert cart != null;
                    ((CartActivity) context).updateCartItem(cart.getId(), cart.getQuantity() + 1);
                    ((CartActivity) context).updateTotal(cart.getFood().getPrice());
                }
                viewHolder.textViewQuantity.setText(String.format("%02d", Integer.parseInt(viewHolder.textViewQuantity.getText().toString()) + 1));
            }
        });

        viewHolder.imageViewDecrease.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {
                if (!viewHolder.textViewQuantity.getText().equals("01")) {
                    if (context instanceof CartActivity) {
                        assert cart != null;
                        ((CartActivity) context).updateCartItem(cart.getId(), cart.getQuantity() - 1);
                        ((CartActivity) context).updateTotal(cart.getFood().getPrice() * (-1));
                    }
                    viewHolder.textViewQuantity.setText(String.format("%02d", Integer.parseInt(viewHolder.textViewQuantity.getText().toString()) - 1));
                }
            }
        });

        viewHolder.imageViewRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Alert");
                builder.setMessage("Do you want to delete this item?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cartList.remove(i);
                        notifyDataSetChanged();
                        if (context instanceof CartActivity) {
                            assert cart != null;
                            ((CartActivity) context).deleteCartItem(cart.getId());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        return view;
    }
}
