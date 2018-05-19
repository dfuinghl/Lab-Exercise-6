public class Prime
{
	public boolean isPrime(int x)
	{
		if (x <= 1)
			return false;
		else if (x <= 3)
			return true;
		else if ((x % 2) == 0 || (x % 3) == 0)
			return false;
		int i = 5;
		while ((i*i) <= x)
		{
			if ((x % i) == 0 || (x % (i + 2)) == 0)
				return false;
			i += 6;
		}
		return true;
	}
}
