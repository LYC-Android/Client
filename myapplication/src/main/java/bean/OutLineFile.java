package bean;

import org.litepal.crud.DataSupport;

/**
 * Created by mr.cheng on 2016/11/17.
 */
public class OutLineFile extends DataSupport {
    private boolean isSuccess;
    private String path;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
