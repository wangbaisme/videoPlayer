package com.zc.xxj.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CommentDetailBean implements Parcelable {
    private int id;
    private String nickName;
    private String userLogo;
    private String content;
    private String imgId;
    private int replyTotal;
    private String createDate;
    private List<ReplyDetailBean> replyList;

    protected CommentDetailBean(Parcel in) {
        id = in.readInt();
        nickName = in.readString();
        userLogo = in.readString();
        content = in.readString();
        imgId = in.readString();
        replyTotal = in.readInt();
        createDate = in.readString();
    }

    public static final Creator<CommentDetailBean> CREATOR = new Creator<CommentDetailBean>() {
        @Override
        public CommentDetailBean createFromParcel(Parcel in) {
            return new CommentDetailBean(in);
        }

        @Override
        public CommentDetailBean[] newArray(int size) {
            return new CommentDetailBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nickName);
        dest.writeString(userLogo);
        dest.writeString(content);
        dest.writeString(imgId);
        dest.writeInt(replyTotal);
        dest.writeString(createDate);
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getNickName() {
        return nickName;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }
    public String getUserLogo() {
        return userLogo;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
    public String getImgId() {
        return imgId;
    }

    public void setReplyTotal(int replyTotal) {
        this.replyTotal = replyTotal;
    }
    public int getReplyTotal() {
        return replyTotal;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getCreateDate() {
        return createDate;
    }

    public void setReplyList(List<ReplyDetailBean> replyList) {
        this.replyList = replyList;
    }
    public List<ReplyDetailBean> getReplyList() {
        return replyList;
    }

    public CommentDetailBean(String nickName,  String content, String createDate) {
        this.nickName = nickName;
        this.content = content;
        this.createDate = createDate;
    }
}
