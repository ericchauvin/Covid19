// Copyright Eric Chauvin 2020.



public class CovidRecordArray
  {
  private MainApp mApp;
  private CovidRecord[] covidRecArray;
  private int[] sortIndexArray;
  private int arrayLast = 0;



  private CovidRecordArray()
    {
    }



  public CovidRecordArray( MainApp useApp )
    {
    mApp = useApp;

    covidRecArray = new CovidRecord[8];
    sortIndexArray = new int[8];
    resetSortIndexArray();
    }



  public void clear()
    {
    arrayLast = 0;
    }



  private void resetSortIndexArray()
    {
    // It's not to arrayLast.  It's to the whole length.
    int max = sortIndexArray.length;
    for( int count = 0; count < max; count++ )
      sortIndexArray[count] = count;

    }



  private void resizeArrays( int toAdd )
    {
    int oldLength = sortIndexArray.length;
    sortIndexArray = new int[oldLength + toAdd];
    resetSortIndexArray();

    CovidRecord[] tempRecArray = new CovidRecord[
                                 oldLength + toAdd];
    
    for( int count = 0; count < arrayLast; count++ )
      {
      tempRecArray[count] = covidRecArray[count];
      }

    covidRecArray = tempRecArray;
    }


/*
  public void sort()
    {
    if( arrayLast < 2 )
      return;

    for( int count = 0; count < arrayLast; count++ )
      {
      if( !bubbleSortOnePass() )
        break;

      }
    }



  private boolean bubbleSortOnePass()
    {
    // This returns true if it swaps anything.

    boolean switched = false;
    for( int count = 0; count < (arrayLast - 1); count++ )
      {
      // compareTo() uses case.
      if( valueArray[count].compareToIgnoreCase(
                        valueArray[count + 1] ) > 0 )
        {
        int temp = sortIndexArray[count];
        sortIndexArray[count] = sortIndexArray[count + 1];
        sortIndexArray[count + 1] = temp;
        switched = true;
        }
      }

    return switched;
    }
*/



  public void readFromFiles()
    {
    try
    {
    String mainDir = "C:\\Eric\\Covid19\\";

    String fileName = mainDir + "Covid19Data.txt";

    String dataS = FileUtility.readFileToString(
                                        mApp,
                                        fileName,
                                        false );

    if( dataS.length() == 0 )
      {
      mApp.showStatus( " " );
      mApp.showStatus( "There is nothing in the file." );
      mApp.showStatus( fileName );
      return;
      }

    // mApp.showStatus( dataS );

    StringArray sArray = new StringArray();
    int max = sArray.makeFieldsFromString( dataS,
                                           '\n' );

    int totalRecords = 0;
    int totalDeaths = 0;
    int totalConfirmed = 0;
    for( int count = 0; count < max; count++ )
      {
      String line = sArray.getStringAt( count );
      if( line.contains( "FIPS,Admin2,Province_State," ))
        continue;

      CovidRecord rec = new CovidRecord( mApp );
      if( !rec.setFieldsFromFileLine( line ))
        continue;

       if( (rec.Deaths == 0) &&
           (rec.Confirmed == 0) )
         continue;

       // Show only the ones where deaths == confirmed.
       // if( rec.Deaths < rec.Confirmed )
         // continue;
      
      // Just look at US county records.
      if( rec.FIPS.length() < 5)
        continue;

      // Colorado:
      // if( !rec.FIPS.startsWith( "08" ))
        // continue;

      // District of Columbia is FIPS: 11001
      // if( !rec.FIPS.startsWith( "11" ))
        // continue;

      // FIPS: 60000 American Samoa
      // FIPS: 66000 Guam
      // FIPS: 69000 Northern Mariana Islands
      if( rec.FIPS.startsWith( "6" ))
        continue;

      // FIPS: 78000 Virgin Islands
      if( rec.FIPS.startsWith( "7" ))
        continue;

      // FIPS: 88888 Diamond Princess cruise ship.
      if( rec.FIPS.startsWith( "8" ))
        continue;

      // FIPS: 99999 Grand Princess cruise ship
      if( rec.FIPS.startsWith( "9" ))
        continue;

      // Statewide is not a county.
      if( rec.FIPS.endsWith( "999" ))
        continue;

      // if( !rec.Country_Region.equals( "US" ))
        // continue;

      totalDeaths += rec.Deaths;
      totalConfirmed += rec.Confirmed;
      totalRecords++;

      if( rec.Deaths < 10 )
        continue;

      // if( rec.Province_State.equals( "Colorado" ))
        // continue;
      
      mApp.showStatus( rec.makeShowString());
      addRecord( rec );
      }

    mApp.showStatus( " " );
    // Wikipedia says there are "3,142 counties
    // and county-equivalents".
    mApp.showStatus( "Total records: " + totalRecords );
    mApp.showStatus( "Total deaths: " + totalDeaths );
    mApp.showStatus( "Total confirmed: " +
                                  totalConfirmed );

    double deathRatio = (double)totalDeaths /
                        (double)totalConfirmed;

    mApp.showStatus( "Death ratio: " + deathRatio );

    mApp.showStatus( " " );
    mApp.showStatus( "Finished processing the file." );
    mApp.showStatus( " " );
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in readFromFiles()." );
      mApp.showStatus( e.getMessage() );
      }
    }



  public void addRecord( CovidRecord rec )
    {
    if( arrayLast >= sortIndexArray.length )
      resizeArrays( 1024 );

    covidRecArray[arrayLast] = rec;
    arrayLast++;
    }



  }
