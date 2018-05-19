import java.io.Serializable;

public class Message implements Serializable
{
	public String control;
	public String associatedData;
	public int id;
	public Message(String c, String a, int id)
	{
		control = c;
		associatedData = a;
		this.id = id;
	}

	public String getControl()
	{
		return control;
	}
	public void setControl(String message)
	{
		control = message;
	}
	public String getAssociatedData()
	{
		return associatedData;
	}
	public int getId()
	{
		return id;
	}
}
