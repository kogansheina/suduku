package suduku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class myOptions extends JInternalFrame implements ItemListener
{
	static final long serialVersionUID = 104L;
	private JCheckBox cb [ ];
    static final int optionNumber = 7;
    public mainSquare parent ;
    public myOptions mySelf ;
    private int X, Y ;
    private JFrame helpwindow = null ;
    
    public myOptions( int x, int y, mainSquare parent )
    {
        super("Continue", 
              false, //resizable
              false, //closable
              false, //maximizable
              false);//iconifiable

        X = x;
        this.parent = parent;
        mySelf = this;
        cb = new JCheckBox [ optionNumber ];
        JPanel pn = new JPanel ( new GridLayout( optionNumber, 1 ), false );
        cb [ 0 ] = createCheckBox( "solve", false );
        pn.add( cb [ 0 ] ); 
        cb [ 1 ] = createCheckBox( "play", false );
        pn.add( cb [ 1 ] );
        cb [ 2 ] = createCheckBox( "clear", false );
        pn.add( cb [ 2 ] );
        cb [ 3 ] = createCheckBox( "quit", false );
        pn.add( cb [ 3 ] );
        cb [ 4 ] = createCheckBox( "from file", false );
        pn.add( cb [ 4 ] );
        cb [ 5 ] = createCheckBox( "save", false );
        pn.add( cb [ 5 ] );
        cb [ 6 ] = createCheckBox( "info", false );
        pn.add( cb [ 6 ] );
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
    private void helpWindow( boolean v )
    {
    JTextArea ed ;
    String [] helpText =
    {
        "To enter a number into a square\n",
        " - click the mouse into the place\n",
        " - click the number\n",
        " To finish the first step - 'end'\n",
        " - clear, beging another game\n",
        " - play, you may fill the empty\n",
        "   places( each enter is checked)\n",
        " - hint is active only after 'end'\n",
        "   and under 'play' state\n",
        " An used hint, needs to be entered\n", 
        " The score ( under 'play' ) is :\n",
        "    + 500 * n for success\n",
        "    - 20 for 'hint'\n"
    };
    if ( v )
    {

        helpwindow = new JFrame ( "HELP" );
        helpwindow.setLocation ( X, 6*Y/5 ) ;
        ed = new JTextArea( 10, 20  );
        ed.setFont( new Font( "Dialog", Font.PLAIN, 15 ) );
        ed.setEditable( false );
        helpwindow.add(ed);
        for ( int t = 0 ; t < helpText.length; t ++ )
        {
            ed.append ( helpText [ t ] );
        }
        helpwindow.pack();
        helpwindow.setVisible(true);
    }
    else if ( helpwindow != null )helpwindow.setVisible(v);
    }
    public void itemStateChanged( ItemEvent e )
    {
        JCheckBox obj = ( JCheckBox ) e.getItemSelectable();
        if ( obj != null )
        {
            if ( obj.equals ( cb [ optionNumber - 1 ] ) )
            {
                if ( e.getStateChange() == e.SELECTED )
                { 
                    helpWindow(true);
                }
                else
                {
                    helpWindow(false);
                }
            }
            for ( int k = 0; k < cb.length - 1; k ++ )
            {
               if ( obj.equals ( cb [ k ] ) )
               {
                   if ( e.getStateChange() == e.SELECTED )
                   {  
                       synchronized ( parent )
                       {
	                       	cb [ k ].setSelected(false);
	                        parent.notify(k,mySelf);
                       }
	                   break;
                   }
               }
           }
       }
   }       
}