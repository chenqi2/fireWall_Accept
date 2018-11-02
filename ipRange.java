package illumioOA;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ipRange {
	Long start;
	Long end;
	
	public ipRange(String s) {
		if (s.indexOf("-") == -1) {
			this.start = ipToLong(s);
			this.end = this.start;
		} else {
			int idx = s.indexOf("-");
			this.start = ipToLong(s.substring(0, idx));
			this.end = ipToLong(s.substring(idx + 1));
		}
	}
	
	//convert String format ip address to long type
	public long ipToLong(String s) {
		InetAddress ip;
		try {
			ip = InetAddress.getByName(s);
			byte[] bytes = ip.getAddress();
			long result = 0;
			for (byte octet : bytes) {
				result <<= 8;
				result |= octet & 0xff;
			}
			return result;
		} catch (UnknownHostException e) { System.out.println(e.getMessage()); }
		//throws exception;
		return -1; 
	}
	
	//convert long type back to byte array
	public byte[] unpack(Long bytes) {
	  return new byte[] {
	    (byte)((bytes >>> 24) & 0xff),
	    (byte)((bytes >>> 16) & 0xff),
	    (byte)((bytes >>>  8) & 0xff),
	    (byte)((bytes       ) & 0xff)
	  };
	}
}