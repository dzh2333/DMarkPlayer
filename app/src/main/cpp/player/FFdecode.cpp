
/**
 * 音频解码
 */
extern "C"
{
    #include <libavcodec/avcodec.h>
    #include <libavcodec/jni.h>
    #include <libavutil/imgutils.h>
}

#include "FFDecode.h"
#include "XLog.h"
#include "IPlayerPorxy.h"
#include "../jnihelper/AndroidHelper.h"
#include "../nativelib.h"

void FFDecode::InitHard(void *vm)
{
    av_jni_set_java_vm(vm,0);
}

void FFDecode::Clear()
{
    IDecode::Clear();
    mux.lock();
    if(codec)
        avcodec_flush_buffers(codec);
    mux.unlock();
}

void FFDecode::Close()
{
    IDecode::Clear();
    mux.lock();
    pts = 0;
    if(frame)
        av_frame_free(&frame);
    if(frameRGBA){
        av_frame_free(&frameRGBA);
    }
    if(codec)
    {
        avcodec_close(codec);
        avcodec_free_context(&codec);
    }
    mux.unlock();
}

bool FFDecode::Open(XParameter para,bool isHard)
{
    index = 1;
    Close();
    if(!para.para) return false;
    AVCodecParameters *p = para.para;

    //1 查找解码器
    AVCodec *cd = avcodec_find_decoder(p->codec_id);
    if(isHard)
    {
        cd = avcodec_find_decoder_by_name("h264_mediacodec");
    }

    if(!cd)
    {
        XLOGE("avcodec_find_decoder %d failed!  %d",p->codec_id,isHard);
        return false;
    }
    XLOGI("avcodec_find_decoder success %d!",isHard);

    mux.lock();
    //2 创建解码上下文，并复制参数
    codec = avcodec_alloc_context3(cd);
    avcodec_parameters_to_context(codec, p);

    codec->thread_count = 8;
    //3 打开解码器
    int re = avcodec_open2(codec, 0, 0);
    if(re != 0)
    {
        mux.unlock();
        char buf[1024] = {0};
        av_strerror(re,buf,sizeof(buf)-1);
        XLOGE("%s",buf);
        return false;
    }

    if(codec->codec_type == AVMEDIA_TYPE_VIDEO)
    {
        this->isAudio = false;
    }
    else
    {
        this->isAudio = true;
    }
    mux.unlock();


    return true;
}
bool FFDecode::SendPacket(XData pkt)
{
    if(pkt.size<=0 || !pkt.data)return false;
    mux.lock();
    if(!codec)
    {
        mux.unlock();
        return false;
    }
    int re = avcodec_send_packet(codec,(AVPacket*)pkt.data);
    mux.unlock();
    if(re != 0)
    {
        return false;
    }
    return true;
}

AVCodecContext* FFDecode::getCodec(){
    if (codec == 0){
        return NULL;
    }
    return codec;
}


AVFrame* FFDecode::getFrameRGBA(){
    return frameRGBA;
}

AVFrame* FFDecode::getFrame(){
    return frame;
}

//从线程中获取解码结果
XData FFDecode::RecvFrame()
{
    mux.lock();
    if(!codec)
    {
        mux.unlock();
        return XData();
    }
    if(!frame)
    {
        frame = av_frame_alloc();
    }
    if(!frameRGBA){
        frameRGBA = av_frame_alloc();
    }
    int re = avcodec_receive_frame(codec, frame);
    if(re != 0)
    {
        mux.unlock();
        return XData();
    }

    XData d;
    d.data = (unsigned char *)frame;

    if(codec->codec_type == AVMEDIA_TYPE_VIDEO && IPlayerPorxy::Get()->getPlayMode() == 0)
    {
        d.size = (frame->linesize[0] + frame->linesize[1] + frame->linesize[2])*frame->height;
        d.width = frame->width;
        d.height = frame->height;
        if (IPlayerPorxy::Get()->getWidth() == 0){
            IPlayerPorxy::Get()->setWidth(codec->width);
            IPlayerPorxy::Get()->setHeight(codec->height);

            jclass cls;
            jmethodID mid;
            JNIEnv *envl;

            //Attach主线程
            if(AndroidHelper::getInstance()->GetVM()->AttachCurrentThread(&envl, NULL) != JNI_OK)
            {
                XLOGE("%s: AttachCurrentThread() failed", __FUNCTION__);
            }

            cls = envl->GetObjectClass(nativelib::GetVideoCls());
            if(cls == NULL)
            {
                XLOGE("FindClass() Error.....");
            }
            //再获得类中的方法
            mid = envl->GetStaticMethodID(cls, "setVideoViewLayoutParams", "(I)V");
            envl->CallStaticVoidMethod(cls, mid , codec->width);
        }
    }else
    {
        //样本字节数 * 单通道样本数 * 通道数
        d.size = av_get_bytes_per_sample((AVSampleFormat)frame->format)*frame->nb_samples*2;
    }
    d.format = frame->format;
    d.dFrame = frame;
    d.dFrameRGBA = frameRGBA;
    //if(!isAudio)
    //    XLOGE("data format is %d",frame->format);
    memcpy(d.datas, frame->data, sizeof(d.datas));
    d.pts = frame->pts;
    pts = d.pts;

    mux.unlock();

    return d;
}
