import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestingRegex {

	public static void main(String[] args) {
		System.out.println("=)=)=)=)=)".matches(("(=\\))+|(:\\))+|(:-\\))+|(>:\\])+|(:o\\))+|(:3)+|(:>)+|(=\\])+|(8\\))+|(:\\})+|(:\\^\\))+|(>:\\[)+|(:-\\()+|(:\\()+|(:-c)+|(:c)+|(:-<)+|(:<)+")));
//		System.out.println(Pattern.matches(Pattern.quote("(:))+"), ":):)"));
		System.out.println("SHOUT".matches("[A-Z]+"));
		
		String str = "blaaaaaaahhh";
		Pattern p = Pattern.compile("(\\w)\\1+");
		Matcher m = p.matcher(str);
		if (m.find()) {
			System.out.println(m.group());
		}
		
		String str1 = "omg!! omg";
		Pattern ps = Pattern.compile("omg|OMG|(Oh)?.? (My)?.? Go+d.?|(oh)? my go+d|(oh)? (my)? goo+dness|(oh)? (my)? gosh");
		Matcher m1= ps.matcher(str1);
		
		if (m1.find())
			System.out.println(m1.group());
	}
}
