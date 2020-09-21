package basics;

public class BoolOper
{

	public static void main(String[] args)
	{
		int a = 0xff;
		int b = 0x1000;

		int c = a | b;
		int d = c & 0x00FF;
		int e = a & 0x00FF;
		int f = a & 0x1000;
		int g = c & 0x1000;
		System.out.println("|"+a+"|"+b+"|"+c+"|"+d+"|"+e+"|"+f+"|"+g);
	}

}
