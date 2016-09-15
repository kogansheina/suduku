package suduku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class mainWindow extends JFrame implements KeyListener
{

	static final long serialVersionUID = 100L;
	static final int DIM = 30 ;
	private mainSquare msq ;
	int dimX = 42 ;
	int dimY = 42 ;
	int DimY = dimY - dimY/4;
	int DimX = dimX/3;
	private mainWindow mySelf ;
			
	public mainWindow ( int N )
	{		
		super ( "SUDUKU" ) ;  
		mySelf = this ;    
		setLayout ( new FlowLayout ( ) ) ;
        enableEvents ( AWTEvent.WINDOW_EVENT_MASK ) ;
        //Set up the GUI.
        JDesktopPane desktop = new JDesktopPane(); 
        setContentPane(desktop);
        desktop.setOpaque(true);
        setLocation( DIM, DIM );
        int mainsizeX = dimX * N * N + 2 * dimX ;
        int mainsizeY = dimY * N * N + 2 * dimY ; 
        setSize( mainsizeX + 200, mainsizeY );
 		setVisible ( true );       
		msq = new mainSquare ( N, desktop, this, dimX, dimY ) ; 		       
		addWindowListener ( new WindowAdapter ( )
		{
			  public void windowClosing ( WindowEvent e )
			  {
			     msq.exit ( false,false );
			  }
			  public void windowDeiconified ( WindowEvent e )
			  {
				  paint();
			  } 
			  public void windowIconified ( WindowEvent e ){}
			  public void windowDeactivated ( WindowEvent e ){}
			  public void windowActivated ( WindowEvent e )
			  {
				  paint();
			  }			  
		} ) ; 
		addMouseListener ( new MouseListener ()
		{
   		public void mouseClicked( MouseEvent event )
   		{
	   		Rectangle bb = msq.getBounds();
       		Point pw = mySelf.getLocationOnScreen();
 	   		Point pm = MouseInfo.getPointerInfo().getLocation(); 
 	   		if ( ( ( pm.x >= bb.x ) && ( pm.x <= bb.x + bb.width ) ) &&
        		( ( pm.y >= bb.y ) && ( pm.y <= bb.y + bb.height ) ) )
        	{
 	   			pm.x -= pw.x ;
 	   			pm.y -= pw.y ;
 	   			msq.notify( pm );     
   			}
   		}
   		public void mouseReleased( MouseEvent event ){ }
   		public void mousePressed( MouseEvent event ){ }
   		public void mouseEntered( MouseEvent event ){ }
   		public void mouseExited( MouseEvent event ){ }			
		} );
	    msq.doMainSquare( ) ;
	    addKeyListener(this);
   }
    public void keyTyped ( KeyEvent e )
    {
    } 
	public void keyPressed ( KeyEvent e )
    {
	    int id = e.getKeyCode();
	    msq.notify ( id );
	    msq.setEnabled(true);
    }
    public void keyReleased ( KeyEvent e )
    {	    
    }       
    public void paint()
    {
	   msq.paint();
    }
        
}