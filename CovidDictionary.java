// Copyright Eric Chauvin 2020.



public class CovidDictionary
  {
  private MainApp mApp;
  private CovidDictionaryLine lineArray[];
  private CovidRecord[] covidRecArray;
  private int[] sortIndexArray;
  private int arrayLast = 0;
  private final int maxIndexLetter = 'z' - 'a';
  private final int keySize = ((maxIndexLetter << 6) |
                                  maxIndexLetter) + 1;




  private CovidDictionary()
    {
    }



  public CovidDictionary( MainApp useApp )
    {
    mApp = useApp;

    lineArray = new CovidDictionaryLine[keySize];
    covidRecArray = new CovidRecord[8];
    sortIndexArray = new int[8];
    resetSortIndexArray();
    }



  public void clear()
    {
    for( int count = 0; count < keySize; count++ )
      lineArray[count] = null;

    }



  private void resetSortIndexArray()
    {
    // It's not to arrayLast.  It's to the whole length.
    int max = sortIndexArray.length;
    for( int count = 0; count < max; count++ )
      sortIndexArray[count] = count;

    }



  private void resizeDisplayArrays( int toAdd )
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

      mApp.showStatus( rec.makeKeysValuesString());
      }
    }



  public void addDisplayRecord( CovidRecord rec )
    {
    if( arrayLast >= sortIndexArray.length )
      resizeDisplayArrays( 1024 );

    covidRecArray[arrayLast] = rec;
    arrayLast++;
    }



  private int letterToIndexNumber( char letter )
    {
    letter = Character.toLowerCase( letter );
    int index = letter - 'a';
    if( index < 0 )
      index = 0;

    // A letter that's way up there in Unicode, like
    // a Chinese character, would be given the value
    // maxindexletter.  In other words anything higher
    // than the letter z is lumped together with z.
    if( index > maxIndexLetter )
      index = maxIndexLetter;

    return index;
    }



  private int getIndex( String key )
    {
    // This index needs to be in sorted order.

    int keyLength = key.length();
    if( keyLength < 1 )
      return 0;

    // The letter z by itself would be sorted before
    // the two letters az unless the space character
    // is added.
    if( keyLength == 1 )
      key = key + " ";

    int one = letterToIndexNumber( key.charAt( 0 ));
    int tempTwo = letterToIndexNumber( key.charAt( 1 ));
    int two = (one << 6) | tempTwo;

    if( two >= keySize )
      two = keySize - 1;

    return two;
    }



  public void updateRecord( String key, CovidRecord value )
    {
    try
    {
    if( key == null )
      return;

    key = key.trim().toLowerCase();
    if( key.length() < 1 )
      return;

    int index = getIndex( key );

    if( lineArray[index] == null )
      lineArray[index] = new CovidDictionaryLine();

    lineArray[index].updateRecord( key, value );
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in updateRecord()." );
      mApp.showStatus( e.getMessage() );
      }
    }



  public CovidRecord getRecord( String key )
    {
    if( key == null )
      return null;

    key = key.trim().toLowerCase();
    if( key.length() < 1 )
      return null;

    int index = getIndex( key );
    if( lineArray[index] == null )
      return null;

    return lineArray[index].getRecord( key );
    }


/*
  public void sort()
    {
    // This is a Library Sort mixed with a Bubble
    // Sort for each line in the array.
    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      lineArray[count].sort();
      }
    }
*/



  public void showKeysValues()
    {
    try
    {
    // sort();

    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      mApp.showStatus( lineArray[count].
                            makeKeysValuesString());

      }
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in showKeysValues():" );
      mApp.showStatus( e.getMessage() );
      }
    }


  public void setDisplayArray()
    {
    try
    {
    arrayLast = 0;
    for( int count = 0; count < keySize; count++ )
      {
      if( lineArray[count] == null )
        continue;

      for( int countL = 0; countL < 2000000000; countL++ )
        {
        CovidRecord rec = lineArray[count].
                                getRecordAt( countL );
     
        if( rec == null )
          break;

        addDisplayRecord( rec );
        }
      }
    }
    catch( Exception e )
      {
      mApp.showStatus( "Exception in setDisplayArray():" );
      mApp.showStatus( e.getMessage() );
      }
    }



  public void showDisplayRecords()
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

      mApp.showStatus( rec.makeKeysValuesString());
      }
    }



  public boolean keyExists( String key )
    {
    if( key == null )
      return false;

    key = key.trim().toLowerCase();
    if( key.length() < 1 )
      return false;

    int index = getIndex( key );
    if( lineArray[index] == null )
      return false;

    return lineArray[index].keyExists( key );
    }



  public void readAllRecordsFile( String fileName )
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

    mApp.showStatus( fileName );
    mApp.showStatus( " " );

    StringArray sArray = new StringArray();
    int max = sArray.makeFieldsFromString( dataS,
                                           '\n' );

    int totalRecords = 0;
    for( int count = 0; count < max; count++ )
      {
      String line = sArray.getStringAt( count );
      if( line.contains( "FIPS,Admin2,Province_State," ))
        continue;

      CovidRecord rec = new CovidRecord( mApp );
      if( !rec.setFieldsFromFileLine( line ))
        {
        mApp.showStatus( "Set fields: " + line );
        return;
        }
      
      // Newer updates to the data sometimes don't
      // have FIPS codes.

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

      totalRecords++;
      updateRecord( rec.CombinedKey, rec );
      }

    // mApp.showStatus( " " );
    // Wikipedia says there are "3,142 counties
    // and county-equivalents".
    mApp.showStatus( "There are 3,142 counties." );
    mApp.showStatus( "Total records: " + totalRecords );
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



  public void readUpdateFile( String fileName )
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

    mApp.showStatus( fileName );
    mApp.showStatus( " " );

    StringArray sArray = new StringArray();
    int max = sArray.makeFieldsFromString( dataS,
                                           '\n' );

    int totalRecords = 0;
    for( int count = 0; count < max; count++ )
      {
      String line = sArray.getStringAt( count );
      if( line.contains( "FIPS,Admin2,Province_State," ))
        continue;

      CovidRecord rec = new CovidRecord( mApp );
      if( !rec.setFieldsFromFileLine( line ))
        {
        mApp.showStatus( "Set fields: " + line );
        return;
        }

      if( !keyExists( rec.CombinedKey ))
        continue;
      
      totalRecords++;
      updateRecord( rec.CombinedKey, rec );
      }

    mApp.showStatus( "Total records: " + totalRecords );
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



  public void sortByConfirmed()
    {
    if( arrayLast < 2 )
      return;

    for( int count = 0; count < arrayLast; count++ )
      {
      if( !bubbleSortOnePassConfirmed() )
        break;

      }
    }



  private boolean bubbleSortOnePassConfirmed()
    {
    // This returns true if it swaps anything.

    boolean switched = false;
    for( int count = 0; count < (arrayLast - 1); count++ )
      {
      if( covidRecArray[sortIndexArray[count]].
             Confirmed < covidRecArray[
               sortIndexArray[count + 1]].Confirmed )
        {
        int temp = sortIndexArray[count];
        sortIndexArray[count] = sortIndexArray[count + 1];
        sortIndexArray[count + 1] = temp;
        switched = true;
        }
      }

    return switched;
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



  }
