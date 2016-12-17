package bean;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;


/**
 * Created by mr.cheng on 2016/10/9.
 */

public class DialogWrapper {
    private BmobIMMessage msg;
    private BmobIMUserInfo info;
    private String ip;
    private String objectId;

    public DialogWrapper(BmobIMUserInfo info, BmobIMMessage msg) {
        this.info = info;
        this.msg = msg;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public BmobIMUserInfo getInfo() {
        return info;
    }

    public void setInfo(BmobIMUserInfo info) {
        this.info = info;
    }

    public BmobIMMessage getMsg() {
        return msg;
    }

    public void setMsg(BmobIMMessage msg) {
        this.msg = msg;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
