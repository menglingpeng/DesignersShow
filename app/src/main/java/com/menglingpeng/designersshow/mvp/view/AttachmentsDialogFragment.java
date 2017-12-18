package com.menglingpeng.designersshow.mvp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.menglingpeng.designersshow.R;
import com.menglingpeng.designersshow.mvp.interf.OnloadDetailImageListener;
import com.menglingpeng.designersshow.mvp.interf.RecyclerView;
import com.menglingpeng.designersshow.mvp.model.Attachment;
import com.menglingpeng.designersshow.mvp.other.TabPagerFragmentAdapter;
import com.menglingpeng.designersshow.mvp.presenter.RecyclerPresenter;
import com.menglingpeng.designersshow.utils.Constants;
import com.menglingpeng.designersshow.utils.ImageLoader;
import com.menglingpeng.designersshow.utils.Json;
import com.menglingpeng.designersshow.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mengdroid on 2017/12/17.
 */

public class AttachmentsDialogFragment extends AppCompatDialogFragment implements RecyclerView, View.OnClickListener{

    private Dialog dialog;
    private Context context;
    private String shotsId;
    private String type;
    private ImageView closeIv;
    private ImageView shareIv;
    private ImageView openInBrowserIv;
    private ImageView downloadIv;
    private ProgressBar attachmentsDialogPb;
    private ImageView attachmentsDialogIv;
    private TabLayout attachmentsDialogTl;
    private ViewPager attachmentsDialogVp;
    private ArrayList<Attachment> attachments;
    private ArrayList<String> titles;
    private ArrayList<RecyclerFragment> fragments;
    private ArrayList<String> urls;
    private TabPagerFragmentAdapter adapter;
    private String attachmentUrl;
    private String currentItemType;
    private int currentItemId;

    public static AttachmentsDialogFragment newInstance(String shotsId){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SHOT_ID, shotsId);
        AttachmentsDialogFragment dialogFragment = new AttachmentsDialogFragment();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getContext();
        shotsId = getArguments().getString(Constants.SHOT_ID);
        dialog = new Dialog(context, R.style.ThemeLoginDialog);
        attachments = new ArrayList<>();
        titles = new ArrayList<>();
        fragments = new ArrayList<>();
        urls = new ArrayList<>();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_fragment_attachments, null);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.LoginDialog);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.getDecorView().setPadding(0, 0, 0, 0);
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        dialog.setContentView(dialogView);
        closeIv = (ImageView)dialogView.findViewById(R.id.attachments_dialog_close_iv);
        shareIv = (ImageView)dialogView.findViewById(R.id.attachments_dialog_share_iv);
        attachmentsDialogPb = (ProgressBar)dialogView.findViewById(R.id.attachments_dialog_pb);
        attachmentsDialogIv = (ImageView)dialogView.findViewById(R.id.attachments_dialog_iv);
        attachmentsDialogTl = (TabLayout)dialogView.findViewById(R.id.attachments_dialog_tl);
        attachmentsDialogVp = (ViewPager)dialogView.findViewById(R.id.attachments_dialog_vp);
        dialog.show();
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.ID, shotsId);
        map.put(Constants.ACCESS_TOKEN, SharedPrefUtil.getAuthToken());
        type = Constants.REQUEST_LIST_ATTACHMENTS_FOR_A_SHOT;
        RecyclerPresenter presenter = new RecyclerPresenter(this, type,
                                Constants.REQUEST_NORMAL, Constants.REQUEST_GET_MEIHOD, map, context);
        presenter.loadJson();
        return dialog;
    }

    @Override
    public void hideProgress() {
        attachmentsDialogPb.setVisibility(ProgressBar.GONE);
    }

    @Override
    public void loadFailed(String msg) {

    }

    @Override
    public void loadSuccess(String json, String requestType) {
        attachments = Json.parseArrayJson(json, Attachment.class);
        if(attachments.size() == 1) {
            attachmentUrl = attachments.get(0).getUrl();
            ImageLoader.load(context, attachments.get(0).getUrl(), attachmentsDialogIv, true,
                    false);
        }else {
            attachmentsDialogIv.setVisibility(ImageView.GONE);
            initTabPager();
        }
    }

    private void initTabPager(){
        attachmentsDialogTl.setVisibility(TabLayout.VISIBLE);
        attachmentsDialogVp.setVisibility(ViewPager.VISIBLE);
        adapter = new TabPagerFragmentAdapter(getFragmentManager());
        initData();
        attachmentsDialogVp.setAdapter(adapter);
        attachmentsDialogTl.setupWithViewPager(attachmentsDialogVp);


    }

    private void initData(){
        for (int i = 0 ; i < attachments.size() ; i++){
            urls.add(attachments.get(i).getThumbnail_url());
            titles.add(String.valueOf(i+1));
            fragments.add(RecyclerFragment.newInstance(attachments.get(i).getThumbnail_url(), new StringBuilder().
                    append(Constants.REQUEST_LIST_ATTACHMENTS_FOR_A_SHOT).append(String.valueOf(i+1)).toString()));
        }
        adapter.setFragments(fragments, titles);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.attachments_dialog_close_iv:
                dialog.dismiss();
                break;
            case R.id.attachments_dialog_share_iv:
                break;
            default:
                break;
        }
    }

    private void shareAttachment(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        String type;
        currentItemId = attachmentsDialogVp.getCurrentItem();
        currentItemType = attachments.get(currentItemId).getContent_type();
        attachmentUrl = urls.get(currentItemId);
        if(currentItemType.equals(Constants.IMAGE_PNG)){
            type = Constants.IMAGE_PNG;
        }else {
            type = Constants.IMAGE_JPEG;
        }
        intent.setType("text/plain");
        String text = new StringBuilder().append(String.valueOf(currentItemId)).append(type).append(".").
                append(attachmentUrl).append("\n")
                .append("--").append(getResources().getString(R.string.detail_toolbar_overflow_menu_share_footer_text))
                .toString();
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string
                .detail_toolbar_overflow_menu_share_create_chooser_title)));
    }
}
