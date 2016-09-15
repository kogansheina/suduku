package suduku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.* ;

class mainSquare extends JInternalFrame 
{
	static final long serialVersionUID = 101L;
	private static final int ENTER_LOCATION_BEFORE_END = 0 ;
	private static final int ENTER_LOCATION_AFTER_END = 4 ;
	private static final int ENTER_NUMBER_BEFORE_END = 1 ;
	private static final int ENTER_NUMBER_AFTER_END = 5 ;
	private static final int ENTER_END = 2 ;
	private static final int SOLVE = 3 ;

	private static final int DELETE_OPTION = 0 ;
	private static final int CLEAR_OPTION = 2 ;
	private static final int QUIT_OPTION = 3 ;
	private static final int SOLVE_OPTION = 0 ;
	private static final int PLAY_OPTION = 1 ;
	private static final int FROM_FILE = 4 ;
	private static final int TO_FILE = 5 ;
	private static String filename = null;
	private static String filenameO = null ;
	private static String tempfile = null ;	

	private int HINT_OPTION ;
    private int END ;

    public boolean trace = true ;
    public boolean fulltrace = false ;
	public int dimX ;
	public int dimY ;
	public int DimY ;
	public int DimX ;
	
	private int N ;		
	private FileOutputStream output = null;
	private FileInputStream input = null;
	private byte game [] ;
	private Graphics g;                     
	private int forbidden [] ;
	private int f = 0;
	private square initial[][];
	private square solution[][];
	private pp coordinates [][];
    private int I = 0;
    private int J = 0;
    private Point pm;
    private Point ppm;
	private JDesktopPane desktop ;
    private int state = ENTER_LOCATION_BEFORE_END;
    private String module = "mainSquare" ;
    private pp previous ;
		
	public mainSquare ( int N, JDesktopPane top, mainWindow mw, int dimX, int dimY )
    {		
		super ( ) ;
        desktop = top ;
        this.N = N ; 
        END = N * N + 1 ;
        HINT_OPTION = END + 1 ;
        this.dimX = dimX ;
        this.dimY = dimY ;
        DimY = dimY - dimY/4;
	    DimX = dimX/3;	       
        setBackground( Color.gray ) ;
        setResizable( false );
        pack(); 
        setVisible(true);		
		g = mw.getGraphics();
        setSize ( dimX * N * N + 2 * dimX, dimY * N * N + 2 * dimY ) ;
        Rectangle bb = getBounds();        
        setLocation( bb.x, bb.y );
        game = new byte [ N * N * N * N ];
        filename = suduku.file_path + suduku.file_separator + "games" + N ;
        filenameO = suduku.file_path + suduku.file_separator + "gamesSave" + N ;
        tempfile = suduku.file_path + suduku.file_separator + "gamesTemp" + N ; 
	}
	public void doMainSquare( )
	{
        initial = new square [ N ][ N ] ;
        solution = new square [ N ][ N ] ;
        forbidden = new int [ N*N ] ;
        coordinates = new pp [ N ][ N ] ;
        int x = dimX;
        int y = dimY ;
        for ( int k = 0; k < N ; k ++ )
        {
	        for ( int kk = 0; kk < N ; kk ++ )
	        {
		        initial [ k ][ kk ] = new square ( k, kk, N, x, y, this ) ;
		        solution [ k ][ kk ] = new square ( k, kk, N, x, y, this ) ;
		        coordinates [ kk ][ k ] = new pp ( x, y ) ;
		        x += dimX*N;
	        }
	        y += N*dimY;
	        x = dimX;
        } 
        Font font = new Font( "Serif", Font.PLAIN, 3*dimY/4 ) ;
        g.setFont ( font );
        paint();
    	g.setColor ( Color.black );
        Rectangle bb = getBounds();
        myInput inputPanel = new myInput( bb.x + bb.width, bb.y, this, N );
        inputPanel.setVisible(true);
        desktop.add( inputPanel );
        Rectangle bbb = inputPanel.getNormalBounds();
        myOptions optionsPanel = new myOptions( bb.x + bb.width + bbb.width + 5, bb.y, this );
        optionsPanel.setVisible(true);
        desktop.add( optionsPanel );
        pm = new Point ();
        ppm = new Point ();
        pm.x = 3 * dimX / 2 ;
        pm.y = 3 * dimY / 2 ;
        ppm.x = pm.x ;
        ppm.y = pm.y ;
        previous = new pp ( ) ;
        previous = getXY ( pm.y, pm.x ) ;
        initial[previous.x][previous.y].setCurrent( pm.x , pm.y, g, false ) ;
    }
    public void notify ( int keyCode )
    {
	    if ( keyCode == KeyEvent.VK_UP ) 
	    {
		    pm.y -= dimY ;
		    setNewLocation( ) ;
	    }
	    if ( keyCode == KeyEvent.VK_DOWN ) 
	    {
		    pm.y += dimY ;   
		    setNewLocation( ) ;
	    }		    
	    if ( keyCode == KeyEvent.VK_RIGHT )
	    {
		     pm.x += dimX ;
		     setNewLocation( ) ;
	    }
	    if ( keyCode == KeyEvent.VK_LEFT ) 
	    {
		    pm.x -= dimX ;
		    setNewLocation( ) ;
	    }
	    if ( keyCode == KeyEvent.VK_NUMPAD1 ) 
	    {
		    myInputNotify ( 1 ) ;
	    }
	    if ( keyCode == KeyEvent.VK_NUMPAD2 ) 
	    {
		    myInputNotify ( 2 ) ;
	    }
	    if ( keyCode == KeyEvent.VK_NUMPAD3 ) 
	    {
		    myInputNotify ( 3 ) ;
	    }	    
	    if ( keyCode == KeyEvent.VK_NUMPAD4 ) 
	    {
		    myInputNotify ( 4 ) ;
	    }
	    if ( keyCode == KeyEvent.VK_NUMPAD5 ) 
	    {
		    myInputNotify ( 5 ) ;
	    }
	    if ( keyCode == KeyEvent.VK_NUMPAD6 ) 
	    {
		    myInputNotify ( 6 ) ;
	    }
	    if ( keyCode == KeyEvent.VK_NUMPAD7 ) 
	    {
		    myInputNotify ( 7 ) ;
	    }
	    if ( keyCode == KeyEvent.VK_NUMPAD8 ) 
	    {
		    myInputNotify ( 8 ) ;
	    }
	    if ( keyCode == KeyEvent.VK_NUMPAD9 ) 
	    {
		    myInputNotify ( 9 ) ;
	    }
	    	    	    	    	    
		requestFocusInWindow(); 
    }
    public void notify ( Point pm ) 
    {
	    this.pm = pm ;
        setNewLocation ( ) ;
    }
    private void setNewLocation( )
    {
	    int xx = pm.x ;
	    int yy = pm.y ;
        pp sp = new pp();
        sp = getXY(yy,xx);
        I = sp.x;
        J = sp.y ;
        if ( ( I != -1 ) && ( J != -1 ) )
        {	    
	    	switch ( state ) 
	    	{
        	case ENTER_LOCATION_BEFORE_END:
//        	System.out.println("pm " + pm.x+" " +pm.y+" ppm"+ppm.x+" "+ppm.y );
        		if ( ( pm.x != ppm.x ) || ( pm.y != ppm.y ) )
	    		{
		    		initial[previous.x][previous.y].retCurrent( ppm.x, ppm.y, g, false ) ;
	    		}
	    		initial[I][J].setCurrent( xx, yy, g, false ) ;
             	break;
        	case ENTER_LOCATION_AFTER_END:
        	    if ( ( ppm.x != pm.x ) || ( pm.y != ppm.y ) )
	    		{
		    		initial[previous.x][previous.y].retCurrent( ppm.x, ppm.y, g, true ) ;
	    		}
	    		initial[I][J].setCurrent( xx, yy, g, true ) ;
             	break;     
	    	}
	    	ppm.x = pm.x ;
	    	ppm.y = pm.y ; 
	    	previous = getXY ( ppm.y, ppm.x ) ;
    	}   	
    }
    public void notify ( int ii, Object obj )
    {   
	    Class cls = obj.getClass();
	    Package pk = cls.getPackage();

        if ( cls.getName().equals(pk.getName() + ".myInput") )
        {
	        myInputNotify ( ii ) ;
        }
        if ( cls.getName().equals(pk.getName() + ".myOptions" ) )
        {
	        myOptionsNotify ( ii ) ;
        }
        requestFocusInWindow(); 
    }
    private void myInputNotify ( int ii )
    {
		boolean error = false;
        if ( pm != null )
        {
                int xx = pm.x;
                int yy = pm.y; 
	            switch ( state ) 
	            {
		        case ENTER_LOCATION_BEFORE_END:   
                    if ( ii ==  END ) 
                    {     
                        endInitial();
                        state = ENTER_END ;
                    }
                    else
                    {
	                	if ( ( I != -1 ) && ( J != -1 ) )
	                	{
                			switch ( ii )
                			{
                			case DELETE_OPTION:
	                		 	initial [I][J].delInitial( xx, yy, g ) ;	
                				break;
                			default:
                        		initial [I][J].delInitial( xx, yy, g ) ;
                        		initial [I][J].setInitial( ii, xx, yy ) ;
                                if ( ! initial[I][J].isCorrect( false ) ) error = true ;
                        		if ( ! lineIsCorrect ( I, J, false ) ) error = true ;
                        		if ( ! columnIsCorrect ( I, J, false ) ) error = true ;
         	    				if ( error ) initial [I][J].drawValue( ii, xx, yy, g, Color.red, false ) ; 
         	    					else initial [I][J].drawValue( ii, xx, yy, g, Color.black, false );              	                		
            					break;                    
                    		}
                		}
            		}
            		break;
		        case ENTER_LOCATION_AFTER_END: 
		        	if ( ( I != -1 ) && ( J != -1 ) )
		        	{                                         
                    	if ( ii == HINT_OPTION )
                     	{
                             int val = solution [I][J].getHintValue( xx, yy );
                             initial [I][J].setValue( val, xx, yy, g ) ;
                             initial [I][J].drawValue( val, xx, yy, g, Color.blue, true );
                        }
                        else
                        {
                			switch ( ii )
                			{
                			case DELETE_OPTION:
		                		initial [I][J].delValue( xx, yy, g ) ;              		
                    			break;               
                    		default:
                              	initial [I][J].delValue( xx, yy, g ) ;
                              	initial [I][J].setValue( ii, xx, yy, g ) ;
                                if ( ! initial[I][J].isCorrect( true ) ) error = true ;
                        		if ( ! lineIsCorrect ( I, J, true ) ) error = true ;
                        		if ( ! columnIsCorrect ( I, J, true ) ) error = true ;
         	    				if ( error ) initial [I][J].drawValue( ii, xx, yy, g, Color.red, true ) ; 
         	    				   else initial [I][J].drawValue( ii, xx, yy, g, Color.green, true );
         	    				if ( last() && isCorrect() )
         	    				{
		    						JOptionPane.showMessageDialog( new Frame(), "Congratulation !!", "", JOptionPane.WARNING_MESSAGE); 
	    						}
                              	break;
                          	}
                         } 
                     } 
                     break;         
            	}    
          }
      }
      private void myOptionsNotify ( int ii )
      {

			switch ( state )
			{
			case ENTER_END:	
            	switch ( ii )
            	{
                case CLEAR_OPTION:// clear
                	for ( int t = 0; t < N; t ++ )
                    {
                        for ( int u = 0; u < N; u ++ )
                        {
                            initial[t][u].retInitial( g, true );
                        }
                    }
                    clear();
                    state = ENTER_LOCATION_BEFORE_END;
                    break;
                case TO_FILE:
                	saveGame();
                    break;    
                case SOLVE_OPTION: // solve
                	state = SOLVE;
                	Solve( false );
                	break;
                case PLAY_OPTION:
                	state = ENTER_LOCATION_AFTER_END;               	
                	break;
                case QUIT_OPTION:
                	exit ( false, false );
                	break;
            	} // end state
            	break;
            case SOLVE:
            	switch ( ii )
            	{
                case TO_FILE:
                	saveGame();
                    break;    	            	
	            case QUIT_OPTION:
	            	exit ( false, false );
                	break;
                case CLEAR_OPTION:
                	for ( int t = 0; t < N; t ++ )
                    {
                        for ( int u = 0; u < N; u ++ )
                        {
                            initial[t][u].retInitial( g, true );
                        }
                    }
                    clear();
                    state = ENTER_LOCATION_BEFORE_END;
                	break;
            	}	// solve state
            	break;
            case ENTER_LOCATION_AFTER_END:
            case ENTER_NUMBER_AFTER_END:
                switch ( ii )
            	{
                case TO_FILE:
                	saveGame();
                    break;    	            	
	            case QUIT_OPTION:
	            	exit ( false, false );
                	break;
                case CLEAR_OPTION:
                	for ( int t = 0; t < N; t ++ )
                    {
                        for ( int u = 0; u < N; u ++ )
                        {
                            initial[t][u].retInitial( g, false );
                        }
                    }
                    clear();
                	break;
                case SOLVE_OPTION:
                	for ( int t = 0; t < N; t ++ )
                    {
                        for ( int u = 0; u < N; u ++ )
                        {
                            initial[t][u].retInitial( g, false );
                        }
                    }
                    clearBeforeSolve();
                    state = SOLVE ;
                    Solve( false );
                	break;
				}
            	break;
            case ENTER_LOCATION_BEFORE_END:
            case ENTER_NUMBER_BEFORE_END:
                switch ( ii )
            	{
	            case FROM_FILE :
	            	fillGame();
	            	break;
	            case QUIT_OPTION:
	            	exit ( false, false );
                	break;
                case CLEAR_OPTION:
                	for ( int t = 0; t < N; t ++ )
                    {
                        for ( int u = 0; u < N; u ++ )
                        {
                            initial[t][u].retInitial( g, true );
                        }
                    }
                    clear();
                    state = ENTER_LOCATION_BEFORE_END;
                	break; 
            	}               	
            }


    }
    private boolean last()
    {
	    boolean found = false;
	    for ( int k = 0; k < N; k ++ )
	    {
		    for ( int kk = 0; kk < N; kk ++ )
		    {
			    if ( initial[k][kk].freePlace() != null)
			    {
				    found = true;
				    break;
			    }
			    if ( found ) break ;
		    }
	    }
	    return ( ! found );
    }
    public void paint()
    {
        for ( int kk = 0; kk < N ; kk ++ )
        {     
        	for ( int k = 0; k < N; k ++ )
        	{
	        	switch ( state )
	        	{
		        	case ENTER_LOCATION_BEFORE_END:
		        	case ENTER_NUMBER_BEFORE_END:
	        			initial [k][kk].drawSquare ( g, false );
	        			break;
	        		case ENTER_END:
	        		case SOLVE:
	        		case ENTER_LOCATION_AFTER_END:
	        		case ENTER_NUMBER_AFTER_END:
	        			initial [k][kk].drawSquare ( g, true );
	        			break;
        		}		
        	}
    	}
	}

    public void Solve ( boolean forhint )
    {
		int x = 0;
		int y = 0;
        boolean firstround = true ;
        pp freeplace = null;
        pp pplace = null;
        boolean fatal = false ;
        for ( x = 0; ( x < N ) && ! fatal; x ++  )
        {
	        for ( y = 0; ( y < N ) && ! fatal; y ++  )
	        {
                if ( firstround ) freeplace = initial[x][y].freePlace();
        		if ( doSquare ( freeplace, x, y ) )
        		{
	        		initial[x][y].variants ++;
                    if ( trace )
                    {
                        suduku.writelnLog( module,"Square (" + x + "," + y + ") finished "+ initial[x][y].variants );
                        initial[x][y].printSquare(false);               
                    }
                    
                    if ( ! initial[x][y].isCorrect( true ) ) 
                    {
	                    exit ( true, false ) ;
	                    fatal = true ;
                    }
                    if ( ! lineIsCorrect ( x, y, true ) ) 
                    {
	                    exit ( true, false ) ;
	                    fatal = true ;
                    }
                    if ( ! columnIsCorrect ( x, y, true ) )
                    {
	                     exit ( true, false ) ;
	                     fatal = true ;
                     }
                    /* update the 'forbidden' list of all locations of all
                    others squares along the replaced lines and columns */
                    firstround = true ;
				}
				else
				{	
                    if ( trace && firstround )
                    {
                        suduku.writelnLog( module,"Square (" + x + "," + y + ") failed " + initial[x][y].failure );
                    }
                    freeplace = null ;
                    /* a square failed to be finished */
                    /* look for a previous square and its possible free place */
                    while ( ( freeplace == null ) && ! fatal )
                    {
                        /* take the previous sqaure */
					    pplace = previous ( x, y );
                        if ( trace && ! firstround )
                        {
                            suduku.writelnLog( module,"Square (" + x + "," + y + ") failed ( not first round ) " + initial[x][y].failure );
                        }
                        /* look from the end to start, for a possible free place */
                        freeplace = retSquare( pplace.x, pplace.y ) ;
                        if ( freeplace == null )
                        {
                            /* there is no free place */
                            if ( ( pplace.x == 0 ) && ( pplace.y == 0 ) )
                            {
                                /* it is the first square !!!*/
                                suduku.writelnLog( module,"I AM DEAD !!!" );
                                exit( true, false );
                                fatal = true ;
                            }
                            else
                            {
                                if ( fulltrace )
                                {
                                    suduku.writelnLog( module,"Fail to return " + pplace.x + " " + pplace.y );
                                }
                                /* look futher for a previous square */
                                x = pplace.x;
                                y = pplace.y;
                            }
                        }
                    }
                    firstround = false ;
                    if ( fulltrace )
                    {
                        suduku.writelnLog( module,"Previous square : " + pplace.x + " " + pplace.y );
					    suduku.writelnLog( module,"Previous place : " + freeplace.x + " " + freeplace.y );
                    }
                    /* because of the 'for' function must put x and y in 
                    such a manner, that after increment we'll receive the correct
                    found square */
					if ( ( pplace.x == 0 ) && ( pplace.y == 0 ) )	
					{					
						x = -1;
						y = N;
					}
					else
					{	
						pplace = previous ( pplace.x, pplace.y );
						y = pplace.y;
						x = pplace.x;
					}
				}
			}
		}
		if ( fatal )
		{
			JOptionPane.showMessageDialog( new Frame(), "No solution - clear, and try again", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
        	if ( ! forhint )
        	{
	        	paint( );
	    	}
        	else
        	{
 //           	suduku.writelnLog( "","Solution");
            	for ( int k = 0; k < N; k ++ )
            	{
                	for ( int kk = 0; kk < N; kk ++ )
                	{
                    	solution[k][kk].copy ( initial[k][kk] );
//                    	solution[k][kk].printSquare(false);
                	}
            	}
        	}
    	}
    }
    public int getX ( int i, int j )
    {
	    return ( coordinates [j][i].x + dimX/2 );
    }
    public int getY ( int i, int j )
    {
	    return ( coordinates [j][i].y + dimY/2 );
    }
	public pp getXY ( int yy, int xx )
    {
	    pp sp = new pp();
	    for ( int kk = 0; kk < N; kk ++ )
	    {
	    	for ( int k = N-1; k >= 0; k -- )
	    	{
    			if ( yy > coordinates[k][kk].y ) 
	    		{
		    		sp.y = k ;
		    		if ( xx > coordinates[k][kk].x ) 
		    		{
			    		sp.x = kk ;
						break;
					}
	    		}
    		}
		}

	    return ( sp ); 
    }    

    private void retForbbiden( int line, int column )
    {
        for ( int l = 0; l < N; l ++ )
        {		        
	        f = 0;
		    for ( int inc = 0; inc < N; inc ++ )
		    {
                int q = initial[line][column].getLocation ( l, inc ).val ;
                if ( q != -1 )
                {
                   	forbidden [ f++ ] = q ;
                }
			}	
			/* at this point 'forbidden' contains the values to be forbid for all
			* the other squares - on a line */
       		for ( int ll = 0; ll < N; ll ++ )
       		{
	       		/* go to all squares with the same 'line' and column != current */
          		if ( ll != column )
          		{
              		for ( int v = 0; v < f; v ++ )
              		{

                      initial[line][ll].forbidLine ( forbidden [v], l );
               		}
           		}
       		}
    	}			        
        for ( int l = 0; l < N; l ++ )
        {		        
	        f = 0;
		    for ( int inc = 0; inc < N; inc ++ )
		    {
                int q = initial[line][column].getLocation ( inc, l ).val ;
                if ( q != -1 )
                {
                   	forbidden [ f++ ] = q ;
                }
			}	
			/* at this point 'forbidden' contains the values to be forbid for all
			* the other squares - on a column */
       		for ( int ll = 0; ll < N; ll ++ )
       		{
	       		/* go to all squares with the same 'column' and  line != current */
          		if ( ll != line )
          		{
              		for ( int v = 0; v < f; v ++ )
              		{

                      initial[ll][column].forbidColumn ( forbidden [v], l );
               		}
           		}
       		}
    	}     
    }
   
	private pp previous ( int x, int y )
	{
		if ( y > 0 )
		{
			y -- ;
		}
		else
		{
			if ( x > 0 )
			{
				y = N - 1;
				x -- ;
			}
		}
		return ( new pp( x,y ) );
	}
    private void oneSquareLine ( boolean start, int x, int y, int line )
    {

       /* forbidden contains all the defined values of a line 'line' 
       go to all squares with the same x as sq, besides square with y = current y
       and add the forbidden to all columns on 'line' */

       for ( int l = 0; l < N; l ++ )
       {
          if ( l != y )
          {
              for ( int v = 0; v < f; v ++ )
              {
                 if ( start )
                      initial[x][l].forbidInitialL ( forbidden [v], line );
                 else
                 {
                      initial[x][l].forbidLine ( forbidden [v], line );
                 }
               }
           }
       }
    }
    private void oneSquareColumn ( boolean start, int x, int y, int column )
    {
    
       /* forbidden contains all the defined values of a column 'column' 
       go to all squares with the same y as sq, besides square with x = current y
       and add the forbidden to all lines on 'column' */

       for ( int l = 0; l < N; l ++ )
       {
          if ( l != x )
          {
              for ( int v = 0; v < f; v ++ )
              {
                 if ( start )
                      initial[l][y].forbidInitialC ( forbidden [v], column );
                 else
                 {
                      initial[l][y].forbidColumn ( forbidden [v], column ); 
                 }               
              }
          }
       }           
	}	
    private void oneSquareInitial ( int x, int y )
    {
	    square sq = initial[x][y] ;
	    for ( int line = 0; line < N; line ++ )
	    {
        	f = 0;
			for ( int l = 0; l < N; l ++ )
            {
                int q = sq.getLocationInitial ( line, l ).val ;
                if ( q != -1 )
                {
                   forbidden [ f++ ] = q ;
                }
            }	        	
        	oneSquareLine ( true, x, y, line );
    	}
        for ( int column = 0; column < N; column ++ )
        {        
			f = 0;
            for ( int l = 0; l < N; l ++ )
            {
                int q = sq.getLocationInitial ( l, column ).val ;
                if ( q != -1 )
                {
                   forbidden [ f++ ] = q ;
                }
            }			
        	oneSquareColumn ( true, x, y, column );
    	}
    }
    private void clearBeforeSolve()
    {
		for ( int x = 0; x < N; x++ )
		{
			for ( int y = 0; y < N; y++)
			{
                initial[x][y].clearBeforeSolve ();
                initial[x][y].forbidR ();
                initial[x][y].clearReplaced();
            }
		}	    
    }
    private void clear()
    {
		for ( int x = 0; x < N; x++ )
		{
			for ( int y = 0; y < N; y++)
			{
                initial[x][y].clear ();
                initial[x][y].clearForbidden ();
                initial[x][y].clearReplaced();
            }
		}
    }
	private void endInitial()
	{
		for ( int x = 0; x < N; x++ )
		{
			for ( int y = 0; y < N; y++)
			{
                oneSquareInitial ( x, y ) ;
                initial[x][y].endInitial();
            }
		}
        clearBeforeSolve();
        suduku.writelnLog("","Initial sate");
        printAll(false);
        Solve( true );
        clearBeforeSolve();
	}
	private void printAll ( boolean all )
	{
		for ( int x = 0; x < N; x++ )
		{
			for ( int y = 0; y < N; y++)
			{
				initial[x][y].printSquare(all);
			}
		}		
	}
    private boolean isCorrect()
    {
        for ( int x = 0; x < N; x ++ )
        {
            for ( int y = 0; y < N; y ++ )
            {
                if ( ! initial[x][y].isCorrect( true ) ) return ( false );
            }
        }
        if ( ! lineIsCorrect ( N-1, N-1, true ) ) return ( false ) ;
        if ( ! columnIsCorrect ( N-1, N-1, true) ) return ( false ) ;
        return ( true );
    }
    private boolean columnIsCorrect ( int X, int Y, boolean end )
    {
        int temp [] = new int [N*N];
        int t = 0;
        /* loop on squares' columns */
        for ( int y = 0; y <= Y; y ++ )
        {
            /* loop on columns of a square */
            for ( int c = 0; c < N; c ++ )
            {
                t = 0;
                /* loop on lines of a square along a column */
                for ( int l = 0; l < N; l ++ )
                {
                    /* loop on squares' lines */
                    for ( int x = 0; x <= X; x ++ )
                    {
                        int vv ;
                        if ( end ) vv = initial[x][y].getLocation(l,c).val ;
                            else vv = initial[x][y].getLocationInitial(l,c).val ;
                        if ( vv != -1 )
                          temp [ t++] = vv ;
                    }
                }
                for ( int i = 0; i < t - 1; i ++ )
                {
                    for ( int r = i + 1 ; r < t; r ++ )
                    {
                        if ( temp [ r ] == temp [ i ] ) 
                        {
                            suduku.writelnLog( module,"Column " + c + " failed" );
                            for ( int k=0; k < t; k++)
                            {
	                            suduku.writeLog(""," " + temp[k]);
                            }
                            suduku.writelnLog();                            
                            return ( false ) ;
                        }
                    }
                }
                /* the column is o.k. */
            }
        }
        return ( true );
     }
    private boolean lineIsCorrect ( int X, int Y, boolean end )
    {
        int temp [] = new int [N*N];
        int t = 0;

        /* loop on squares' lines */
        for ( int x = 0; x <= X; x ++ )
        {
            /* loop on lines of a square */
            for ( int l = 0; l < N; l ++ )
            {
                t = 0;
                /* loop on columns of a square along a line */
                for ( int c = 0; c < N; c ++ )
                {
                    /* loop on squares' columns */
                    for ( int y = 0; y <= Y; y ++ )
                    {
                        int vv ;
                        if ( end ) vv = initial[x][y].getLocation(l,c).val ;
                            else vv = initial[x][y].getLocationInitial(l,c).val ;
                        if ( vv != -1 )
                          temp [ t++] = vv ;
                    }
                }
                for ( int i = 0; i < t - 1; i ++ )
                {
                    for ( int r = i + 1 ; r < t; r ++ )
                    {
                        if ( temp [ r ] == temp [ i ] ) 
                        {
                            suduku.writelnLog(module,"Line " + l + " failed for " + x + "("+X+","+Y+")");
                            for ( int k=0; k < t; k++)
                            {
	                            suduku.writeLog(""," " + temp[k]);
                            }
                            suduku.writelnLog();
                            return ( false ) ;
                        }
                    }
                }
                /* the line is o.k. */
            }
        }
       return ( true );
    }
	public void exit( boolean stop, boolean onlyexit )
	{
		if ( ! onlyexit )
		{
            if ( isCorrect() )
            {
                if ( stop )
                {
                    suduku.writelnLog( module,"Final state - CORRECT !!!!");
                    printAll(false);
                }
                else
                {
                    suduku.writelnLog( module,"Temporary state - CORRECT !!!!");
                }
            }
            else
            {
                if ( stop )
                {
                    suduku.writelnLog( module,"Final state - NOT correct !!!!");
                    printAll(true);
                }
                else
                {
                    suduku.writelnLog( module,"Temporary state - NOT correct !!!!");
                    printAll(true);
                }
            }
		}
		else printAll ( stop );
        if ( ! stop )
        {
            if ( suduku.session != null )
            {
                try
                {
                   suduku.session.close();
                }
                catch ( IOException e )
                {
                }
            }
            if ( input != null )
            {
	            try
	            {
            		input.close();
                }
                catch ( IOException e )
                {
                }
            }     
            if ( output != null )
            {
	            try
	            {
            		output.close();
            		if ( input == null )
            		{
	            		FileInputStream fi = new FileInputStream ( filenameO );
	            		FileOutputStream fo = new FileOutputStream ( filename ) ;
	            		if ( ( fi != null ) && ( fo != null ) )
	            		{
	            			byte n = ( byte )fi.read();
	            			if ( n != -1 ) fo.write ( n );
	            			n = ( byte )fi.read(game,0,N*N*N*N);
	            			while ( n != -1 )
	            			{		            			
		            			fo.write(game,0,N*N*N*N);
		            			n = ( byte )fi.read(game,0,N*N*N*N);		           			
	            			}
	            			fi.close();
	            			fo.close();
            			}
            		}
            		else
            		{
	            		FileInputStream fi = new FileInputStream ( filename );
	            		FileOutputStream fo = new FileOutputStream ( tempfile ) ;
	            		if ( ( fi != null ) && ( fo != null ) )
	            		{
		            		byte n = ( byte )fi.read();
	            			if ( n != -1 ) fo.write ( n );
	            			n = ( byte )fi.read(game,0,N*N*N*N);
	            			while ( n != -1 )
	            			{		            			
		            			fo.write(game,0,N*N*N*N);
		            			n = ( byte )fi.read(game,0,N*N*N*N);		           			
	            			}
	            			fi.close();
							fi = new FileInputStream ( filenameO );
							n = ( byte )fi.read();
							if ( n != -1 ) n = ( byte )fi.read(game,0,N*N*N*N);
							while ( n != -1 )
	            			{		            			
		            			fo.write(game,0,N*N*N*N);
		            			n = ( byte )fi.read(game,0,N*N*N*N);		           			
	            			}
							fi.close();
							fo.close();
	            		fi = new FileInputStream ( tempfile );
	            		fo = new FileOutputStream ( filename ) ;
	            		if ( ( fi != null ) && ( fo != null ) )
	            		{
	            			n = ( byte )fi.read();
	            			if ( n != -1 ) fo.write ( n );
	            			n = ( byte )fi.read(game,0,N*N*N*N);
	            			while ( n != -1 )
	            			{		            			
		            			fo.write(game,0,N*N*N*N);
		            			n = ( byte )fi.read(game,0,N*N*N*N);		           			
	            			}
	            			fi.close();
	            			fo.close();
            			}							
						}
            		}
                }
                catch ( IOException e )
                {
                }
            }                          		
		    System.exit(0);
        }
	}
	private boolean doSquare( pp freeplace, int line, int column )
	{
        square sq = initial[line][column] ;
        pp temp = sq.getFirstFree();
        /* loop until no pree place into the square OR all the places are 'dead' */
        if ( fulltrace )
            suduku.writelnLog( module,"doSquare : " + line + " " + column + " start place " + freeplace.x + " " + freeplace.y ) ;
        while ( freeplace != null )
        {  
           if ( fulltrace )
                suduku.writelnLog( module,"doSquare : new free place " + freeplace.x + " " + freeplace.y+ " " + freeplace.val ) ;
           if ( ! sq.IsDeadEnd( freeplace ) )
           {
               /* the location is not 'dead', means exist in freelist at least
               a value which is not in forbidden or replaced
               */
               /* get a possible value for this location */
               int no = sq.getLocation ( freeplace ).getFreeNumber();
               /* return the previous value - if any */
               sq.returnFree ( freeplace  );
               if ( freeplace.val != -1 )
               {
                   if ( fulltrace )
                       suduku.writelnLog(module,"doSquare : replace " + freeplace.x + " " + freeplace.y + " " + freeplace.val ) ;
                   updateForbidden ( line, column, freeplace );
               }
               /* change the value of the place */
               freeplace.val = no;
               /* take out the new value from the free list and set the location's value */
               sq.markNotFree ( freeplace ) ;
               /* store the place into previous places stack */
               if ( fulltrace )
                  suduku.writelnLog(module,"doSquare : mark " + freeplace.x + " " + freeplace.y + " with " + no ) ;
               sq.setFreePlace( freeplace );
               /* get the next free place into the square */
               freeplace = sq.freePlace();
           }
           else
           {  
               /* the location is 'dead'; 
               it must be cleared and the previous location will be considered */
               /* clear the 'already replaced' values - except the first free location
               of the square (0,0) */
              if ( ( line != 0 ) || ( column != 0 ) )
              {
                  sq.clearReplaced ( freeplace ) ;
              }
              else
              {
                  if ( ( freeplace.x != temp.x ) || ( freeplace.y != temp.y ) )
                  {
                      sq.clearReplaced ( freeplace ) ;
                  }
              }
              /* return the value to the free list */
              sq.returnFree ( freeplace  );
              /* if it is the first free place into the square => the entire 
              square is 'dead', mark failure and return */
              if ( ( freeplace.x == temp.x ) && ( freeplace.y == temp.y ) )
              {
              	  sq.failure ++;
              	  /* the previous returned value must be cleared from all the
              	  possible influenced squares */
              	  retForbbiden( line, column );              	  
                  if ( fulltrace )
                  {
                      suduku.writelnLog(module,"doSquare : return false " + freeplace.x + " " + freeplace.y ) ;
//                      initial[line][column].printFree();
//                      initial[line][column].getLocation(freeplace.x, freeplace.y).printLocation();
                  }

              	  return ( false );
              }
              else
              {
                    if ( freeplace.val != -1 )
                    {
                        updateForbidden ( line, column, freeplace );
                    }
                    if ( fulltrace )
                    {
                        suduku.writelnLog(module,"doSquare : dead replace " + freeplace.x + " " + freeplace.y + " " + freeplace.val + " " + temp.x + " " + temp.y ) ;
                        initial[line][column].printFree();
                        initial[line][column].getLocation(freeplace.x, freeplace.y).printLocation();
                    }
	          	    /* previous place in square */
              	    freeplace = sq.previousPlace();
              }
            }
        }
        /* the square is 'done' */
        /* the previous returned value must be cleared from all the
        possible influenced squares */
        retForbbiden( line, column );

		return ( true ) ;
    }
	private pp retSquare( int line, int column )
    {
       square sq = initial[line][column] ;
       /* take the last filled place into the square */
       pp freeplace = sq.previousPlace();
       pp temp = sq.getFirstFree();
       while ( freeplace != null )
       {
       	   if ( fulltrace )
       	   {
           	   suduku.writelnLog(module,"retSquare " + line + " " + column + " starting from " + freeplace.x + " " + freeplace.y+ " " + freeplace.val ) ;	       
       	   }
           if ( ! sq.IsDeadEnd( freeplace ) ) 
           {
               /* the place is a good candidate */
               /* return the previous value - if any */
               sq.returnFree ( freeplace  );
               if ( freeplace.val != -1 )
               {
                   updateForbidden ( line, column, freeplace );
               }
               break;
           }
           else 
           {
               /* the location is 'dead'; 
               it must be cleared and the previous location will be considered */
               /* clear the 'already replaced' values - except the first free location
               of the square (0,0) */
               if ( ( line != 0 ) || ( column != 0 ) )
               {
                   sq.clearReplaced ( freeplace ) ;
               }
               else
               {
                   if ( ( freeplace.x != temp.x ) || ( freeplace.y != temp.y ) )
                   {
                       sq.clearReplaced ( freeplace ) ;
                   }
               }
               /* the place is a good candidate */
               /* return the previous value - if any */
               sq.returnFree ( freeplace  );
               if ( freeplace.val != -1 )
               {
                   updateForbidden ( line, column, freeplace );
               }
               freeplace = sq.previousPlace() ;
           }
       }
        /* the previous returned value must be cleared from all the
        possible influenced squares */
	   retForbbiden( line, column ); 
       return ( freeplace ) ;
    }
	
/*********************************************************************/    	
    private void addToForbidden ( int val )
    {
        if ( val != -1 )
        {
            boolean found = false ;
            for ( int ff = 0; ff < f; ff ++ )
            {
                if ( forbidden [ ff ] == val ) found = true;
            }
            if ( ! found ) forbidden [ f++ ] = val;
        }
    }
    private boolean alreadyForbidden ( int val )
    {
        boolean found = false ;
        for ( int ff = 0; ff < f; ff ++ )
        {
            if ( forbidden [ ff ] == val )
            {
                found = true;
                break;
            }
        }
        return ( found ) ;
    }
    private void updateForbidden ( int x, int y, pp place )
    {
        /* go along all the squares of the same line, besides the
        current square */
        for ( int sqcol = 0; sqcol < N; sqcol ++ )
        {
            if ( sqcol != y )
            {
                /* I am in a square to be updated in place.x , place.y
               build the forbidden from the square of the same column and
               all the other lines, along place.y column into every square */
               for ( int colinsq = 0; colinsq < N; colinsq ++ )
               {
                  f = 0;
                  for ( int othersq = 0; othersq < N; othersq ++ )
                  {
                  
                     if ( othersq != x )
                     {
                       for ( int lineofcol = 0; lineofcol < N; lineofcol ++ )
                       {
                           addToForbidden ( initial[othersq][sqcol].getLocation(lineofcol,colinsq).val ) ;
                       }
                     }
                  }
                  /* check if place.val exist in forbidden - in no; it must be took off from 
                  the list of the current location */
                  if ( ! alreadyForbidden ( place.val ) )
                  {
                       initial[x][sqcol].getLocation( place.x, colinsq ).retForbidden ( place.val ) ;
                  }
               }
            }
        }
        /* go along all the squares of the same column, besides the
        current square */
        for ( int sqline = 0; sqline < N; sqline ++ )
        {
            if ( sqline != x )
            {
                /* construct the 'forbidden' along the line c, from
                squares of all line besides the current, and line l */
                for ( int lineinsq = 0; lineinsq < N; lineinsq ++ )
                {
                   f = 0;
                   for ( int othersq = 0; othersq < N; othersq ++ )
                   {

                      if ( othersq != y )
                      {
                        for ( int colofline = 0; colofline < N; colofline ++ )
                        {
                            addToForbidden ( initial[sqline][othersq].getLocation(lineinsq,colofline).val ) ;
                        }
                      }
                   }
                   if ( ! alreadyForbidden ( place.val ) )

                   {
                       initial[sqline][y].getLocation( lineinsq, place.y ).retForbidden ( place.val ) ;
                   }
                }
            }
        }
    }
    private void fillGame()
    {
	    try
	    {
		    int n = -1;
			if ( input == null )
	    	{
		    	input = new FileInputStream ( filename ) ;
	    		if ( input != null )
	    		{
            		n = input.read ( );
            		if ( n == -1 )
					{
						JOptionPane.showMessageDialog( new Frame(), "the file is over - next 'file' will read the first game", "", JOptionPane.WARNING_MESSAGE);
						input.close();
						input = null ;
					}
					else
					{
		        		if ( n != N ) 
		        		{
			        		JOptionPane.showMessageDialog( new Frame(), "the file is for games with " + N +" dimension", "", JOptionPane.WARNING_MESSAGE);
		        		}
		        		readGame();
	        		}
        		}
    		}
		    else
		    {
			    readGame();
		    }
	    }
    	catch ( IOException e )
    	{
    	}	    
    } 
    private void readGame ()
    {
	    int n = -1;
	    
	    try
	    {
    	n = input.read( game, 0 , N * N * N * N ) ;
		if ( n == -1 )
		{
			JOptionPane.showMessageDialog( new Frame(), "the file is over - next 'file' will read the first game", "", JOptionPane.WARNING_MESSAGE);
			input.close();
			input = null ;
		}
		else
		{
			for ( int t = 0; t < N; t ++ )
        	{
            	for ( int u = 0; u < N; u ++ )
            	{
                  	initial[t][u].clearFile(g);
            	}
        	}
			for ( int x = 0; x < N ; x ++ )
			{
				 for ( int y = 0; y < N; y ++ )
				 {
					 for ( int l = 0; l < N ; l ++ )
					 {
						 for ( int c = 0; c < N; c ++ )
						 {
							 int index = ( x * N + y ) * ( N * N ) + ( l * N + c ) ;
							 int val = ( int ) game [ index ] ;
							 initial [x][y].drawValueFromFile ( val, l, c, g ) ;
						 }
					 }
				 }
			}
			endInitial();	    			            
            state = ENTER_END ;
		}
	    }
    	catch ( IOException e )
    	{
    	}	    						            	
    }

    private void saveGame()
    {  
	    try
	    {
	    if ( output == null )
        {
	        output = new FileOutputStream ( filenameO ) ;
	        if ( output != null )
            {
	             byte n = ( byte )N ;
	             output.write( n ) ;
            }
        }
    	if ( output != null )
        {
	    	for ( int x = 0 ; x < N; x ++ )
	        {
		    	for ( int y = 0; y < N ; y ++ )
		        {
			    	for ( int l = 0 ; l < N ; l ++ )
			    	{
				    	for ( int c = 0; c < N ; c ++ )
				    	{
					    	int index = ( x * N + y ) * ( N * N ) + ( l * N + c ) ;
			                game [ index ] = ( byte )initial [x][y].getLocationInitial ( l, c ).val ;
                		}
                	}
            	}
        	}
        	output.write( game, 0, N * N * N * N ) ;		       
    	}
	}
    catch ( IOException e )
    {
    }	    
	}
}

