package com.menglingpeng.designersshow.mvp.other;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnRecyclerListItemListener;
import com.menglingpeng.designersshow.mvp.model.OpenResource;
import com.menglingpeng.designersshow.utils.ShareAndOpenInBrowserUtil;

import java.util.ArrayList;

/**
 * Created by mengdroid on 2017/12/24.
 */

public class AboutRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<OpenResource> list;
    public AboutRecyclerAdapter (Context context){
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.open_source_licenses_recycler_item, null);
        RecyclerView.ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;
        final OpenResource openResource = list.get(position);
        viewHolder.nameTv.setText(openResource.getName());
        viewHolder.authorNameTv.setText(openResource.getAuthorName());
        viewHolder.licenseTv.setText(openResource.getLicense());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareAndOpenInBrowserUtil.openInBrowser(context, openResource.getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final CardView cardView;
        private final TextView nameTv;
        private final TextView authorNameTv;
        private final TextView licenseTv;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView)view.findViewById(R.id.about_open_source_licenses_cv);
            nameTv = (TextView)view.findViewById(R.id.about_open_source_name_tv);
            authorNameTv = (TextView)view.findViewById(R.id.about_open_source_author_tv);
            licenseTv = (TextView)view.findViewById(R.id.about_open_source_licenses_name_tv);
        }
    }

    public void addData(OpenResource openResource){
        list.add(openResource);
        notifyDataSetChanged();
    }
}
