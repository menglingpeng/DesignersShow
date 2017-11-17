package com.menglingpeng.designersshow.mvp.interf;

import com.menglingpeng.designersshow.mvp.other.Data;

/**
 * Created by mengdroid on 2017/10/15.
 */

public interface RecyclerView<T extends Data> {
    void hideProgress();
    void loadFailed(String msg);
    void loadSuccess(String shotsJson, String requestType);
}
