package map.android.mr_auspicious.bukarteadmin.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import map.android.mr_auspicious.bukarteadmin.Model.Order;
import map.android.mr_auspicious.bukarteadmin.R;

public class Orders_Adapter extends RecyclerView.Adapter<Orders_Adapter.MyViewHolder> {

    private List<Order> orders;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView bookImage;
        TextView bookName;
        TextView bookPrice;
        TextView qty;
        TextView order_date;


        public MyViewHolder(View view) {
            super(view);

            bookImage = (ImageView) view.findViewById(R.id.iv);
            bookName = (TextView) view.findViewById(R.id.t1);
            bookPrice = (TextView) view.findViewById(R.id.t2);
            qty = (TextView) view.findViewById(R.id.quantity);
            order_date = (TextView) view.findViewById(R.id.t4);
        }

    }


    public Orders_Adapter(Context context, List<Order> orders) {
        mContext = context;
        this.orders = orders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list_design, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Order order = orders.get(position);
        Picasso.with(mContext).load(order.getImageurl()).placeholder(R.drawable.phimg).into(holder.bookImage);
        holder.bookName.setText(order.getBookname());
        holder.bookPrice.setText("₹"+ order.getBookPrice());
        holder.order_date.setText("Product ID - "+order.getId());
        holder.qty.setText("QTY - "+order.getQty());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }



        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}