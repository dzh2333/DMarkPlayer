


//
// Created by Administrator on 2018-03-01.
//

#include "IDemux.h"
#include "XLog.h"
#include "IPlayerPorxy.h"

//读取数据
void IDemux::Main()
{
    while(!isExit)
    {
        if(IsPause())
        {
            XSleep(2);
            continue;
        }
        XData d = Read();
        if(d.size > 0){
            Notify(d);
            IPlayerPorxy::Get()->PlayPos();
        }else{
            XSleep(2);
            if (IPlayerPorxy::Get()->PlayPos() >= 0.99){
                IPlayerPorxy::Get()->Over();
            }
        }
    }
}