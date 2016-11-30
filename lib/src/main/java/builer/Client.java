package builer;

import java.util.ArrayList;

/**
 * Created by mr.cheng on 2016/10/20.
 */

public class Client {
    public static void main(String[] args) {
//        BenzModel benz = new BenzModel();
//        ArrayList<String> sequence = new ArrayList<String>(); //存放run的顺序
//        sequence.add("engine boom"); //客户要求，run的时候时候先发动引擎
//        sequence.add("start"); //启动起来
//        sequence.add("stop"); //开了一段就停下来
////然后我们把这个顺序给奔驰车：
//        benz.setSequence(sequence);
//        benz.run();
        ArrayList<String> sequence = new ArrayList<String>(); //存放run的顺序
        sequence.add("engine boom"); //客户要求，run的时候时候先发动引擎
        sequence.add("start"); //启动起来
        sequence.add("stop"); //开了一段就停下来
        BenzBuilder benzBuilder = new BenzBuilder();
//把顺序给这个builder类，制造出这样一个车出来
        benzBuilder.setSequence(sequence);
//制造出一个奔驰车
        BenzModel benz = (BenzModel)benzBuilder.getCarModel();
//奔驰车跑一下看看
        benz.run();
    }
}
