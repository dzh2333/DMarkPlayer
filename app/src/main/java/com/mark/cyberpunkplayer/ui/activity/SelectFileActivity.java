package com.mark.cyberpunkplayer.ui.activity;

import android.content.Context;
import android.net.LinkAddress;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.cyberpunkplayer.R;
import com.mark.cyberpunkplayer.base.BaseActivity;
import com.mark.cyberpunkplayer.bean.local.BTSelectedFileBean;
import com.mark.cyberpunkplayer.event.BTEvent;
import com.mark.cyberpunkplayer.event.FileEvent;
import com.mark.cyberpunkplayer.ui.activity.mvc.SelectFileModel;
import com.mark.cyberpunkplayer.ui.adapter.SelectedFileRVAdapter;
import com.mark.cyberpunkplayer.ui.fragment.mvc.MVCCallBack;
import com.mark.cyberpunkplayer.ui.widget.DMarkToolbar;
import com.mark.cyberpunkplayer.util.AppExecutors;
import com.mark.cyberpunkplayer.util.FileUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.mark.cyberpunkplayer.event.BTEvent.BT_SELECT_FILE;

public class SelectFileActivity extends BaseActivity {

    private SelectedFileRVAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView preFilePathButton;
    private DMarkToolbar markToolbar;

    private ArrayList<BTSelectedFileBean> list;

    private SelectFileModel fileModel;

    private String nowPath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FileEvent event){
        Logger.d("收到了选择文件的消息" + event.toString());
        switch (event.getType()){
            case FileEvent.SELECTED_FILE:
                EventBus.getDefault().post(new BTEvent(BT_SELECT_FILE,null, event.getExtraData()));
                finish();
                break;
            case FileEvent.SELECT_FILE_PRE:
                String enterPath = FileUtil.getLastPath(event.getExtraData());
                if (enterPath.equals("")
                        || enterPath.equals("/storage/emulated/")
                        || enterPath.equals("/storage/emulated/0/")){
                    enterPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    preFilePathButton.setVisibility(View.INVISIBLE);
                }
                fileModel.getFileList(new MVCCallBack<List<BTSelectedFileBean>>() {
                    @Override
                    public void callback(final List<BTSelectedFileBean> btSelectedFileBeans) {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setLoading(false);
                                adapter.changeListData(btSelectedFileBeans);
                            }
                        });
                    }
                }, enterPath);
                nowPath = enterPath;
                break;
            case FileEvent.OPEN_DIRECTORY:
                fileModel.getFileList(new MVCCallBack<List<BTSelectedFileBean>>() {
                    @Override
                    public void callback(final List<BTSelectedFileBean> btSelectedFileBeans) {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setLoading(false);
                                adapter.changeListData(btSelectedFileBeans);
                            }
                        });
                    }
                }, event.getExtraData());
                nowPath = event.getExtraData();
                preFilePathButton.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.activity_select_file_rv);
        markToolbar = findViewById(R.id.activity_select_file_tb);
        preFilePathButton = findViewById(R.id.activity_select_file_pre_button);
        adapter = new SelectedFileRVAdapter(SelectFileActivity.this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(SelectFileActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(SelectFileActivity.this, DividerItemDecoration.VERTICAL));

        markToolbar.setTitle("选择文件");

        preFilePathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new FileEvent(FileEvent.SELECT_FILE_PRE, nowPath));
            }
        });
        preFilePathButton.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        fileModel = new SelectFileModel();
        fileModel.getFileList(new MVCCallBack<List<BTSelectedFileBean>>() {
            @Override
            public void callback(final List<BTSelectedFileBean> btSelectedFileBeans) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setLoading(false);
                        adapter.changeListData(btSelectedFileBeans);
                    }
                });
            }
        }, Environment.getExternalStorageDirectory().getAbsolutePath().toString());

        Logger.d("访问路径" + Environment.getExternalStorageDirectory().getAbsolutePath().toString());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_file;
    }
}
