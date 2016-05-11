/*
 * Copyright (c) 2016 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of NegativePrognosisModel.
 * NegativePrognosisModel is free software: you can redistribute it and/or modify
 * it under the terms of the NIBIO Open Source License as published by 
 * NIBIO, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * NegativePrognosisModel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NIBIO Open Source License for more details.
 * 
 * You should have received a copy of the NIBIO Open Source License
 * along with NegativePrognosisModel.  If not, see <http://www.nibio.no/licenses/>.
 * 
 */

package no.nibio.vips.model.negativeprognosismodel;

/**
 * @copyright 2016 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class NegativePrognosisContribution {

    private String type;
    private Double tmMin, tmMax, contribution;
    
    public NegativePrognosisContribution(String type, Double tmMin, Double tmMax, Double contribution){
        this.type = type;
        this.tmMin = tmMin;
        this.tmMax = tmMax;
        this.contribution = contribution;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the tmMin
     */
    public Double getTmMin() {
        return tmMin;
    }

    /**
     * @param tmMin the tmMin to set
     */
    public void setTmMin(Double tmMin) {
        this.tmMin = tmMin;
    }

    /**
     * @return the tmMax
     */
    public Double getTmMax() {
        return tmMax;
    }

    /**
     * @param tmMax the tmMax to set
     */
    public void setTmMax(Double tmMax) {
        this.tmMax = tmMax;
    }

    /**
     * @return the contribution
     */
    public Double getContribution() {
        return contribution;
    }

    /**
     * @param contribution the contribution to set
     */
    public void setContribution(Double contribution) {
        this.contribution = contribution;
    }
}
