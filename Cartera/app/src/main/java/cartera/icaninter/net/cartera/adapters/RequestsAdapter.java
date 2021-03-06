package cartera.icaninter.net.cartera.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cartera.icaninter.net.cartera.R;
import cartera.icaninter.net.cartera.TransactionActivity;
import cartera.icaninter.net.cartera.Utils;
import cartera.icaninter.net.cartera.models.Request;

/**
 * Created by Juzer on 2/27/2016.
 */
public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsViewHolder> {

    private ArrayList<Request> mRequests = new ArrayList<>();
    private Context mContext;
    private View emptyText;


    public RequestsAdapter(Context context, ArrayList<Request> requests, View emptyView){
        mRequests = requests;
        mContext = context;
        emptyText = emptyView;
    }


    @Override
    public RequestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_request_item, parent, false);

        return new RequestsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RequestsViewHolder holder, int position) {

        Request request = mRequests.get(position);
        holder.requestText.setText(Utils.formatRequestString(
                request.getRequesterName(),
                Integer.toString(request.getRequestAmount())));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(request.getRequesterName().substring(0,2),
                        ContextCompat.getColor(mContext, R.color.colorPrimary));

        holder.avatar.setImageDrawable(drawable);

        holder.timeText.setText(request.getDate());

    }

    public void swapData(ArrayList<Request> requests){
        mRequests = requests;
        emptyText.setVisibility(
                getItemCount() == 0
                        ? View.VISIBLE
                        : View.GONE
        );
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRequests == null ? 0 : mRequests.size();
    }

    public class RequestsViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.request)
        TextView requestText;

        @Bind(R.id.time_stamp)
        TextView timeText;

        @Bind(R.id.alpha_icon)
        ImageView avatar;

        public RequestsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Request r = mRequests.get(getAdapterPosition());
                    Intent intent = new Intent(mContext, TransactionActivity.class);
                    intent.putExtra(TransactionActivity.EXTRA_AMOUNT, r.getRequestAmount());
                    intent.putExtra(TransactionActivity.EXTRA_NAME, r.getRequesterName());
                    intent.putExtra(TransactionActivity.EXTRA_LAT, r.getLat());
                    intent.putExtra(TransactionActivity.EXTRA_LONG, r.getLon());
                    intent.putExtra(TransactionActivity.EXTRA_REQUESTER, r.getUserId());
                    intent.putExtra(TransactionActivity.EXTRA_REQUEST, r.getRequestId());
                    mContext.startActivity(intent);

                }
            });
        }
    }

}
