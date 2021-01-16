cmake_minimum_required(VERSION 3.4.1)
#设置So文件的目录
set(distribution_DIR ../../../../libs/arm64-v8a)

add_library(
        native-lib
        SHARED
        nativelib.cpp

        jnihelper/StrHelper.cpp
        jnihelper/AndroidHelper.cpp

        player/IDemux.cpp
        player/FFDemux.cpp
        player/XData.cpp
        player/XLog.cpp
        player/XThread.cpp
        player/IObserver.cpp
        player/FFDecode.cpp
        player/IDecode.cpp
        player/XParameter.cpp
        player/IVideoView.cpp
        player/GLVideoView.cpp
        player/XTexture.cpp
        player/XEGL.cpp
        player/XShader.cpp
        player/FFResample.cpp
        player/IResample.cpp
        player/SLAudioPlay.cpp
        player/IAudioPlay.cpp
        player/IPlayer.cpp
        player/IPlayerBuilder.cpp
        player/FFPlayerBuilder.cpp
        player/IPlayerPorxy.cpp
)


find_library(
        log-lib
        log
        android-lib
        android
        EGL)

add_library( avcodec-58
        SHARED
        IMPORTED)

set_target_properties( avcodec-58
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libavcodec-58.so)

add_library(yuv
        SHARED
        IMPORTED)

set_target_properties(yuv
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libyuv.so)

add_library( avfilter-7
        SHARED
        IMPORTED)
set_target_properties( avfilter-7
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libavfilter-7.so)

add_library( avformat-58
        SHARED
        IMPORTED)
set_target_properties( avformat-58
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libavformat-58.so)

add_library( avutil-56
        SHARED
        IMPORTED)
set_target_properties( avutil-56
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libavutil-56.so)

add_library( swresample-3
        SHARED
        IMPORTED)
set_target_properties( swresample-3
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libswresample-3.so)

add_library( swscale-5
        SHARED
        IMPORTED)
set_target_properties( swscale-5
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/libswscale-5.so)

include_directories(include/libyuv)
include_directories(include)


target_link_libraries(
        native-lib
        avcodec-58
        avfilter-7
        avformat-58
        avutil-56
        swresample-3
        swscale-5
        yuv
        android
        GLESv2 EGL
        OpenSLES
        ${log-lib})