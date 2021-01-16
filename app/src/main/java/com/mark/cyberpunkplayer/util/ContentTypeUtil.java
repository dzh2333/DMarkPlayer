package com.mark.cyberpunkplayer.util;

public class ContentTypeUtil {

    public static String getType(String fileName){
        if (fileName.contains(".mp4")){
            return "video/mp4";
        }else if (fileName.contains(".avi")){
            return "video/avi";
        }else if (fileName.contains(".class")){
            return "java/*";
        } else if (fileName.contains(".mp3")){
            return "audio/mp3";
        } else if (fileName.contains(".gif")){
            return "image/gif";
        } else if (fileName.contains(".html")){
            return "text/html";
        } else if (fileName.contains(".jpg")){
            return "application/x-jpg";
        }
        else if (fileName.contains(".pdf")){
            return "application/pdf";
        }
        else if (fileName.contains(".txt")){
            return "text/plain";
        }

        return "application/octet-stream";
    }
}
