package main_test;

import main.interfaces.FacadeI;
import main.services_layer.Facade;

/**
 * Created by gabigiladov on 4/25/15.
 */
public abstract class Driver {

    public static FacadeI getBridge() {
        Proxy bridge = new Proxy();

        bridge.setRealBridge(Facade.getFacade()); // add real bridge here
        return bridge;
    }
}
