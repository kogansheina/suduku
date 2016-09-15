package suduku;
import java.util.* ;
import java.io.* ;

class suduku 
{
	public static FileWriter session = null;
	private static Properties SP;
	public static String file_separator ;
	public static String file_path ;
	
	public suduku ( int N, String name )
	{
		SP = System.getProperties();
        file_separator = SP.getProperty( "file.separator" ) ;
        file_path = SP.getProperty( "user.dir" ) ;		
		if ( ! name.equals(""))
        {
            openLogs( name );
        }
		new mainWindow ( N );
	}

    private void openLogs( String s )
    {
        try
        {
            session = new FileWriter ( s ) ;
        }
        catch ( IOException e )
        {
            System.out.println("No Log file open");
        }
    }
	public static void writeLog ( String module, String s )
	{
		if ( session != null )
		{
			try
			{
				if ( module.equals(""))
				{
					session.write ( s );					
				}
				else
				{
					session.write ( module + " : " + s );
				}
			}
			catch ( IOException e )
			{
				System.out.println("Write exception " + e );
			}
		}
/*
		else 
        {
            if ( module.equals(""))
            {
                System.out.print ( s );					
            }
            else
            {
                System.out.print ( module + " : " + s );
            }
        }
*/        
	}
	public static void writelnLog ( String module, String s )
	{
		if ( session != null )
		{
			try
			{
				if ( module.equals(""))
				{				
					session.write ( s + "\n" );
				}
				else
				{
					session.write ( module + " : " + s + "\n" );					
				}
			}
			catch ( IOException e )
			{
				System.out.println("Write exception " + e );
			}
		}
/*
		else 
        {
            if ( module.equals(""))
            {				
                System.out.println ( s + "\n" );
            }
            else
            {
                System.out.println ( module + " : " + s + "\n" );					
            }
        }
 */       
	}	
	public static void writelnLog ( )
	{
		if ( session != null )
		{
			try
			{
				session.write ( " \n" );
			}
			catch ( IOException e )
			{
				System.out.println("Write exception " + e );
			}
		}
//		else System.out.println();
	}    	
/************************************************************************/
//                                                                      //
//////                            MAIN                             ///////
//                                                                      //
/************************************************************************/
    public static void main( String[] args )
    {
        String name = "";
        int n = 3 ;
        boolean error = false;

        String [] help =
        {
            "Parameters may be : ",
            "   -f < file name > : logs file",
            "   -n < number of squares ( default 3 )>",
        };
        if ( args.length == 0 )
        {
             for ( int k = 0; k < help.length; k ++ )
             {
                 System.out.println ( help [ k ] );
             }
         }
         else
         {
             for ( int k = 0; k < args.length; k ++ )
             {
                 int who = 0;
                 String st = args [ k ];
                 if ( st.equals ( "-f" ) )
                 {
                     who = 2;
                 }
                 if ( st.equals ( "-n" ) )
                 {
                     who = 1;
                 }
                 switch ( who )
                 {
                 case 2:
                     if ( args.length > k )
                     {
                         name = args [ ++k ];
                     }
                     else
                     {
                         System.out.println ( "wrong parameter for '-f' option" );
                         error = true;
                     }
                     break;
                 case 1:
                     n = Integer.parseInt ( args [ ++k ] );
                     break;
                default:
                    System.out.println ( "wrong parameter " + st );
                    error = true;
                    break;
                }
                 if ( error ) System.exit ( 0 );
             }
        }
        new suduku( n, name );
//        sk.start();
    } // main
}

