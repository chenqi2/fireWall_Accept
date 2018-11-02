package illumioOA;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class App {

	public static void main(String[] args) throws UnknownHostException {

		String fileName = "/Users/chenchenqi/Desktop/test.csv";
		Firewall fw = new Firewall(fileName);
		
		//test invalid direction or protocal input
		System.out.println(fw.accept_packet("inbound","tcp",80,"192.168.1.2"));
		System.out.println(fw.accept_packet("inbound","udp",80,"192.168.1.2"));
		System.out.println(fw.accept_packet("sefa","tcp",80,"192.168.1.2"));
		System.out.println(fw.accept_packet("inbound","afe",80,"192.168.1.2"));
		
		//test numbers on the bound of a port range
		System.out.println(fw.accept_packet("outbound","tcp",1000,"192.168.10.11"));
		System.out.println(fw.accept_packet("outbound","tcp",2005,"192.168.10.11"));
		
		//test number included in the port range
		System.out.println(fw.accept_packet("outbound","tcp",10001,"192.168.10.11"));
		System.out.println(fw.accept_packet("outbound","tcp",1500,"192.168.10.11"));
		System.out.println(fw.accept_packet("outbound","tcp",2000,"192.168.10.11"));
		
		//test invalid port number
		System.out.println(fw.accept_packet("outbound","tcp",4000,"192.168.10.11"));
		System.out.println(fw.accept_packet("outbound","tcp",100,"192.168.10.11"));
		
		//test ip at the bound of an ip range
		System.out.println(fw.accept_packet("inbound", "tcp", 80, "192.168.1.1"));
		System.out.println(fw.accept_packet("inbound", "tcp", 80, "192.168.11.15"));

		//test number included in the ip range
		System.out.println(fw.accept_packet("outbound", "udp", 20000, "192.168.11.1"));	
		
		//test invalid ip range
		System.out.println(fw.accept_packet("outbound", "udp", 20000, "256.256.256.256"));	
		System.out.println(fw.accept_packet("outbound", "udp", 20000, "0.0.0.0"));	
		System.out.println(fw.accept_packet("outbound", "udp", 20000, "52.12.48.93"));
		
		//print out current valid firewall ip range from reading csv file
		//used for writing test case 
		/*
		for (ipRange ip: fw.getIpRange()) {
			System.out.println(InetAddress.getByAddress(ip.unpack(ip.start)).getHostAddress() 
					+ " " + InetAddress.getByAddress(ip.unpack(ip.end)).getHostAddress());
		}
		*/
	}
}

