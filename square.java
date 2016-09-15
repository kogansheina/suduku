package suduku;
import java.awt.*;
import javax.swing.*;

class square 
{
    public int failure = 0;
    public int variants = 0;
	public int idX, idY ;
    public int freelistInitial [];
    public int freelistsize ;
    public int freelist [] ;

	private location initial[][];
	private location initialSave[][];
	private int N ;
    private pp placeList [];
    private int placeIndex ;
    private String module = "square";
    private pp firstFreePlace ;
    private mainSquare parent;
	private pp coordinates [][];
	private int myX, myY ;
	private int I, J ;
				
	public square ( int IDX, int IDY, int n, int X, int Y, mainSquare parent )
	{
		N = n ;
		idX = IDX ;
		idY = IDY ;
		myX = X ;
		myY = Y ;
        this.parent = parent;
        coordinates = new pp [ N ] [ N ] ;          
        initial = new location [ N ][ N ] ;
        initialSave = new location [ N ][ N ] ;
        freelist = new int [ N*N ];
        freelistInitial = new int [ N*N ];
        placeList = new pp [ N*N ];
        freelistsize = 0 ;
        placeIndex = 0;
        for ( int kk = 0; kk < N ; kk ++ )
        {
	        for ( int k = 0; k < N ; k ++ )
	        {
		        initial [ kk ][ k ] = new location ( n, kk, k, this ) ;
		        initialSave [ kk ][ k ] = new location ( n, kk, k ) ;
		        coordinates [ kk ] [ k ] = new pp ( k * parent.dimX, kk * parent.dimY ) ;
	        }
        }
	}
    private boolean freeInSquare ( int val )
    {
        for ( int t = 0; t < freelistsize; t ++ )
        {
            if ( freelist [ t ] == val ) return ( false );
        }
        return ( true );
    }
    private boolean InitialfreeInSquare ( int val )
    {
        for ( int t = 0; t < freelistsize; t ++ )
        {
            if ( freelistInitial [ t ] == val ) return ( true );
        }
        return ( false );
    }

    /* return the first value is in the freelist */
    public int getFreeNumber ( int t )
    {
        return ( freelist [ t ] ) ;
    }
    public int getFreeNumberIndex ( int v )
    {
        for ( int t = v + 1; t < freelistsize; t ++ )
        {
            if ( freelist [ t ] != -1 )
            {
                return ( t ) ;
            }
        }
        return ( -1 );
    }
    /* take off 'val' from the free list */
    public void markNotFree ( pp place )
    {
        for ( int t = 0; t < freelistsize; t ++ )
        {
            if ( freelist [ t ] == place.val )
            {
               freelist [ t ] = -1 ;
               break;
            }
        }
        getLocation( place).val = place.val ;
        getLocation( place).letter = Color.green ;
    }

    public void delInitial ( int cx, int cy, Graphics g )
    {
       
       J = getX( cx - myX );
       I = getY( cy - myY );		
      
       if ( ( initialSave[I][J].val != -1 ) && ( ( I !=-1 ) && ( J !=-1 ) ) )
       {
           g.setColor ( initialSave[I][J].letterB );
           Integer j = new Integer ( initialSave[I][J].val );	        		
           g.drawString ( j.toString(), coordinates[I][J].x+myX+parent.DimX, coordinates[I][J].y+myY+parent.DimY );
       	   initialSave [I][J].val = -1 ;
   	   }
    }
    public void delValue ( int cx, int cy, Graphics g )
    {

       J = getX( cx - myX );
       I = getY( cy - myY );		
      
       if ( ( initial [I][J].val != -1 ) && ( ( I !=-1 ) && ( J !=-1 ) ) )
       {
           g.setColor ( initial[I][J].letterB );
           Integer j = new Integer ( initial [I][J].val );	        		
           g.drawString ( j.toString(), coordinates[I][J].x+myX+parent.DimX, coordinates[I][J].y+myY+parent.DimY );
       	   initial [I][J].val = -1 ;
   	   }
    }
    public void clearFile( Graphics g)
    {
       for ( I = 0; I < N; I ++ )
       {
           for ( J = 0; J < N; J ++ )
           {
               if ( initialSave[I][J].val != -1 )
               {
                   g.setColor ( Color.yellow );
                   Integer j = new Integer ( initialSave[I][J].val );	        		
                   g.drawString ( j.toString(), coordinates[I][J].x+myX+parent.DimX, coordinates[I][J].y+myY+parent.DimY );	           
               }
           }
       }	    
    }
    public void retInitial ( Graphics g, boolean end )
    {
       for ( I = 0; I < N; I ++ )
       {
           for ( J = 0; J < N; J ++ )
           {
               if ( ! end )
               {
                   if ( ( initialSave [I][J].val == -1 ) && ( initial[I][J].val != -1 ) )
                   {
                       g.setColor ( Color.yellow );
                       initial[I][J].letter = Color.black;
                       Integer j = new Integer ( initial[I][J].val );	        		
                       g.drawString ( j.toString(), coordinates[I][J].x+myX+parent.DimX, coordinates[I][J].y+myY+parent.DimY );
                   }
               }
               else
               {
                   if ( initial [I][J].val != -1 )
                   {
                       g.setColor ( Color.yellow );
                       initialSave[I][J].letter = Color.black;
                       Integer j = new Integer ( initial[I][J].val );
                       g.drawString ( j.toString(), coordinates[I][J].x+myX+parent.DimX, coordinates[I][J].y+myY+parent.DimY );
                   }
               }
           }
       }
       if ( end ) clearBeforeSolve();
    }
    public void setInitial ( int val, int cx, int cy )
    {
        J = getX( cx - myX );
        I = getY( cy - myY );
        if ( ( I !=-1 ) && ( J !=-1 ) )
        {
            initialSave [I][J].val = val ;
        }
	}
    public void drawValueFromFile ( int val, int cx, int cy, Graphics g )
	{
		initialSave [cx][cy].val = val ;
		initialSave [cx][cy].letter = Color.black ;
        g.setColor( Color.black );
        if ( val != -1 )
        {
        	Integer j = new Integer ( val );
        	g.drawString ( j.toString(), coordinates[cx][cy].x+myX+parent.DimX, coordinates[cx][cy].y+myY+parent.DimY );	
    	}	
	}
    public void drawValue ( int val, int cx, int cy, Graphics g, Color c, boolean end )
    {
        J = getX( cx - myX );
        I = getY( cy - myY );
        if ( ( I !=-1 ) && ( J !=-1 ) && ( val != -1 ) )
        {
            if ( end )
                initial [I][J].letter = c;
            else initialSave [I][J].letter = c;
            g.setColor( c );
            Integer j = new Integer ( val );
            g.drawString ( j.toString(), coordinates[I][J].x+myX+parent.DimX, coordinates[I][J].y+myY+parent.DimY );
        }
    }
    public int getHintValue ( int cx, int cy )
    {
        J = getX( cx - myX );
        I = getY( cy - myY );
        if ( ( I !=-1 ) && ( J !=-1 ) )
        {
            return ( initial[I][J].val );
        }
        return ( -1 );
    }
    public void setValue ( int val, int cx, int cy, Graphics g )
    {
        J = getX( cx - myX );
        I = getY( cy - myY );
        if ( ( I !=-1 ) && ( J !=-1 ) )
        {
            initial [I][J].val = val ;
        }
	}
	public void setCurrent ( int x, int y, Graphics g, boolean end )
	{
		g.setColor ( Color.pink ) ;
		J = getX( x - myX );
        I = getY( y - myY );
        if ( end )
        {
            initial [I][J].letterB = Color.pink;		        
        }
        else
        {
            initialSave [I][J].letterB = Color.pink;
        }
		int X = myX + J*parent.dimX ;
		int Y = myY + I*parent.dimY ;
        g.fillRect ( X + 3, Y + 3, parent.dimX - 3, parent.dimY - 3 ) ;
        if ( end )
        {
            if ( initial [I][J].val != -1 )
            {
                g.setColor ( initial [I][J].letter ) ;        		
                Integer j = new Integer ( initial [I][J].val );
                g.drawString ( j.toString(), coordinates[I][J].x+myX+parent.DimX, coordinates[I][J].y+myY+parent.DimY );
            }
        }
        else
        {
            if ( initialSave [I][J].val != -1 )
            {
                g.setColor ( initialSave [I][J].letter ) ;        		
                Integer j = new Integer ( initialSave [I][J].val );
                g.drawString ( j.toString(), coordinates[I][J].x+myX+parent.DimX, coordinates[I][J].y+myY+parent.DimY );
            }
        }
    }
    public void retCurrent ( int x, int y, Graphics g, boolean end )
	{
		g.setColor ( Color.yellow ) ;
		J = getX( x - myX );
        I = getY( y - myY );
//        System.out.println("previous : "+x+" " +y + " I="+I+" J="+J);
        if ( end )
        {
            initial [I][J].letterB = Color.yellow;		        
        }
        else
        {
            initialSave [I][J].letterB = Color.yellow;
        }
		int X = myX + J*parent.dimX ;
		int Y = myY + I*parent.dimY ;
        g.fillRect ( X + 3, Y + 3, parent.dimX - 3, parent.dimY - 3 ) ;
        if ( end )
        {
            if ( initial [I][J].val != -1 )
            {
                g.setColor ( initial [I][J].letter ) ;        		
                Integer j = new Integer ( initial [I][J].val );
                g.drawString ( j.toString(), coordinates[I][J].x+myX+parent.DimX, coordinates[I][J].y+myY+parent.DimY );
            }
        }
        else
        {
            if ( initialSave [I][J].val != -1 )
            {
                g.setColor ( initialSave [I][J].letter ) ;        		
                Integer j = new Integer ( initialSave [I][J].val );
                g.drawString ( j.toString(), coordinates[I][J].x+myX+parent.DimX, coordinates[I][J].y+myY+parent.DimY );
            }
        }
    }

	public void drawSquare( Graphics g, boolean end )
	{
        int x = myX ;
        int y = myY ;
        for ( int k = 0; k < N ; k ++ )
        {     
        	for ( int kk = 0; kk < N; kk ++ )
        	{
	        	g.setColor ( initial[k][kk].letterB ) ;
        		g.fillRect ( x, y, parent.dimX, parent.dimY ) ;
	        	g.setColor ( Color.black ) ;
        		g.drawRect ( x, y, parent.dimX, parent.dimY ) ;	                	
        		x += parent.dimX ;
                if ( end )
                {
                    if ( initial [k][kk].val != -1 )
                    {	
                        g.setColor ( initial [k][kk].letter ) ;        		
                        Integer j = new Integer ( initial[k][kk].val );
                        g.drawString ( j.toString(), coordinates[k][kk].x+myX+parent.DimX, coordinates[k][kk].y+myY+parent.DimY );
                    }
                }
                else
                {
                    if ( initialSave [k][kk].val != -1 )
                    {	
                        g.setColor ( initialSave [k][kk].letter ) ;        		
                        Integer j = new Integer ( initialSave[k][kk].val );
                        g.drawString ( j.toString(), coordinates[k][kk].x+myX+parent.DimX, coordinates[k][kk].y+myY+parent.DimY );
                    }
                }
    		}
    		x = myX;
    		y += parent.dimY;
    	}
    	g.setColor ( Color.magenta ) ;
        g.drawRect ( myX, myY, parent.dimX*N, parent.dimY*N ) ;	
        g.drawRect ( myX+2, myY+2, parent.dimX*N, parent.dimY*N ) ;	
	}
    public int retX ( int i )
    {
	    return( 3 / 2 * parent.dimX + myX ) ;
    }
	public int retY ( int i )
    {
	    return( 3 / 2 * parent.dimY  + myY ) ;
    }
    private int getX ( int yy )
    {
	    for ( int k = 0; k < N; k ++ )
	    {
    		if ( ( yy >= k*parent.dimY ) && ( yy < (k+1)*parent.dimY ) )
	    	{
				return(k);
	    	}
    	}
	    return ( -1 ); 
    }
    private int getY ( int xx )
    {
	    for ( int k = 0; k < N; k ++ )
		{
     		if ( ( xx >= k*parent.dimX ) && ( xx < (k+1)*parent.dimX ) )
     		{
	     		return(k);
     		}
 		}
 		return(-1);
	}
	public void endInitial ( )
	{
        for ( int i = 1; i <= N*N; i ++ )
        {
            boolean found = false ;
		    for ( int k = 0; k < N ; k ++ )
		    {
		    	for ( int kk = 0; kk < N; kk ++ )
		    	{
		    		if ( initialSave[k][kk].val == i )
		    		{
                        found = true ;
                        break;
		    		}
		    	}
                if ( found ) break;
            }
            if ( ! found )
            {
                freelistInitial [ freelistsize ] = i ;
                freelist [ freelistsize ++ ] = i ;
            }
        }
	}
	public pp freePlace ()
	{
		for ( int x = 0; x < N; x++ )
		{
			for ( int y = 0; y < N; y++ )
			{
				if ( initial[x][y].val == -1 )
				{					
					return ( new pp ( x, y ) );
				}
			}
		}
		return ( null );
	}
    public pp getFirstFree ()
    {
        return( firstFreePlace ) ;
    }
	public void setFreePlace ( pp place )
	{
		placeList [ placeIndex ++ ] = place; 
	}
	public boolean IsDeadEnd ( int l, int c )
    {
        /* check every value from free list */
        return ( getLocation( l, c ).IsDeadEnd( ) ) ;
    }
	public boolean IsDeadEnd ( pp place )
    {
        /* check every value from free list */
       /* at least one free location is not stucked */
         return ( getLocation( place.x, place.y ).IsDeadEnd( ) ) ;
    }
	public pp previousPlace( )
	{
		pp place = null;
		if ( placeIndex > 0 )
		{
			place = placeList [ --placeIndex ] ;
            returnFree ( place );
    	}
	    return ( place );
    }

	public void clearReplaced( pp place )
	{    
		getLocation ( place ).clearReplaced();
	}
	public void clearReplaced( )
	{   
        for ( int x = 0; x < N; x ++ )
        {
            for ( int y = 0; y < N; y ++ )
            {
                getLocation (x,y).clearReplaced();
            }
        }
	}
    /* return a value to the free list */
	public void returnFree( pp place )
	{    
		int v = place.val ;
        /* clear the location */
		getLocation ( place ).val = -1 ;
        /* if it is legal value */
        if ( v != -1 )
        {
            /* if it is not already into the free list and 
               it is a free value from the beginnin, means it is NOT an
               initial value */

            if ( InitialfreeInSquare( v ) && freeInSquare ( v ) )
            {
                /* put it into the first free place into the free list */
                for ( int x = 0; x < freelistsize; x ++ )
                {
                    if ( freelist [ x ] == -1 )
                    {
                        freelist [ x ] = v;
                        break;
                    }
                }
            }
        }
	}	
	public boolean forbidLine ( int val, int l )
	{
	
		for ( int c = 0; c < N; c ++ )
		{	
			boolean found = false;
			for ( int t = 0; ( t < N ) && ! found; t ++ )
			{
				if ( initial[l][t].val != -1 )
				{
					if ( initial[l][t].val == val ) found = true ;
				}
			}			
			if ( ! found ) initial[l][c].setForbidden ( val ) ;
		}
		return(true);
	} 
	public boolean forbidColumn ( int val, int c )
	{
		
        for ( int l = 0; l < N; l ++ )
        {	 
	        boolean found = false;
			for ( int t = 0; ( t < N ) && ! found; t ++ )
			{
				if ( initial[t][c].val != -1 )
				{
					if ( initial[t][c].val == val ) found = true ;
				}
			}			
			if ( ! found ) initial[l][c].setForbidden ( val ) ;
        }
        return(true);
	}
	public void clearForbidden ()
	{
		for ( int l = 0; l < N; l ++ )
		{
            for ( int c = 0; c < N; c ++ )
            {
                initial[l][c].clearForbidden () ;
            }
		}
	} 	 
    /* return to every location its initial forbidden */
	public void forbidR ()
	{
		for ( int l = 0; l < N; l ++ )
		{
            for ( int c = 0; c < N; c ++ )
            {
                initial[l][c].setForbiddenR () ;
            }
		}
	} 	 	
    
    public void clearBeforeSolve ()
    {
		for ( int l = 0; l < N; l ++ )
		{
            for ( int c = 0; c < N; c ++ )
            {
                initial[l][c].val = initialSave[l][c].val ;
                initial[l][c].letter = initialSave[l][c].letter ;
            }
		}
        for ( int l = 0; l < freelistsize; l ++)
        {
            freelist [ l ] = freelistInitial[ l ] ;
        }
		placeIndex = 0;
		firstFreePlace = freePlace ();		
    }
	public void clear ()
    {
		for ( int l = 0; l < N; l ++ )
		{
            for ( int c = 0; c < N; c ++ )
            {
                initial[l][c].val = -1 ;
                initial[l][c].letter = Color.black ;
                initialSave[l][c].val = -1 ;
                initialSave[l][c].letter = Color.black ;               
            }
		}
		freelistsize = 0;
		placeIndex = 0 ;
	}
	public void forbidInitialL ( int val, int place )
	{
        for ( int y = 0; y < N; y ++ )
        {
		    initial[place][y].setForbiddenInitial ( val ) ;
        }
	}
	public void forbidInitialC ( int val, int place )
	{
        for ( int x = 0; x < N; x ++ )
        {
		    initial[x][place].setForbiddenInitial ( val ) ;
        }
	}
    public location getLocation ( int x, int y )
    {
        return ( initial [x][y] ) ;
    }
    public location getLocationInitial ( int x, int y )
    {
        return ( initialSave [x][y] ) ;
    }
    public location getLocation ( pp place )
    {
        return ( initial [place.x][place.y] ) ;
    }
	public void printFree( )
    {
        writeNoLog ("free : " );
        for ( int k = 0; k < freelistsize; k ++ )
        {
           writeNoLog ( freelist [ k ] + " " ) ;
        }
        suduku.writelnLog();
    }
	public void printSquare( boolean full )
	{
		writelnLog( idX+" "+idY + " successfully variants " + variants + " failures " + failure );
        for ( int k = 0; k < N; k ++ )
        {
            for ( int kk = 0; kk < N; kk ++ )
            {
                writeNoLog( initial [k][kk].val + " " );
            }
            suduku.writelnLog();
        }
        suduku.writelnLog();
        if ( full )
        {
            printFree();
            for ( int k = 0; k < N; k ++ )
            {
                for ( int kk = 0; kk < N; kk ++ )
                {
                    initial [k][kk].printLocation( );
                }
                suduku.writelnLog();
            }
        }
	}
	private void writeNoLog ( String s )
	{
		suduku.writeLog( "", s ) ;
	}	
	private void writeLog ( String s )
	{
		suduku.writeLog( module, s ) ;
	}
	private void writelnLog ( String s )
	{
		suduku.writelnLog ( module, s ) ;
	}
    public boolean isCorrect ( boolean end )
    {
        int temp [] = new int [N*N];
        int t = 0;
        for ( int t1 = 0; t1 < N; t1 ++ )
        {
            for ( int t2 = 0; t2 < N; t2 ++ )
            {
                if ( end ) temp [ t ++ ] = initial [ t1 ] [ t2 ].val ;
                    else temp [ t ++ ] = initialSave [ t1 ] [ t2 ].val ;
            }
        }
        
        for ( int r = 0 ; r < t; r ++ )
        {
            if ( temp [ r ] != -1 )
            {
                boolean found = false ;
                for ( int i = 0 ; i < t ; i ++ )
                {
                    if ( i != r )
                    {
                        if ( temp [ r ] == temp [ i ] )
                        {
                            found = true;
                            break;
                        }
                    }
                }
                if ( found ) return ( false );
            }
        }
        return ( true ) ;
    }
    public void copy( square orig )
    {
        for ( int kk = 0; kk < N ; kk ++ )
        {
	        for ( int k = 0; k < N ; k ++ )
	        {
		        initial [ kk ][ k ].val = orig.getLocation( kk, k ).val ;
		        initial [ kk ][ k ].letter = orig.getLocation( kk, k ).letter ;
            }
        }
    }
}   
