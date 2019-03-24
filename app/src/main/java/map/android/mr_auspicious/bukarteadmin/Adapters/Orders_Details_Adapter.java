package map.android.mr_auspicious.bukarteadmin.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import map.android.mr_auspicious.bukarteadmin.Model.Order;
import map.android.mr_auspicious.bukarteadmin.R;

public class Orders_Details_Adapter extends RecyclerView.Adapter<Orders_Details_Adapter.MyViewHolder> {

    private List<Order> orders;
    private Context mContext;
    private Cancel_Listener cancel_listener;
    private TextView cancel_text;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView bookImage;
        TextView bookName;
        TextView bookPrice;
        TextView qty;
        TextView order_date;
        RelativeLayout cancel_layout;
        RelativeLayout track_layout;
        ImageView img;

        public MyViewHolder(View view) {
            super(view);

            bookImage = (ImageView) view.findViewById(R.id.iv);
            bookName = (TextView) view.findViewById(R.id.t1);
            bookPrice = (TextView) view.findViewById(R.id.t2);
            qty = (TextView) view.findViewById(R.id.quantity);
            order_date = (TextView) view.findViewById(R.id.t4);
            img = (ImageView) view.findViewById(R.id.cancel_click);
            track_layout = (RelativeLayout) view.findViewById(R.id.track_layout);
            cancel_text = (TextView) view.findViewById(R.id.cancel_text);
            cancel_layout = (RelativeLayout) view.findViewById(R.id.cancel_layout);
        }
    }

    public interface Cancel_Listener{
        void cancel_click(View view, int position, String can_type);

        void track_Click(View view, int position, int id);
    }

    public Orders_Details_Adapter(Context context, List<Order> orders, Cancel_Listener cancel_listener) {
        mContext = context;
        this.orders = orders;
        this.cancel_listener = cancel_listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list_design_1, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Order order = orders.get(position);

        Picasso.with(mContext).load(order.getImageurl()).placeholder(R.drawable.phimg).into(holder.bookImage);
        holder.bookName.setText(order.getBookname());
        holder.bookPrice.setText("₹"+ order.getBookPrice());
        holder.order_date.setText("Ordered ID - "+order.getId());
        holder.qty.setText("QTY - "+order.getQty());

        holder.track_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_listener.track_Click(view,position,1);
            }
        });

        cancel_text.setTag(position);

        holder.cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_listener.cancel_click(view,position,"c");
            }
        });



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
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }



        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
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