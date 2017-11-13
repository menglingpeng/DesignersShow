package com.menglingpeng.designersshow.mvp.other;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.model.Shots;
import com.menglingpeng.designersshow.utils.Json;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.TimeUtil;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mengdroid on 2017/10/19.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private OnRecyclerListItemListener mListener;
    private ArrayList<Shots> shotses = new ArrayList<Shots>();
    private Fragment fragment;
    private boolean isLoading;
    onLoadingMore loadingMore;
    //加载更多的提前量
    private int visibleThreshold = 2;


    public RecyclerAdapter(RecyclerView recyclerView, Fragment fragment, String type, OnRecyclerListItemListener listener){
        this.fragment = fragment;
        mListener = listener;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int itemcount = layoutManager.getItemCount();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
                if(!isLoading && lastPosition >= (itemcount - visibleThreshold)){
                    if(loadingMore != null){
                        isLoading = true;
                        loadingMore.onLoadMore();
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return shotses.size() + 1;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder)holder;
            boolean isGif = shotses.get(position).isAnimated();
            int attachments = shotses.get(position).getAttachments_count();
            ImageLoader.loadCricleImage(fragment, shotses.get(position).getUser().getAvatar_url(), viewHolder.avatarIv);
            /*if(!isScrolling) {
                ImageLoader.loadCricleImage(fragment, shotses.get(position).getUser().getAvatar_url(), viewHolder.avatarIv);
            }else {
                viewHolder.avatarIv.setImageResource(R.drawable.ic_avatar);
            }*/
            viewHolder.shotsTitleTx.setText(shotses.get(position).getTitle());
            viewHolder.shots_userTx.setText(shotses.get(position).getUser().getUsername());
            viewHolder.shotsCreatedTimeTx.setText(TimeUtil.getTimeDifference(shotses.get(position).getUpdated_at()));
            ImageLoader.load(fragment, shotses.get(position).getImages().getNormal(), viewHolder.shotsIm);
            if(isGif){
                viewHolder.shotsGifIm.setVisibility(TextView.VISIBLE);
            }
            viewHolder.itemLikesCountTx.setText(String.valueOf(shotses.get(position).getLikes_count()));
            viewHolder.itemCommentsCountTx.setText(String.valueOf(shotses.get(position).getComments_count()));
            viewHolder.itemViewsCountTx.setText(String.valueOf(shotses.get(position).getViews_count()));
            if(attachments ==0){
                viewHolder.itemAttachmentsCountIm.setVisibility(ImageView.GONE);
                viewHolder.itemAttachmentsCountTx.setVisibility(TextView.GONE);
            }else {
                viewHolder.itemAttachmentsCountTx.setText(String.valueOf(attachments));
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onRecyclerFragmentListListener(viewHolder, shotses.get(position));
                    }
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView avatarIv;
        public final TextView shotsTitleTx, shots_userTx, shotsCreatedTimeTx;
        public final ImageView shotsIm, shotsGifIm, itemAttachmentsCountIm;
        public final TextView itemLikesCountTx, itemCommentsCountTx, itemViewsCountTx, itemAttachmentsCountTx;
        public ViewHolder(View view) {
            super(view);
            avatarIv = (ImageView) view.findViewById(R.id.avatar_im);
            shotsTitleTx = (TextView) view.findViewById(R.id.shots_title_tx);
            shots_userTx = (TextView) view.findViewById(R.id.shots_user_name_tx);
            shotsCreatedTimeTx = (TextView) view.findViewById(R.id.shots_create_time_tx);
            shotsIm = (ImageView) view.findViewById(R.id.shots_im);
            shotsGifIm = (ImageView) view.findViewById(R.id.shots_gif_im);
            itemLikesCountTx = (TextView) view.findViewById(R.id.item_likes_count_tx);
            itemCommentsCountTx = (TextView) view.findViewById(R.id.item_comments_count_tx);
            itemViewsCountTx = (TextView) view.findViewById(R.id.item_views_count_tx);
            itemAttachmentsCountIm = (ImageView)view.findViewById(R.id.item_attachments_count_im);
            itemAttachmentsCountTx = (TextView)view.findViewById(R.id.item_attachments_count_tx);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void addData(Shots data){
        shotses.add(data);
        notifyDataSetChanged();
    }

    public void setLoading(boolean isLoading){
        this.isLoading = isLoading;
    }

    public void setLoadingMore(onLoadingMore loadingMore){
        this.loadingMore = loadingMore;
    }

    public interface onLoadingMore{
        void onLoadMore();
    }

}
