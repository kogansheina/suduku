package suduku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class myInput extends JInternalFrame implements ItemListener
{
	static final long serialVersionUID = 103L;
	private JCheckBox cb [ ];
    private int optionNumber ;
    public mainSquare parent ;
    public myInput mySelf;
    
    public myInput( int x, int y, mainSquare parent, int N )
    {
        super("Input Options", 
              false, //resizable
              false, //closable
              false, //maximizable
              false);//iconifiable
        this.parent = parent;
        mySelf = this ;
        optionNumber = N * N + 3 ;
		cb = new JCheckBox [ optionNumber ];
        JPanel pn = new JPanel ( new GridLayout( optionNumber, 1 ), false );
        cb [ 0 ] = createCheckBox( "delete", false );
        pn.add( cb [ 0 ] );
        for ( int i = 1; i < optionNumber - 2; i ++ )
        {
        	cb [ i ] = createCheckBox( new Integer ( i ).toString(), false );
        	pn.add( cb [ i ] );
    	}
    	cb [ optionNumber - 2 ] = createCheckBox( "end", false );
        pn.add( cb [ optionNumber - 2 ] );
    	cb [ optionNumber - 1 ] = createCheckBox( "hint", false );
        pn.add( cb [ optionNumber - 1 ] );
        getContentPane().add ( pn );
        //Set the window's location.
        setLocation( x, y );
        pack();      
    }
 	private JCheckBox createCheckBox( String s, boolean b )
    {
        JCheckBox cb = new JCheckBox( s, b );
        cb.setHorizontalAlignment( JCheckBox.LEFT );
        cb.addItemListener( this );

        return cb;
    }

    public void itemStateChanged( ItemEvent e )
    {
        JCheckBox obj = ( JCheckBox ) e.getItemSelectable();
        if ( obj != null )
        {
            for ( int k = 0; k < cb.length ; k ++ )
            {
               if ( obj.equals ( cb [ k ] ) )
               {
                   if ( e.getStateChange() == e.SELECTED )
                   {  
                       synchronized ( parent )
                       {
	                      cb [ k ].setSelected(false);
                          parent.notify(k, mySelf);
                          transferFocusBackward();
	    				  parent.requestFocusInWindow();
                       }
	                   break;
                   }
               }
           }
       }
   }   
}

