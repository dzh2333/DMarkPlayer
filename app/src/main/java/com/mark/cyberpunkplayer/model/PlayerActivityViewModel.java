package com.mark.cyberpunkplayer.model;

import com.mark.cyberpunkplayer.base.MvvmCallBack;
import com.mark.cyberpunkplayer.base.mvvm.AbstractViewModel;
import com.mark.cyberpunkplayer.databinding.PlayerHomeBinding;
import com.mark.cyberpunkplayer.player.PlayerBean;

public class PlayerActivityViewModel extends AbstractViewModel<PlayerHomeBinding> {

    public PlayerActivityViewModel(PlayerHomeBinding playerHomeBinding){
        super(playerHomeBinding);
    }

    public void getPlayerBean(MvvmCallBack<PlayerBean> callBack){
        PlayerBean playerBean = new PlayerBean("S", "url", 0,0, 0,0,0);
        callBack.onCallBack(playerBean);
    }

    public void setPlayerBean(){

    }


}
