package com.menglingpeng.designersshow.mvp.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.net.Json;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.SharedPreUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mengdroid on 2017/10/19.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private OnRecyclerListItemListener mListener;
    private ArrayList<Shots> shotses;


    public RecyclerAdapter(OnRecyclerListItemListener listener){
        this.mListener = listener;
        shotses = Json.parseShots(SharedPreUtil.getShotsJson());
    }

    @Override
    public int getItemViewType(int position) {
        if(position == shotses.size()){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType == TYPE_FOOTER){
            view = inflater.inflate(R.layout.recycler_item_footer_loading, parent, false);
            return new FooterViewHolder(view);
        }else {
            view = inflater.inflate(R.layout.recycler_item, parent, false);
            return  new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Context context = holder.itemView.getContext();
        if(holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder)holder;
            ImageLoader.load(context, shotses.get(position).getUser().getAvatar_url(), viewHolder.avatar);
            viewHolder.shotsTitleTx.setText(shotses.get(position).getTitle());
            viewHolder.shots_userTx.setText(shotses.get(position).getUser().getUsername());
            viewHolder.shotsCreatedTimeTx.setText(shotses.get(position).getUpdated_at());
            ImageLoader.load(context, shotses.get(position).getImages().getNormal(), viewHolder.shotsIm);
            if(shotses.get(position).isAnimated()){
                viewHolder.shotsGifTx.setVisibility(TextView.VISIBLE);
            }
            viewHolder.itemLikesCountTx.setText(String.valueOf(shotses.get(position).getLikes_count()));
            viewHolder.itemCommentsCountTx.setText(String.valueOf(shotses.get(position).getComments_count()));
            viewHolder.itemCommentsCountTx.setText(String.valueOf(shotses.get(position).getViews_count()));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onRecyclerFragmentListListener(viewHolder);
                    }
                }
            });
        }

    }

    public void addShots(Shots shots){
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return shotses.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final CircleImageView avatar;
        public final TextView shotsTitleTx, shots_userTx, shotsCreatedTimeTx;
        public final ImageView shotsIm;
        public final TextView shotsGifTx;
        public final TextView itemLikesCountTx, itemCommentsCountTx, itemViewsCountTx;

        public ViewHolder(View view) {
            super(view);
            avatar = (CircleImageView) view.findViewById(R.id.avatar);
            shotsTitleTx = (TextView) view.findViewById(R.id.shots_title);
            shots_userTx = (TextView) view.findViewById(R.id.shots_user);
            shotsCreatedTimeTx = (TextView) view.findViewById(R.id.shots_create_time);
            shotsIm = (ImageView) view.findViewById(R.id.shots_im);
            shotsGifTx = (TextView)view.findViewById(R.id.shots_gif_tx);
            itemLikesCountTx = (TextView) view.findViewById(R.id.item_likes_count_tx);
            itemCommentsCountTx = (TextView) view.findViewById(R.id.item_comments_count_tx);
            itemViewsCountTx = (TextView) view.findViewById(R.id.item_view_count_tx);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

}
