package basics;

public class StyleColor
{

    public static void main(String[] args) {
        int [] color = { 11111, 22222, 33333, 44444 , 55555 };
        for (int i : color) {
            System.out.println(i + formatColor(i));
        }

    }
    
    
    private static String formatColor( int aColor )
    {
      String lBlue = Integer.toString( aColor & 0xff, 16 );
      String lGreen = Integer.toString( ( aColor >> 8 ) & 0xff, 16 ) ;
      String lRed = Integer.toString( ( aColor >> 16 ) & 0xff, 16 );
      return "#" + (lBlue.length()<2?"0":"")+lBlue 
              +(lGreen.length()<2?"0":"")+ lGreen 
              +(lRed.length()<2?"0":"")+ lRed;
    }

}
