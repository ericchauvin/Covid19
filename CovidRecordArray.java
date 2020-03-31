// Copyright Eric Chauvin 2020.



// Do a previous data file and the change amounts.


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
Sort by change, by totals... whatever.
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

    int totalDeaths = 0;
    for( int count = 0; count < max; count++ )
      {
      String line = sArray.getStringAt( count );
      if( line.contains( "FIPS,Admin2,Province_State," ))
        continue;

      CovidRecord rec = new CovidRecord( mApp );
      if( !rec.setFieldsFromFileLine( line ))
        continue;
      
      if( rec.FIPS.length() == 0 )
        continue;

      totalDeaths += rec.Deaths;
      if( rec.Province_State.equals( "Colorado" ))
        mApp.showStatus( rec.makeShowString());

      addRecord( rec );
      }

    mApp.showStatus( " " );
    mApp.showStatus( "Total deaths: " + totalDeaths );

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
