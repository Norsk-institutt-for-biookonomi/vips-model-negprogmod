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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.List;
import no.nibio.vips.entity.ModelConfiguration;
import no.nibio.vips.entity.Result;
import no.nibio.vips.entity.ResultImpl;
import no.nibio.vips.entity.WeatherObservation;
import no.nibio.vips.i18n.I18nImpl;
import no.nibio.vips.model.ConfigValidationException;
import no.nibio.vips.model.Model;
import no.nibio.vips.model.ModelExcecutionException;
import no.nibio.vips.model.ModelId;
import no.nibio.vips.util.ModelUtil;

/**
 * @copyright 2016 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class NegativePrognosisModel extends I18nImpl implements Model{
    
    private final static ModelId MODEL_ID = new ModelId("NEGPROGMOD");
    
    private ModelUtil modelUtil;
    
    private DataMatrix dataMatrix;
    private TimeZone timeZone;
    
    // Minimum consecutive hours of humid conditions for 
    // adding A and B type contributions (sporulation and infection)
    private final Integer CONTRIBUTION_A_MINIMUM_DURATION = 4;
    private final Integer CONTRIBUTION_B_MINIMUM_DURATION = 10;
    
    // Humid/dry conditions
    private final Double CONTRIBUTION_AB_MINIMUM_PRECIPITATION = 0.1;
    private final Double CONTRIBUTION_AB_MINIMUM_HUMIDITY = 90.0;
    private final Double CONTRIBUTION_D_MAXIMUM_HUMIDITY = 70.0;
    
    // Other model thresholds
    // When we go from green to yellow or red status
    private final Double INFECTION_THRESHOLD = 150.0;
    // When daily contribution exeeds this (after total INFECTION_THRESHOLD has 
    // been exceeded), daily status is red, yellow otherwise
    private final Double INFECTION_DAILY_CONTRIBUTION_THRESHOLD = 7.0;
    
    // Other model constants
    private final Double DAILY_CONSTANT_C = 1.0926;
    private final Double DAILY_CONSTANT_D = -1.1232;

    public NegativePrognosisModel()
    {
        super("no.nibio.vips.model.negativeprognosismodel.texts");
        this.dataMatrix = new DataMatrix();
        this.modelUtil = new ModelUtil();
    }
    
    @Override
    public List<Result> getResult() throws ModelExcecutionException {
        this.calculateDailyContributions();
        
        Date currentDate = this.dataMatrix.getFirstDateWithParameterValue(DataMatrix.AGGREGATED_CONTRIB);
        Date lastDate = this.dataMatrix.getLastDateWithParameterValue(DataMatrix.AGGREGATED_CONTRIB);
        Calendar cal = Calendar.getInstance(this.timeZone);
        
        List<Result> retVal = new ArrayList<>();
        
        while(currentDate.compareTo(lastDate) <= 0)
        {
            Result result = new ResultImpl();
            result.setValidTimeStart(currentDate);
            // Set model params
            
            result.setValue(this.getModelId().toString(), DataMatrix.DAILY_CONTRIB_A, this.dataMatrix.getDefaultFormattedValueForDate(currentDate, DataMatrix.DAILY_CONTRIB_A));
            result.setValue(this.getModelId().toString(), DataMatrix.DAILY_CONTRIB_B, this.dataMatrix.getDefaultFormattedValueForDate(currentDate, DataMatrix.DAILY_CONTRIB_B));
            result.setValue(this.getModelId().toString(), DataMatrix.DAILY_CONTRIB_C, this.dataMatrix.getDefaultFormattedValueForDate(currentDate, DataMatrix.DAILY_CONTRIB_C));
            result.setValue(this.getModelId().toString(), DataMatrix.DAILY_CONTRIB_D, this.dataMatrix.getDefaultFormattedValueForDate(currentDate, DataMatrix.DAILY_CONTRIB_D));
            result.setValue(this.getModelId().toString(), DataMatrix.DAILY_CONTRIB, this.dataMatrix.getDefaultFormattedValueForDate(currentDate, DataMatrix.DAILY_CONTRIB));
            result.setValue(this.getModelId().toString(), DataMatrix.AGGREGATED_CONTRIB, this.dataMatrix.getDefaultFormattedValueForDate(currentDate, DataMatrix.AGGREGATED_CONTRIB));
            result.setValue(this.getModelId().toString(), "INFECTION_THRESHOLD", String.valueOf(this.INFECTION_THRESHOLD));
            // Deciding warning status
            Double aggregatedContribution = this.dataMatrix.getParamDoubleValueForDate(currentDate, DataMatrix.AGGREGATED_CONTRIB);
            Double dailyContribution = this.dataMatrix.getParamDoubleValueForDate(currentDate, DataMatrix.DAILY_CONTRIB);
            Integer warningStatus = 2;
            // If aggregated contribution < INFECTION_THRESHOLD: NO RISK
            if(aggregatedContribution >= this.INFECTION_THRESHOLD)
            {
                // If aggregated contribution >= INFECTION_THRESHOLD and daily contribution <= INFECTION_DAILY_CONTRIBUTION_THRESHOLD: MEDIUM RISK
                warningStatus = 3;
                // If aggregated contributin >= INFECTION_THRESHOLD and daily contribution > INFECTION_DAILY_CONTRIBUTION_THRESHOLD: HIGH RISK
                if(dailyContribution > this.INFECTION_DAILY_CONTRIBUTION_THRESHOLD)
                {
                    warningStatus = 4;
                }
                
            }
            result.setWarningStatus(warningStatus);
            retVal.add(result);
            // Moving on
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, 1);
            currentDate = cal.getTime();
        }
        return retVal;
    }

    @Override
    public ModelId getModelId() {
        return NegativePrognosisModel.MODEL_ID;
    }

    @Override
    public String getModelName() {
        return this.getModelName(Model.DEFAULT_LANGUAGE);
    }

    @Override
    public String getModelName(String language) {
        return this.getText("name", language);
    }

    @Override
    public String getLicense() {
        return "\n" +
            "Copyright (c) 2016 NIBIO <http://www.nibio.no/>. \n" +
            "\n" +
            "This file is part of NegativePrognosisModel.\n" +
            "NegativePrognosisModel is free software: you can redistribute it and/or modify\n" +
            "it under the terms of the NIBIO Open Source License as published by \n" +
            "NIBIO, either version 1 of the License, or (at your option) any\n" +
            "later version.\n" +
            "\n" +
            "NegativePrognosisModel is distributed in the hope that it will be useful,\n" +
            "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
            "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
            "NIBIO Open Source License for more details.\n" +
            "\n" +
            "You should have received a copy of the NIBIO Open Source License\n" +
            "along with NegativePrognosisModel.  If not, see <http://www.nibio.no/licenses/>.";
    }

    @Override
    public String getCopyright() {
        return "(c) 2016 NIBIO (http://www.nibio.no/). Contact: post@nibio.no";
    }

    @Override
    public String getModelDescription() {
        return this.getModelDescription(Model.DEFAULT_LANGUAGE);
    }

    @Override
    public String getModelDescription(String language) {
        try
        {
            return this.modelUtil.getTextWithBase64EncodedImages(this.getText("description", language), this.getClass());
        }
        catch(IOException ex)
        {
            return this.getText("description", language);
        }
    }

    @Override
    public String getWarningStatusInterpretation() {
        return this.getWarningStatusInterpretation(Model.DEFAULT_LANGUAGE);
    }

    @Override
    public String getWarningStatusInterpretation(String language) {
        return this.getText("statusInterpretation", language);
    }

    @Override
    public String getModelUsage() {
        return this.getModelUsage(Model.DEFAULT_LANGUAGE);
    }

    @Override
    public String getModelUsage(String language) {
        return this.getText("usage", language);
    }

    @Override
    public String getSampleConfig() {
        return  "{\n" +
                "\t\"loginInfo\":{\n" +
                "\t\t\"username\":\"example\",\n" +
                "\t\t\"password\":\"example\"\n" +
                "\t},\n" +
                "\t\"modelId\":\"" + MODEL_ID.toString() + "\",\n" +
                "\t\"configParameters\":{\n" +
                "\t\t\"observations\":[\n" +
                "\t\t{\n" +
                "\t\t\t\t\"timeMeasured\": \"2012-08-20T00:00:00+02:00\",\n" +
                "\t\t\t\t\"elementMeasurementTypeId\":\"TM\",\n" +
                "\t\t\t\t\"logIntervalId\":1,\n" +
                "\t\t\t\t\"value\":13.77\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\t\"timeMeasured\": \"2012-08-20T00:00:00+02:00\",\n" +
                "\t\t\t\t\"elementMeasurementTypeId\":\"RR\",\n" +
                "\t\t\t\t\"logIntervalId\":1,\n" +
                "\t\t\t\t\"value\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\t\"timeMeasured\": \"2012-08-20T00:00:00+02:00\",\n" +
                "\t\t\t\t\"elementMeasurementTypeId\":\"UM\",\n" +
                "\t\t\t\t\"logIntervalId\":1,\n" +
                "\t\t\t\t\"value\":60\n" +
                "\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}\n";
    }

    @Override
    public void setConfiguration(ModelConfiguration config) throws ConfigValidationException {
        // Setting timezone
        this.timeZone = TimeZone.getTimeZone((String) config.getConfigParameter("timeZone"));
        // Getting weather data
        List<WeatherObservation> observations = this.modelUtil.extractWeatherObservationList(config.getConfigParameter("observations"));
        for(WeatherObservation o:observations)
        {
            this.dataMatrix.setParamDoubleValueForDate(o.getTimeMeasured(), o.getElementMeasurementTypeId(), o.getValue());
        }
    }
    
    /**
     * Calculating the contribution of infection, sporulation, mycel and drying inhibition per hour
     */
    private void calculateHourlyContributions()
    {
        // Iterate the set of weather data
        Date currentTime = this.dataMatrix.getFirstDateWithParameterValue(DataMatrix.TM);
        Date endTime = this.dataMatrix.getLastDateWithParameterValue(DataMatrix.TM);
        /*
        System.out.println("Last temp=" + this.dataMatrix.getLastDateWithParameterValue(DataMatrix.TM));
        System.out.println("Last RH=" + this.dataMatrix.getLastDateWithParameterValue(DataMatrix.UM));
        System.out.println("Last rain=" + this.dataMatrix.getLastDateWithParameterValue(DataMatrix.RR));
        */
        Calendar cal = Calendar.getInstance(this.timeZone);
        
        Integer contributionACounter = 0;
        Integer contributionBCounter = 0;
        Double aggregatedContributionA = 0.0;
        Double aggregatedContributionB = 0.0;
        
        while(currentTime.compareTo(endTime) <= 0)
        {
            Double temp = this.dataMatrix.getParamDoubleValueForDate(currentTime, DataMatrix.TM);
            Double hum = this.dataMatrix.getParamDoubleValueForDate(currentTime, DataMatrix.UM);
            Double rain = this.dataMatrix.getParamDoubleValueForDate(currentTime, DataMatrix.RR);
            
            // A (infection) and B (sporulation) contributions
            // Humid conditions?
            // If so, contributions may be added, given that we have enough consecutive hours of humid conditions
            if(hum >= this.CONTRIBUTION_AB_MINIMUM_HUMIDITY || rain >= this.CONTRIBUTION_AB_MINIMUM_PRECIPITATION)
            {
                contributionACounter++;
                aggregatedContributionA += NegativePrognosisTable.getNegativePrognosisContribution("A", temp);
                if(contributionACounter >= this.CONTRIBUTION_A_MINIMUM_DURATION)
                {
                    this.dataMatrix.setParamDoubleValueForDate(currentTime, DataMatrix.HOURLY_CONTRIB_A, aggregatedContributionA);
                    aggregatedContributionA = 0.0;
                }
                contributionBCounter++;
                aggregatedContributionB += NegativePrognosisTable.getNegativePrognosisContribution("B", temp);
                if(contributionBCounter >= this.CONTRIBUTION_B_MINIMUM_DURATION)
                {
                    this.dataMatrix.setParamDoubleValueForDate(currentTime, DataMatrix.HOURLY_CONTRIB_B, aggregatedContributionB);
                    aggregatedContributionB = 0.0;
                }
            }
            // Otherwise, reset counter and contribution
            else
            {
                contributionACounter = 0;
                contributionBCounter = 0;
                aggregatedContributionA = 0.0;
                aggregatedContributionB = 0.0;
            }
            
            // C (mycel) contributions
            this.dataMatrix.setParamDoubleValueForDate(currentTime, DataMatrix.HOURLY_CONTRIB_C, NegativePrognosisTable.getNegativePrognosisContribution("C", temp));
            
            // D (drying inhibition) contributions
            this.dataMatrix.setParamDoubleValueForDate(currentTime, DataMatrix.HOURLY_CONTRIB_D, 
                    hum < this.CONTRIBUTION_D_MAXIMUM_HUMIDITY ? 
                    NegativePrognosisTable.getNegativePrognosisContribution("D", temp)
                    : 0.0
            );
            
            cal.setTime(currentTime);
            cal.add(Calendar.HOUR_OF_DAY, 1);
            currentTime = cal.getTime();
        }
        //System.out.println(this.dataMatrix.toCSV());
    }

    public void calculateDailyContributions()
    {
        this.calculateHourlyContributions();
        // Finding the first midnight time with data, starting from there
        Date firstPotentialTime = this.dataMatrix.getFirstDateWithParameterValue(DataMatrix.HOURLY_CONTRIB_C);
        Calendar cal = Calendar.getInstance(this.timeZone);
        cal.setTime(firstPotentialTime);
        while(cal.get(Calendar.HOUR_OF_DAY) != 0)
        {
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }
        Date currentDayStart = cal.getTime();
        cal.add(Calendar.HOUR_OF_DAY, 23);
        Date currentDayEnd = cal.getTime();
        Date lastTimeWithData = this.dataMatrix.getLastDateWithParameterValue(DataMatrix.HOURLY_CONTRIB_C);
        Double aggregatedContribution = 0.0;
        while(currentDayEnd.compareTo(lastTimeWithData) <= 0)
        {
            Double dailyContributionA = 0.0;
            Double dailyContributionB = 0.0;
            Double dailyContributionC = this.DAILY_CONSTANT_C;
            Double dailyContributionD = this.DAILY_CONSTANT_D;
            
            Date currentHour = currentDayStart;
            while(currentHour.compareTo(currentDayEnd) <= 0)
            {
                dailyContributionA += this.dataMatrix.getParamValueForDate(currentHour, DataMatrix.HOURLY_CONTRIB_A) != null ? 
                        this.dataMatrix.getParamDoubleValueForDate(currentHour, DataMatrix.HOURLY_CONTRIB_A)
                        : 0.0;
                dailyContributionB += this.dataMatrix.getParamValueForDate(currentHour, DataMatrix.HOURLY_CONTRIB_B) != null ? 
                        this.dataMatrix.getParamDoubleValueForDate(currentHour, DataMatrix.HOURLY_CONTRIB_B)
                        : 0.0;
                dailyContributionC += this.dataMatrix.getParamDoubleValueForDate(currentHour, DataMatrix.HOURLY_CONTRIB_C);
                dailyContributionD += this.dataMatrix.getParamDoubleValueForDate(currentHour, DataMatrix.HOURLY_CONTRIB_D);
                // Moving to next hour of day
                cal.setTime(currentHour);
                cal.add(Calendar.HOUR_OF_DAY, 1);
                currentHour = cal.getTime();
            }
            this.dataMatrix.setParamDoubleValueForDate(currentDayStart, DataMatrix.DAILY_CONTRIB_A, dailyContributionA);
            this.dataMatrix.setParamDoubleValueForDate(currentDayStart, DataMatrix.DAILY_CONTRIB_B, dailyContributionB);
            this.dataMatrix.setParamDoubleValueForDate(currentDayStart, DataMatrix.DAILY_CONTRIB_C, dailyContributionC);
            this.dataMatrix.setParamDoubleValueForDate(currentDayStart, DataMatrix.DAILY_CONTRIB_D, dailyContributionD);
            Double dailyTotalContribution = 
                    dailyContributionA
                    + dailyContributionB
                    + dailyContributionC
                    + dailyContributionD;
            this.dataMatrix.setParamDoubleValueForDate(currentDayStart, DataMatrix.DAILY_CONTRIB, dailyTotalContribution);
            aggregatedContribution += dailyTotalContribution;
            this.dataMatrix.setParamDoubleValueForDate(currentDayStart, DataMatrix.AGGREGATED_CONTRIB, aggregatedContribution);
                    
            // Moving to next day
            cal.setTime(currentDayStart);
            cal.add(Calendar.DATE, 1);
            currentDayStart = cal.getTime();
            cal.add(Calendar.HOUR_OF_DAY, 23);
            currentDayEnd = cal.getTime();
        }
        //System.out.println(this.dataMatrix.toCSV());
    }
}
