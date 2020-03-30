// Copyright Eric Chauvin 2020.



// FIPS,Admin2,Province_State,Country_Region,Last_Update,Lat,Long_,Confirmed,Deaths,Recovered,Active,Combined_Key
// 45001,Abbeville,South Carolina,US,2020-03-29 23:08:25,34.22333378,-82.46170658,3,0,0,0,"Abbeville, South Carolina, US"
// ,,,Brazil,2020-03-29 23:08:13,-14.235,-51.9253,4256,136,6,4114,Brazil

// FIPS county codes:
// https://en.wikipedia.org/wiki/FIPS_county_code


public class CovidRecord
  {
  private MainApp mApp;
  public String FIPS = ""; // FIPs county code.
  public String Admin2 = ""; // County
  public String Province_State
  public String Country_Region
  String Last_Update
  public float Latitude;
  public float Longitude;
  public int Confirmed;
  public int Deaths;
  public int Recovered;
  public int Active;
  public String CombinedKey = "";




  private CovidRecord()
    {
    }


  public CovidRecord( MainApp useApp, String in )
    {


    }


  }
