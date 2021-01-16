package com.mark.cyberpunkplayer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.cyberpunkplayer.player.DMarkVideoView;
import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.base.BaseFragment;
import com.mark.cyberpunkplayer.bean.local.ShowVideoBean;
import com.mark.cyberpunkplayer.event.AppEvent;
import com.mark.cyberpunkplayer.event.PlayerEvent;
import com.mark.cyberpunkplayer.ui.fragment.mvc.AllVideoModel;
import com.mark.cyberpunkplayer.ui.fragment.mvc.MVCCallBack;
import com.mark.cyberpunkplayer.ui.adapter.ShowVideoAdapter;
import com.mark.cyberpunkplayer.util.AppExecutors;
import com.mark.cyberpunkplayer.util.ToastUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.mark.cyberpunkplayer.event.AppConstants.HOME_TOOLBAR_LOCAL_VIDEO_REFLASH;
import static com.mark.cyberpunkplayer.event.AppConstants.HOME_VIDEO_DELETE;
import static com.mark.cyberpunkplayer.event.AppConstants.HOME_VIDEO_SORT_SIZE;
import static com.mark.cyberpunkplayer.event.AppConstants.HOME_VIDEO_SORT_TIME;

public class LocalVideoFragment extends BaseFragment {

    private AllVideoModel allVideoModel;
    private ShowVideoAdapter showVideoAdapter;
    private List<ShowVideoBean> mList;

    private RecyclerView recyclerView;

    private int nowIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PlayerEvent event){
        switch (event.getType()){
            case PlayerEvent.PLAY_START:
                nowIndex = (int) event.getSeek();
                break;
            case PlayerEvent.PLAY_CHANGE_URL_LAST:
                if ((nowIndex - 1) < 0){
                    ToastUtils.showToast("没有更多的视频");
                }else {
                    DMarkVideoView.changeURL(mList.get(nowIndex - 1).getPath());
                    nowIndex = nowIndex  - 1;
                }
                break;
            case PlayerEvent.PLAY_CHANGE_URL_NEXT:
                if ((nowIndex + 1) >= mList.size()){
                    ToastUtils.showToast("没有更多的视频");
                }else {
                    DMarkVideoView.changeURL(mList.get(nowIndex + 1).getPath());
                    nowIndex = nowIndex + 1;
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppEvent appEvent){
        switch (appEvent.getType()){
            case HOME_VIDEO_DELETE:
                allVideoModel.getAllVideoCallBack(new MVCCallBack<List<ShowVideoBean>>() {
                    @Override
                    public void callback(final List<ShowVideoBean> list) {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                showVideoAdapter.changeListAndSort(AllVideoModel.SORT_TIME, list);
                                mList = list;
                            }
                        });
                    }
                }, AllVideoModel.SORT_TIME);
            case HOME_TOOLBAR_LOCAL_VIDEO_REFLASH:
                allVideoModel.getAllVideoCallBack(new MVCCallBack<List<ShowVideoBean>>() {
                    @Override
                    public void callback(final List<ShowVideoBean> list) {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                showVideoAdapter.changeListAndSort(AllVideoModel.SORT_TIME, list);
                                mList = list;
                            }
                        });
                    }
                }, AllVideoModel.SORT_TIME);
                break;
            case HOME_VIDEO_SORT_TIME:
                allVideoModel.getAllVideoCallBack(new MVCCallBack<List<ShowVideoBean>>() {
                    @Override
                    public void callback(final List<ShowVideoBean> list) {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                showVideoAdapter.changeListAndSort(AllVideoModel.SORT_TIME, list);
                                mList = list;
                            }
                        });
                    }
                }, AllVideoModel.SORT_TIME);
                break;
            case HOME_VIDEO_SORT_SIZE:
                allVideoModel.getAllVideoCallBack(new MVCCallBack<List<ShowVideoBean>>() {
                    @Override
                    public void callback(final List<ShowVideoBean> list) {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                showVideoAdapter.changeListAndSort( AllVideoModel.SORT_SIZE, list);
                                mList = list;
                                Logger.d("reflesh method size");
                            }
                        });
                    }
                }, AllVideoModel.SORT_SIZE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_video;
    }

    @Override
    protected void initView() {
        mList = new ArrayList<>();
        showVideoAdapter = new ShowVideoAdapter(getActivity(), mList);
        recyclerView = rootView.findViewById(R.id.local_video_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(showVideoAdapter);

    }

    @Override
    protected void initData() {
        allVideoModel = new AllVideoModel();
        showVideoAdapter.setLoading(true);
        allVideoModel.getAllVideoCallBack(new MVCCallBack<List<ShowVideoBean>>() {
            @Override
            public void callback(final List<ShowVideoBean> list) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mList = list;
                        showVideoAdapter.changeListAndSort(AllVideoModel.SORT_TIME, list);
                    }
                });
            }
        }, AllVideoModel.SORT_TIME);
    }

}
