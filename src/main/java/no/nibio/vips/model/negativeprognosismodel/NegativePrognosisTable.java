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

import java.util.HashMap;
import java.util.Map;

/**
 * @copyright 2016 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class NegativePrognosisTable {

    private static final Map<String, NegativePrognosisContribution[]> NEGATIVE_PROGNOSIS_CONTRIBUTION;
    static{
        NEGATIVE_PROGNOSIS_CONTRIBUTION = new HashMap<>();
        NEGATIVE_PROGNOSIS_CONTRIBUTION.put(
                "A", new NegativePrognosisContribution[] {
                    new NegativePrognosisContribution("A",10.0,12.0,0.898999989032745),
                    new NegativePrognosisContribution("A",14.0,16.0,0.411799997091293),
                    new NegativePrognosisContribution("A",16.0,18.0,0.533599972724915),
                    new NegativePrognosisContribution("A",18.0,20.0,0.881600022315979),
                    new NegativePrognosisContribution("A",20.0,22.0,1.04980003833771),
                    new NegativePrognosisContribution("A",22.0,24.0,0.585799992084503),
                }
            );
        NEGATIVE_PROGNOSIS_CONTRIBUTION.put(
                "B", new NegativePrognosisContribution[] {
                    new NegativePrognosisContribution("B",10.0,12.0,0.392399996519089),
                    new NegativePrognosisContribution("B",14.0,16.0,0.070200003683567),
                    new NegativePrognosisContribution("B",16.0,18.0,0.12780000269413),
                    new NegativePrognosisContribution("B",18.0,20.0,0.910799980163574),
                    new NegativePrognosisContribution("B",20.0,22.0,1.47060000896454),
                    new NegativePrognosisContribution("B",22.0,24.0,0.855000019073486),
                }
            );
        NEGATIVE_PROGNOSIS_CONTRIBUTION.put("C", new NegativePrognosisContribution[] {new NegativePrognosisContribution("C", 15.0, 20.0, 0.163900002837181)});
        NEGATIVE_PROGNOSIS_CONTRIBUTION.put("D", new NegativePrognosisContribution[] {new NegativePrognosisContribution("D", -100.0, 100.0, 0.0467999987304211)});
    }
    
    public static Double getNegativePrognosisContribution(String type, Double temperature){
        for(NegativePrognosisContribution contribution: NEGATIVE_PROGNOSIS_CONTRIBUTION.get(type))
        {
            if(temperature >= contribution.getTmMin() && temperature < contribution.getTmMax())
            {
                return contribution.getContribution();
            }
        }
        // No temperature interval match, return zero.
        return 0.0;
    }
}
