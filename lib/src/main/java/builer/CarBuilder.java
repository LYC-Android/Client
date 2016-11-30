package builer;

import java.util.ArrayList;

/**
 * Created by mr.cheng on 2016/10/20.
 */

public abstract class CarBuilder {
    public abstract void setSequence(ArrayList<String> sequence);
    public abstract CarModel getCarModel();
}
