package builer;

import java.util.ArrayList;

/**
 * Created by mr.cheng on 2016/10/20.
 */

public abstract class CarModel {
    private ArrayList<String> sequence = new ArrayList<>();

    protected abstract void start();

    protected abstract void alarm();

    protected abstract void stop();

    protected abstract void engineBoom();

    final public void run() {
        for (int i = 0; i < sequence.size(); i++) {
            String actionName = this.sequence.get(i);
            if (actionName.equalsIgnoreCase("start")) { //如果是start关键字，
                this.start(); //开启汽车
            } else if (actionName.equalsIgnoreCase("stop")) { //如果是stop关键字
                this.stop(); //停止汽车
            } else if (actionName.equalsIgnoreCase("alarm")) { //如果是alarm关键字
                this.alarm(); //喇叭开始叫了
            } else if (actionName.equalsIgnoreCase("engine boom")) { //如果是engine
                this.engineBoom(); //引擎开始轰鸣
            }
        }
    }
    //把传递过来的值传递到类内
    final public void setSequence(ArrayList<String> sequence){
        this.sequence = sequence;
    }

}
