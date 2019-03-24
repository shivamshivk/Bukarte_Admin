package map.android.mr_auspicious.bukarteadmin.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;
import map.android.mr_auspicious.bukarteadmin.Model.Request_;
import map.android.mr_auspicious.bukarteadmin.R;

public class Request_Adapter extends RecyclerView.Adapter<Request_Adapter.MyViewHolder> {

    private List<Request_> requests;
    private Context mContext;
    private int selected_position = -1;
    private ClickListener clickListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView b_name;
        TextView author_;
        TextView publisher;
        TextView req_time;
        TextView c_id;
        Button share_btn;

        public MyViewHolder(View view) {
            super(view);

            b_name = (TextView) view.findViewById(R.id.b_name);
            author_ = (TextView) view.findViewById(R.id.author_name);
            publisher = (TextView) view.findViewById(R.id.publisher);
            req_time = (TextView) view.findViewById(R.id.request_time);
            c_id = (TextView) view.findViewById(R.id.c_id);
            share_btn = (Button) view.findViewById(R.id.share_btn);
            share_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onShareClick(view,getAdapterPosition());
                }
            });
        }

    }


    public Request_Adapter(Context context,List<Request_> requests,ClickListener clickListener) {
        this.mContext = context;
        this.requests = requests;
        this.clickListener = clickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request__book_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Request_ request = requests.get(position);
        holder.b_name.setText(request.getB_name());
        holder.author_.setText(request.getA_name());
        holder.publisher.setText(request.getP_name());
        holder.req_time.setText(request.getRequest_time());
        holder.c_id.setText(request.getC_id());

    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selected_position = position;
                } else {
                    selected_position = -1;
                }
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return requests.size();
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

        void onShareClick(View view,int position);
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
        public void onTouchEvent(RecyclerView rv, MotionEvent e){

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}