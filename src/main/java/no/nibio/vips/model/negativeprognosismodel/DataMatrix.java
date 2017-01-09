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

import no.nibio.vips.util.DateMap;

/**
 * @copyright 2016 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class DataMatrix extends DateMap {
    public final static String TM = "TM"; 
    public final static String UM = "UM";
    public final static String RR = "RR";
    public final static String HOURLY_CONTRIB_A = "HOURLY_CONTRIB_A"; // Infection
    public final static String HOURLY_CONTRIB_B = "HOURLY_CONTRIB_B"; // Sporulation
    public final static String HOURLY_CONTRIB_C = "HOURLY_CONTRIB_C"; // Mycel tissue
    public final static String HOURLY_CONTRIB_D = "HOURLY_CONTRIB_D"; // Drying inhibition (?)
    public final static String DAILY_CONTRIB_A = "DAILY_CONTRIB_A"; // Infection
    public final static String DAILY_CONTRIB_B = "DAILY_CONTRIB_B"; // Sporulation
    public final static String DAILY_CONTRIB_C = "DAILY_CONTRIB_C"; // Mycel tissue
    public final static String DAILY_CONTRIB_D = "DAILY_CONTRIB_D"; // Drying inhibition (?)
    public final static String DAILY_CONTRIB = "DAILY_CONTRIB"; // SUM A-D per day
    public final static String AGGREGATED_CONTRIB = "AGGREGATED_CONTRIB"; // Aggregated SUM A-D from start of calculation
}
