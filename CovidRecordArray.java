// Copyright Eric Chauvin 2020.


public class CovidRecordArray
  {
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



  }
