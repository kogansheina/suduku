package suduku;
import java.awt.*;

class location
{
	public int val ;
	private int N ;
    private int idX, idY ;
	private int forbidden [];
	private int forbiddenIndex ;
	private int forbiddenInitial [];
	private int forbiddenInitialIndex ;
	private int replaced [];
	private int replacedIndex ;
	private String module = "location";
    private square parent ;
    public Color letter ;
    public Color letterB ;
		
	public location( int n, int x, int y, square parent )
	{
		N = n ;
		val = -1;
        idX = x;
        idY = y ;
        this.parent = parent;
		forbidden = new int [N*N];
		forbiddenInitial = new int [N*N];
		replaced = new int [N*N];
        forbiddenInitialIndex = 0;
		forbiddenIndex = 0;		
		replacedIndex = 0;
        letter = Color.black;
        letterB = Color.yellow ;
	}
	public location( int n, int x, int y )
	{
		N = n ;
		val = -1;
        idX = x;
        idY = y ;
        parent = null;
	}
	public void setForbiddenInitial ( int val )
	{
		boolean found = false ;
		for ( int y = 0; y < forbiddenInitialIndex; y ++ )
		{
			if ( forbiddenInitial [ y ] == val ) found = true;
		}
		if ( ! found )	forbiddenInitial [ forbiddenInitialIndex ++ ] = val ;
	}
    public int getFreeNumber ( )
    {  
       int v = parent.getFreeNumberIndex( -1 );
       while ( v != -1 )
       {
            int value = parent.getFreeNumber ( v );
            if ( ! isForbidden( value ) ) 
            {
                if ( ! isReplaced( value ) )
                {
                    replaced [ replacedIndex ++ ] = value;
                    /* it is a candidate to replace*/
                    return ( value ) ;
                }
            }
			v = parent.getFreeNumberIndex( v );
        }
        return( -1 );
    }
	public void setForbidden ( int val )
	{
		boolean found = false ;

		for ( int y = 0; y < forbiddenIndex; y ++ )
		{
			if ( forbidden [ y ] == val )
            {
                found = true;
                break;
            }
		}
		if ( ! found )	forbidden [ forbiddenIndex ++ ] = val ;
	}
	public void retForbidden ( int val )
	{
		boolean found = false ;
        int y ;
		for ( y = 0; y < forbiddenIndex; y ++ )
		{
			if ( forbidden [ y ] == val )
            {
                found = true;
                break;
            }
		}
		if ( found ) 
        {
            for ( int yy = y; yy < forbiddenIndex - 1; yy ++ )
            {
                forbidden [ yy ] = forbidden [ yy + 1 ] ;
            }
            forbiddenIndex -- ;
        }
	}
	public void clearForbidden()
	{
		forbiddenInitialIndex = 0;
		forbiddenIndex = 0;
	}
	public void setForbiddenR ( )
	{
        forbiddenIndex = 0;
		for ( int y = 0; y < forbiddenInitialIndex; y ++ )
		{
			forbidden [ forbiddenIndex ++ ] = forbiddenInitial[ y ] ;
		}
	}
	private boolean isForbidden ( int val )
	{
		for ( int y = 0; y < forbiddenIndex; y ++ )
		{
			if ( forbidden [ y ] == val )
			{
				return ( true );
			}
		}
		return ( false );
	}
	private boolean isReplaced ( int val )
	{
		for ( int y = 0; y < replacedIndex; y ++ )
		{
			if ( replaced [ y ] == val )
			{
				return ( true );
			}
		}
		return ( false );
	}
	/*
	 a location is 'dead' if any of free is in the sum of
	already replaced and forbidden list
	*/
    public boolean IsDeadEnd ()
    {
        int temp [] = new int [ N*N ];
        int t = 0;
        for ( int x = 0; x < forbiddenIndex; x ++ )
        {
            if ( forbidden [ x ] != -1 )  
            {
                temp [ t ++ ] = forbidden [ x ] ;
            }
        }
        for ( int x = 0; x < replacedIndex; x ++ )
        {
            if ( replaced [ x ] != -1 )
            {
                boolean found = false ;
                for ( int tt = 0 ; tt < t ; tt ++ )
                {
                    if ( temp [ tt ] == replaced [ x ] )
                    {
                        found = true;
                        break;
                    }
                }
                if ( ! found )
                {
                    temp [ t ++ ] = replaced [ x ] ;
                }
            }
        }
        for ( int y = 0; y < parent.freelistsize; y ++ )
        {
            if ( parent.freelist [ y ] != -1 ) 
            {
                boolean found = false;
                for ( int x = 0; x < t; x ++ )
                {
                    if ( temp [ x ] == parent.freelist [ y ] ) 
                    {
                       found = true;
                       break;
                    }
                }
                /* not found => there is - at least - one free to try it */
                if ( ! found ) return ( false );
            }
        }
        return ( true );
    }
    
    public void clearReplaced()
    {
    	replacedIndex = 0 ;
    }
    public void printLocation()
    {
        writeLog( idX + " " + idY + " : " + val + "\nreplaced : " );
        for ( int y = 0 ; y < replacedIndex; y ++ )
        {
            writeNoLog( replaced [ y ] + " " );
        }
        writeNoLog("forbidden : " );
        for ( int y = 0 ; y < forbiddenIndex; y ++ )
        {
            writeNoLog( forbidden [ y ] + " " );
        }
        suduku.writelnLog();
    }
 	private void writeNoLog ( String s )
	{
		suduku.writeLog( "", s ) ;
	}   
	private void writeLog ( String s )
	{
		suduku.writeLog( module, s ) ;
	}
}