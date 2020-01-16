package com.aimissu.basemvp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.aimissu.basemvp.utils.ToastUtils;

import java.io.File;
import java.util.Locale;
import java.util.regex.Pattern;

public class FileUtils {

    public static String getFileExtensionFromUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            int fragment = url.lastIndexOf('#');
            if (fragment > 0) {
                url = url.substring(0, fragment);
            }

            int query = url.lastIndexOf('?');
            if (query > 0) {
                url = url.substring(0, query);
            }

            int filenamePos = url.lastIndexOf('/');
            String filename =
                    0 <= filenamePos ? url.substring(filenamePos + 1) : url;

            // if the filename contains special characters, we don't
            // consider it valid for our matching purposes:
            if (!TextUtils.isEmpty(filename)) {
                int dotPos = filename.lastIndexOf('.');
                if (0 <= dotPos) {
                    return filename.substring(dotPos + 1);
                }
            }
        }

        return "";
    }
    /**
     * 按类型打开文件
     **/
    public static void openFile(Context context, File file) {
        Uri data;
        if (file != null ) {
            try {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory("android.intent.category.DEFAULT");
                String ext = MimeTypeMap.getFileExtensionFromUrl(file.getPath());
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                String mimeType = mimeTypeMap.getMimeTypeFromExtension(ext.toLowerCase());
                data = Uri.fromFile(file);
                intent.setDataAndType(data, mimeType);
                context.startActivity(intent);
            } catch (Exception e) {
                ToastUtils.showLocalToast("打开失败");
                e.printStackTrace();
            }
        } else {
            ToastUtils.showLocalToast("打开失败");
        }
    }
}