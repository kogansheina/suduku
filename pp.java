package suduku;
class pp
{
	public int x;
	public int y;
	public int val;
	
	public pp ()
	{
		this.x = -1;
		this.y = -1;
        val = -1;
	}
	public pp ( int x, int y )
	{
		this.x = x;
		this.y = y;
        val = -1;
	}
	public pp ( int x, int y, int val )
	{
		this.x = x;
		this.y = y;
        this.val = val;
	}
}
