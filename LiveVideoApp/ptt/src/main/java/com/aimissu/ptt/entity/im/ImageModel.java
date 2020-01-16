package com.aimissu.ptt.entity.im;

public class ImageModel  extends BaseFile
    {
        /// <summary>
        ///  图片地址
        /// </summary>
        public String ImageUrl ;
        public String FileId ;

        @Override
        public String toString() {
            return "ImageModel{" +
                    "ImageUrl='" + ImageUrl + '\'' +
                    ", FileId='" + FileId + '\'' +
                    ", FileName='" + FileName + '\'' +
                    ", FileCode='" + FileCode + '\'' +
                    ", FileSize='" + FileSize + '\'' +
                    '}';
        }
    }