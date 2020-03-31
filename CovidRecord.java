// Copyright Eric Chauvin 2020.



// This Covid 19 data comes from Johns Hopkins.
// It's the CSV text files for daily reports.
// Get data files from:
// https://github.com/CSSEGISandData/COVID-19/tree/master/csse_covid_19_data/csse_covid_19_daily_reports
// Press the "Raw" button and then just copy and 
// paste the data to a text file.


// FIPS,Admin2,Province_State,Country_Region,Last_Update,Lat,Long_,Confirmed,Deaths,Recovered,Active,Combined_Key
// 45001,Abbeville,South Carolina,US,2020-03-29 23:08:25,34.22333378,-82.46170658,3,0,0,0,"Abbeville, South Carolina, US"
// ,,,Brazil,2020-03-29 23:08:13,-14.235,-51.9253,4256,136,6,4114,Brazil

// FIPS county codes:
// https://en.wikipedia.org/wiki/FIPS_county_code
// https://www.census.gov/prod/techdoc/cbp/cbp95/st-cnty.pdf


public class CovidRecord
  {
  private MainApp mApp;
  public String FIPS = ""; // FIPs county code.
  public String Admin2 = ""; // County
  public String Province_State = "";
  public String Country_Region = "";
  String Last_Update = "";
  public double Latitude;
  public double Longitude;
  public int Confirmed;
  public int Deaths;
  public int Recovered;
  public int Active;
  public String CombinedKey = "";
  public int ConfirmedChange;
  public int DeathsChange;
  public int RecoveredChange;
  public int ActiveChange;




  private CovidRecord()
    {
    }


  public CovidRecord( MainApp useApp )
    {
    mApp = useApp;
    }



  public boolean setFieldsFromFileLine( String in )
    {
    if( in == null )
      return false;

    StringBuilder sBuilder = new StringBuilder();

    boolean isInsideQuote = false;
    int field = 0;
    int max = in.length();
    for( int count = 0; count < max; count++ )
      {
      char testC = in.charAt( count );
      if( testC == '"' )
        {
        // If there are things like escaped quote
        // characters like \" then this part would
        // have to be more complicated in order to
        // handle that too.
        if( isInsideQuote )
          isInsideQuote = false;
        else
          isInsideQuote = true;
  
        continue;
        }

      if( !isInsideQuote )
        {
        if( testC == ',' )
          {
          String fieldS = sBuilder.toString().trim();
          sBuilder.setLength( 0 );
          setFieldByIndex( field, fieldS );
          field++;
          continue;
          }
        }

      sBuilder.append( testC );
      }

    return true;
    }



  public boolean setFieldByIndex( int index, String in )
    {
    try
    {
    switch( index )
      {
      case 0:   
        FIPS = in;
        return true;

      case 1:
        Admin2 = in;
        return true;
     
      case 2:
        Province_State = in;
        return true;
     
      case 3:
        Country_Region = in;
        return true;

      case 4:
        Last_Update = in;
        return true;
     
      case 5:
        Latitude = Double.parseDouble( in );
        return true;
     
      case 6:
        Longitude = Double.parseDouble( in );
        return true;
     
      case 7:
        Confirmed = Integer.parseInt( in );
        return true;
     
      case 8:
        Deaths = Integer.parseInt( in );
        return true;
     
      case 9:
        Recovered = Integer.parseInt( in );
        return true;
     
      case 10:
        Active = Integer.parseInt( in );
        return true;
     
      case 11:
        CombinedKey = in;
        return true;

      case 12:
        mApp.showStatus( "What is this field? " + in );
        return false;

      // default:
      }
    
    return false;
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in setFieldByIndex()." );
      mApp.showStatus( e.getMessage() );
      return false;
      }
    }



  public String makeShowString()
    {
    double deathRatio = 0;
    if( Confirmed > 0 )
      deathRatio = (double)Deaths / (double)Confirmed;

    String result = "FIPS: " + FIPS + "\n" +
              "County: " + Admin2 + "\n" +
              "State: " + Province_State + "\n" +
              "Country/Region: " + Country_Region + "\n" +
              // "Updated: " + Last_Update + "\n" +
              // Latitude
              // Longitude
              "Ratio: " + deathRatio + "\n" +
              "Confirmed: " + Confirmed + "\n" +
              "Deaths: " + Deaths + "\n\n";
              // "Recovered: " + Recovered + "\n" +
              // "Active: " + Active + "\n\n";

    return result;
    }



  }
