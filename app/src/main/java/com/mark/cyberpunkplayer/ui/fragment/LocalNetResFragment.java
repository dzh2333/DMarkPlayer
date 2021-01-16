package com.mark.cyberpunkplayer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.base.BaseCallBack;
import com.mark.cyberpunkplayer.base.BaseFragment;
import com.mark.cyberpunkplayer.base.BaseListCallBack;
import com.mark.cyberpunkplayer.bean.network.LocalNetResBean;
import com.mark.cyberpunkplayer.db.DBManager;
import com.mark.cyberpunkplayer.db.SmbBean;
import com.mark.cyberpunkplayer.event.SmbSessionEvent;
import com.mark.cyberpunkplayer.ui.activity.NewSessionActivity;
import com.mark.cyberpunkplayer.ui.adapter.LocalNetRVAdapter;
import com.mark.cyberpunkplayer.ui.fragment.mvc.SmbSessionModel;
import com.mark.cyberpunkplayer.util.AppExecutors;
import com.mark.cyberpunkplayer.util.LogUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.mark.cyberpunkplayer.event.SmbSessionEvent.BACK_TO_UP_LEVEL;


public class LocalNetResFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<SmbBean> mList;
    private LocalNetRVAdapter adapter;
    private ImageView floatButton;

    private SmbSessionModel smbSessionModel;

    private SmbBean nowBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final SmbSessionEvent smbSessionEvent){
        LogUtils.d("share folder enter folder: event" + smbSessionEvent.toString());
        switch (smbSessionEvent.getType()){
            case SmbSessionEvent.OPEN_DIRECTORY:
                adapter.dataloading();
                smbSessionModel.openFile(smbSessionEvent.getBean(),
                        new BaseListCallBack<SmbBean>() {
                            @Override
                            public void callBacks(final List<SmbBean> t) {
                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.changeListData(t);
                                    }
                                });
                            }
                        });
                floatButton.setImageResource(R.drawable.icon_back);
                nowBean = smbSessionEvent.getBean();
                floatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new SmbSessionEvent(BACK_TO_UP_LEVEL, nowBean));
                    }
                });
                break;
            case SmbSessionEvent.OPEN_SESSION:
                adapter.dataloading();
                smbSessionModel.openSession(smbSessionEvent.getBean(),
                        new BaseCallBack<List<SmbBean>>() {
                            @Override
                            public void callBack(final List<SmbBean> list) {
                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.changeListData(list);
                                    }
                                });
                            }
                        });
                break;
            case SmbSessionEvent.DELETE_SESSION:
                DBManager.getInstance().removeSmbBeans(smbSessionEvent.getBean());
                adapter.changeListData(DBManager.getInstance().getAllSmbBean());
                break;
            case SmbSessionEvent.SUCCESS_SESSION:
                DBManager.getInstance().addSmbBean(smbSessionEvent.getBean());
                adapter.changeListData(DBManager.getInstance().getAllSmbBean());
                break;
            case BACK_TO_UP_LEVEL:
                adapter.dataloading();
                SmbBean bean = smbSessionEvent.getBean();
                String[] inputFilePath = bean.getFolderPath().split("\\\\");
                if (inputFilePath.length == 0 || (inputFilePath.length == 1 && inputFilePath[0].equals(""))){
                    bean.setFolderPath("");
                    bean.setDiskPath("");
                    bean.setFileName("");
                    bean.setShowLevel(1);
                    adapter.dataloading();
                    smbSessionModel.openSession(bean,
                            new BaseCallBack<List<SmbBean>>() {
                                @Override
                                public void callBack(final List<SmbBean> list) {
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.changeListData(list);
                                        }
                                    });
                                }
                            });
                    return;
                }
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < inputFilePath.length - 1; i++) {
                    sb.append(inputFilePath[i] + "\\");
                }
                bean.setFolderPath(sb.toString());
                bean.setFileName("");
                smbSessionModel.openFile(bean,
                        new BaseListCallBack<SmbBean>() {
                            @Override
                            public void callBacks(final List<SmbBean> t) {
                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.changeListData(t);
                                    }
                                });
                            }
                        });
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
        return R.layout.fragment_local_net_res;
    }

    @Override
    protected void initView() {
        mList = new ArrayList<>();
        recyclerView = mView.findViewById(R.id.f_local_net_rv);
        floatButton = mView.findViewById(R.id.f_local_net_float_button);
        adapter = new LocalNetRVAdapter(getContext(), mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewSessionActivity.class));
            }
        });
    }

    @Override
    protected void initData() {
        adapter.changeListData(DBManager.getInstance().getAllSmbBean());
        smbSessionModel = new SmbSessionModel();
    }


}
