package illumioOA;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class Firewall{
	
	private Set<String> directions;
	private Set<String> protocals;
	private List<portRange> portRanges;
	private List<ipRange> ipRanges;
	
	public Firewall(String fileName) {
		directions = new HashSet<>();
		protocals = new HashSet<>();
		portRanges = new LinkedList<>();
		ipRanges = new LinkedList<>();
		
		LinkedList<String> content = new LinkedList<>();
		//read from csv file and add to a linkedlist for future processing
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = "";
	        try {
				while ((line = br.readLine()) != null) {
				    content.add(line);
				}
			} catch (IOException e) { System.out.println(e.getMessage()); }
	    } catch (FileNotFoundException e) { System.out.println(e.getMessage());}
	    
		//read each line from the .csv file
		for (String str: content){
			String[] s = str.split(",");
			directions.add(s[0]);
			protocals.add(s[1]);
			portRange p = new portRange(s[2]);
			ipRange ip = new ipRange(s[3]);
			portRanges.add(p);
			ipRanges.add(ip);
		}
				
		//sort port ranges by start number in ascending order
		Collections.sort(portRanges, new Comparator<portRange>(){
			public int compare(portRange p1, portRange p2) {
				if (p1.start != p2.start) {
					return p1.start - p2.start;
				} else if (p1.end != p2.end) {
					return p1.end - p2.end;
				}
				else return 0;
			}
			
		});
		
		//sort ip ranges by start number in ascending order
		Collections.sort(ipRanges, new Comparator<ipRange>(){
			public int compare(ipRange ip1, ipRange ip2) {
				if (ip1.start != ip2.start) {
					return (ip1.start).compareTo(ip2.start);
				} else if (ip1.end != ip2.end) {
					return (ip1.end).compareTo(ip2.end);
				}
				else return 0;
			}
		});
		
		
		//in-place merge for all port ranges
		for (int i = 0; i < portRanges.size() - 1; i++) {
			portRange cur = portRanges.get(i);
			portRange next = portRanges.get(i+1);

	        if (cur.end >= next.start) {
	            next.start = cur.start;
	            next.end = (int) Math.max(next.end, cur.end);
	            portRanges.remove(i);
	            i--;
	        }
	    }
		
		//in-place merge of all ipRanges 
		for (int i = 0; i < ipRanges.size() - 1; i++) {
			ipRange cur = ipRanges.get(i);
			ipRange next = ipRanges.get(i+1);

	        if (cur.end >= next.start) {
	            next.start = cur.start;
	            next.end = (Long)Math.max(next.end, cur.end);
	            ipRanges.remove(i);
	            i--;
	        }
	    }
	}
	
	public boolean accept_packet(String direction, String protocal, Integer port, String ip_address) {
		if (!directions.contains(direction)) {
			//System.out.println("dir wrong");
			return false;
		}
		if (!protocals.contains(protocal)) {
			//System.out.println("pro wrong");
			return false;
		}
		if (!findPortRange(portRanges, port)) {
			//System.out.println("port wrong");
			return false;
		}
		if (!findIpRange(ipRanges, ip_address)) {
			//System.out.println("ip wrong");
			return false;
		}
		return true;
	}
	
	//use binary search to find if an input port number is in any of the ranges of port ranges
	public boolean findPortRange(List<portRange> portRanges, Integer port) {
		int l = 0; int r = portRanges.size() - 1;
		int maybe = l;
		while (l <= r) {
			int mid = l + (r-l)/2;
			if (portRanges.get(mid).start == port) return true;
			else if (portRanges.get(mid).end == port) return true;
			else if (portRanges.get(mid).start < port) {
				maybe = mid;
				l = mid + 1;
			}
			else r = mid - 1;
		}
		if (port >= portRanges.get(maybe).start && port <= portRanges.get(maybe).end) return true;
		if (l < portRanges.size() && port >= portRanges.get(l).start && port <= portRanges.get(l).end) return true;
		return false;
	}
	
	//use binary search to find if an input ip address is in any of the ranges of ip ranges 
	public boolean findIpRange(List<ipRange> ipRanges, String str) {
		Long ip = ipToLong(str);
		int l = 0; int r = ipRanges.size() - 1;
		int maybe = l;
		while (l <= r) {
			int mid = l + (r-l)/2;
			if ((ipRanges.get(mid).start).equals(ip)) return true;
			else if ((ipRanges.get(mid).end).equals(ip)) return true;
			else if ((ipRanges.get(mid).start) <= ip) {
				maybe = mid;
				l = mid + 1;
			}
			else r = mid - 1;
		}
		if (ip.longValue() >= ipRanges.get(maybe).start.longValue() 
				&& ip.longValue() <= ipRanges.get(maybe).end.longValue()) return true;
		if (l < ipRanges.size() && ip.longValue() >= ipRanges.get(l).start.longValue() 
				&& ip.longValue() <= ipRanges.get(l).end.longValue()) return true;
		return false;
	}
	
	//convert ip adddress in string format to long type (for comparison)
	public static long ipToLong(String s) {
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
	
	public Set<String> getDirections() {
		return this.directions;
	}
	
	
	public Set<String> getProtocals() {
		return this.protocals;
	}
	
	public List<portRange> getPortRange(){
		return this.portRanges;
	}
	
	public List<ipRange> getIpRange(){
		return this.ipRanges;
	}
	
}


