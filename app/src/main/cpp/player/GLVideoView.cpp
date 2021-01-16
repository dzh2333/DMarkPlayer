

extern "C"{

#include <libswscale/swscale.h>
#include <libavcodec/avcodec.h>
#include <libavutil/imgutils.h>
#include <libyuv.h>
}
#include <android/native_window.h>

#include "GLVideoView.h"
#include "XTexture.h"
#include "XLog.h"
#include "../nativelib.h"
#include "../jnihelper/AndroidHelper.h"
#include "FFdecode.h"
#include "FFPlayerBuilder.h"


int img_convert(AVPicture *dst, int dst_pix_fmt, const AVPicture *src,
                int src_pix_fmt, int src_width, int src_height) {
    int w;
    int h;
    struct SwsContext *pSwsCtx;

    w = src_width;
    h = src_height;

    pSwsCtx = sws_getContext(w, h, (AVPixelFormat) src_pix_fmt, w, h,
                             (AVPixelFormat) dst_pix_fmt, SWS_BICUBIC, NULL, NULL, NULL);
    sws_scale(pSwsCtx, (const uint8_t* const *) src->data, src->linesize, 0, h,
              dst->data, dst->linesize);

    XLOGE("返回0");
    return 0;
}

void GLVideoView::SetRender(ANativeWindow *win)
{
    index = 4;
    view = static_cast<ANativeWindow *>(win);

}

//BUG
void GLVideoView::Close()
{

    if(txt)
    {
        txt = 0;
    }

//        mux.lock();
////    XLOGE("解锁--上锁  GLVideoView");
////
//    if(txt)
//        {
//            txt->Drop();
//            txt = 0;
//        }
//    mux.unlock();
//    XLOGE("解锁--上锁  GLVideoView   完成");
}

/**
 * 渲染数据
 * @param data
 */
void GLVideoView::Render(XData data)
{
    isExit = false;
    if(!view){
        XLOGE("not have view");
        return;
    }

    ANativeWindow_setBuffersGeometry(view, data.width/*视频宽度*/, data.height/*视频高度*/,WINDOW_FORMAT_RGBA_8888);

    ANativeWindow_lock(view, &windowBuffer, NULL);

    //设置rgb_frame的属性（像素格式、宽高）和缓冲区
    //rgb_frame缓冲区与outBuffer.bits是同一块内存
    avpicture_fill((AVPicture *)data.dFrameRGBA, (const uint8_t *) windowBuffer.bits, AV_PIX_FMT_RGBA, data.width, data.height);
    //YUV->RGBA_8888
    switch (data.format){
        case AV_PIX_FMT_YUVJ420P:
        case AV_PIX_FMT_YUV420P:
            libyuv::I420ToARGB(data.dFrame->data[0], data.dFrame->linesize[0],
                               data.dFrame->data[2], data.dFrame->linesize[2],
                               data.dFrame->data[1], data.dFrame->linesize[1],
                               data.dFrameRGBA->data[0], data.dFrameRGBA->linesize[0],
                               data.width, data.height);
            break;
        case AV_PIX_FMT_YUVJ422P:
        case AV_PIX_FMT_YUV422P:
        case AV_PIX_FMT_YUYV422:
            libyuv::I422ToARGB(data.dFrame->data[0], data.dFrame->linesize[0],
                               data.dFrame->data[2], data.dFrame->linesize[2],
                               data.dFrame->data[1], data.dFrame->linesize[1],
                               data.dFrameRGBA->data[0], data.dFrameRGBA->linesize[0],
                               data.width, data.height);
            break;
        case AV_PIX_FMT_YUV444P:
            libyuv::I444ToARGB(data.dFrame->data[0], data.dFrame->linesize[0],
                               data.dFrame->data[2], data.dFrame->linesize[2],
                               data.dFrame->data[1], data.dFrame->linesize[1],
                               data.dFrameRGBA->data[0], data.dFrameRGBA->linesize[0],
                               data.width, data.height);
            break;
        case AV_PIX_FMT_NV12:
            libyuv::NV12ToARGB(data.dFrame->data[0], data.dFrame->linesize[0],
                               data.dFrame->data[2], data.dFrame->linesize[2],
                               data.dFrameRGBA->data[0], data.dFrameRGBA->linesize[0],
                               data.width, data.height);
            break;
        case AV_PIX_FMT_NV21:
            libyuv::NV21ToARGB(data.dFrame->data[0], data.dFrame->linesize[0],
                               data.dFrame->data[2], data.dFrame->linesize[2],
                               data.dFrameRGBA->data[0], data.dFrameRGBA->linesize[0],
                               data.width, data.height);
            break;
        default:
            break;

    }


    ANativeWindow_unlockAndPost(view);

//    if(!txt)
//    {
//        txt = XTexture::Create();
//        txt->Init(view, (XTextureType)data.format);
//    }
//    txt->Draw(data.datas,data.width,data.height);

}
