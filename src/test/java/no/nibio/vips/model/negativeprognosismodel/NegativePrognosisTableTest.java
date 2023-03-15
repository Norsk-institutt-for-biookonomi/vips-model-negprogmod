/*
 * Copyright (c) 2016 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of NegativePrognosisModel.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * 
 */
package no.nibio.vips.model.negativeprognosismodel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author treinar
 */
public class NegativePrognosisTableTest {
    
    public NegativePrognosisTableTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getNegativePrognosisContribution method, of class NegativePrognosisTable.
     */
    @Test
    public void testGetNegativePrognosisContribution() {
        System.out.println("getNegativePrognosisContribution");
        String[] types = {"A","B","C","D"};
        Double[] temps = {0.0,10.2,13.1,16.9,18.001,30.0};
        Double[] expResults = {
            0.0,0.898999989032745,0.0,0.533599972724915,0.881600022315979,0.0, // A
            0.0,0.392399996519089,0.0,0.12780000269413,0.910799980163574,0.0, // B
            0.0,0.0,0.0,0.163900002837181,0.163900002837181,0.0, // C
            0.0467999987304211,0.0467999987304211,0.0467999987304211,0.0467999987304211,0.0467999987304211,0.0467999987304211// D
        };
        for(int i=0;i<types.length;i++)
        {
            for(int j=0;j<temps.length;j++)
            {
                System.out.println("Type = " + types[i] + ", temp = " + temps[j]);
                Double result = NegativePrognosisTable.getNegativePrognosisContribution(types[i], temps[j]);
                assertEquals(expResults[i*6+j],result);
            }
        }
        
    }
    
}
