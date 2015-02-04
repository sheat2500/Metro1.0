package me.blueland.metro.model;

import java.io.Serializable;

/**
 * Created by Te on 2/4/15.
 */
public class BusRoute implements Serializable {


    String RouteId;
    String RouteName;

    public BusRoute(String routeId, String routeName) {
        RouteId = routeId;
        RouteName = routeName;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    public String getRouteId() {
        return RouteId;
    }

    public void setRouteId(String routeId) {
        RouteId = routeId;
    }


}
