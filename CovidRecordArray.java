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



  public void sortByFips()
    {
    if( arrayLast < 2 )
      return;

    for( int count = 0; count < arrayLast; count++ )
      {
      if( !bubbleSortOnePassFips() )
        break;

      }
    }



  private boolean bubbleSortOnePassFips()
    {
    // This returns true if it swaps anything.

    boolean switched = false;
    for( int count = 0; count < (arrayLast - 1); count++ )
      {
      // compareTo() uses case.
      if( covidRecArray[sortIndexArray[count]].FIPS.
             compareToIgnoreCase(
             covidRecArray[sortIndexArray[count + 1]].
             FIPS ) > 0 )
        {
        int temp = sortIndexArray[count];
        sortIndexArray[count] = sortIndexArray[count + 1];
        sortIndexArray[count + 1] = temp;
        switched = true;
        }
      }

    return switched;
    }



  public void sortByDeaths()
    {
    if( arrayLast < 2 )
      return;

    for( int count = 0; count < arrayLast; count++ )
      {
      if( !bubbleSortOnePassDeaths() )
        break;

      }
    }



  private boolean bubbleSortOnePassDeaths()
    {
    // This returns true if it swaps anything.

    boolean switched = false;
    for( int count = 0; count < (arrayLast - 1); count++ )
      {
      if( covidRecArray[sortIndexArray[count]].Deaths
             < covidRecArray[
               sortIndexArray[count + 1]].Deaths )
        {
        int temp = sortIndexArray[count];
        sortIndexArray[count] = sortIndexArray[count + 1];
        sortIndexArray[count + 1] = temp;
        switched = true;
        }
      }

    return switched;
    }



  public void setChangeValues( CovidRecordArray prev )
    {
    sortByFips();
    prev.sortByFips();
    if( arrayLast != prev.arrayLast )
      {
      // The number of counties shouldn't change.
      // Not normally.
      mApp.showStatus( "Why aren't these the same for lastArray?" );
      return;
      }

    for( int count = 0; count < arrayLast; count++ )
      {
      CovidRecord rec = covidRecArray[
                        sortIndexArray[count]];

      CovidRecord prevRec = prev.covidRecArray[
                        prev.sortIndexArray[count]];

      if( !rec.FIPS.equals( prevRec.FIPS ))
        {
        mApp.showStatus( "These FIPS values should be the same." );
        return;
        }

      // These are references to the original objects.
      rec.ConfirmedChange = rec.Confirmed -
                            prevRec.Confirmed;

      rec.DeathsChange = rec.Deaths -
                         prevRec.Deaths;

      }



    }



  public void showRecords()
    {
    for( int count = 0; count < arrayLast; count++ )
      {
      CovidRecord rec = covidRecArray[
                        sortIndexArray[count]];

      // if( rec.Deaths < 10 )
        // continue;

      if( (rec.Deaths == 0) &&
          (rec.Confirmed == 0) )
        continue;

      mApp.showStatus( rec.makeShowString());
      }
    }



  public void readFromFile( String fileName )
    {
    try
    {
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

      // Just look at US county records.
      // In Louisiana a Parish is like a county.
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
    // mApp.showStatus( "Finished processing the file." );
    // mApp.showStatus( " " );
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
