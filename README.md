# NegativePrognosisModel

<p>From a specific date (50% germination), daily risk values are accumulated based on weather data (temperature, relative humidity and precipitation). The risk is an accumulated value of how the weather affects late blight germination/infection, sporulation and growth. Alle processes are corrected for inhibition due to drying. After the accumulated risk has reached a certain limit (150), there will be an infection risk if there is one day with high infection risk. This model is together with time of row closure, used to decide the timing of first spray against potato late blight.</p>
<h4>First spray</h4>
<p>There is normally no need for spraying against potato late blight before row closure. Long lasting, favourable weather conditions in areas with early production, in combination with high levels of primary inoculum, can make it necessary to spray earlier. If the accumulated risk based on the negative prognosis model reaches 150 before row closure, first spray should be applied at the next infection period. In years with long periods with wet soil early in the season can also lead to early risk of infection.</p>
<p>When accumulated risk &lt; 150, warning symbol is green</p>
<p>When accumulated risk &gt; 150, warning symbol is yellow if the daily risk is below 7 and red if it is above 7</p>
<p>When a late blight warning has been issued, cultivar suseptibility, selection of fungicide and time of application should be considered before spraying. Risk of getting an infection is highest if warnings are issued two days ore more in a row, and when late blight has been observed in the area. &lsquo;</p>
<p>Fungicides are most effective when used as protectants against late blight. Infection normally occurs during the morning, which means that the spray should be applied the day before if not covered by the last spray. One application normally protects the crop for 7 to 14 days, depending on the fungicide, dose and growing conditions.&nbsp;</p>
<h4>Testing and validation</h4>
<p>This model has been tested in several countries, and is the basis for late blight warnings in the USA (NegFry)</p>
<h4>References</h4>
<p>Ullrich J. &amp; H. Schr&ouml;dter 1966. Das Problem der Vorhersage des Auftretens der Kartoffel krautf&auml;ule (Phytophthora infestans) und die M&ouml;glichkeit seiner L&ouml;sung durch eine &ldquo;Negativprognose&rdquo;. Nachrichtenblatt Deutsch. Pflanzenschutzdienst (Braunschweig) 18, 33-40</p>

### Development

Clone Gitlab repository

```
$ git clone https://gitlab.nibio.no/VIPS/models/java/Model_NEGPROGMOD.git
$ cd Model_NEGPROGMOD
```