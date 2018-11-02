package illumioOA;

public class portRange {
	int start;
	int end;

	public portRange(String s) {
		if (s.indexOf("-") == -1) {
			this.start = sToNum(s);
			end = start;
		} else {
			int idx = s.indexOf("-");
			this.start = sToNum(s.substring(0, idx));
			this.end = sToNum(s.substring(idx + 1));
		}
	}
	
	//convert string to integer
	public int sToNum(String s) {
		int tmp = 0;
		for (int i = 0; i < s.length(); i++) {
			tmp = tmp * 10 + (s.charAt(i) - '0');
		}
		return tmp;
	}
	
}
