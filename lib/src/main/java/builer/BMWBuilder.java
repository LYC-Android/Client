package builer;

import java.util.ArrayList;

/**
 * Created by mr.cheng on 2016/10/20.
 */

public class BMWBuilder extends CarBuilder {
    private BMWModel bmw = new BMWModel();

    @Override
    public void setSequence(ArrayList<String> sequence) {
        this.bmw.setSequence(sequence);

    }

    @Override
    public CarModel getCarModel() {
        return this.bmw;
    }

}