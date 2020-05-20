package com.nature.third.service;

import com.nature.component.stopsComponent.model.StopsTemplate;

public interface IStop {
    /**
     * Call the group interface
     *
     * @return
     */
    public String[] getAllGroup();

    public String[] getAllStops();

    public StopsTemplate getStopInfo(String bundle);

}
