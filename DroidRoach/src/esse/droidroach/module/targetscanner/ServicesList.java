/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.targetscanner;

public class ServicesList {

	
	/**
	 * Function to obtain the service commonly running on the given port
	 * @param port
	 * @return
	 */
	public static String getServiceName(int port) {

		switch (port) {

		case 13:
			return "Daytime protocol";
		case 20:
			return "Ftp data transfer";
		case 21:
			return "Ftp control(command)";
		case 22:
			return "Secure shell (SSH)";
		case 23:
			return "Telnet protocol";
		case 24:
			return "Priv-mail";
		case 25:
			return "SMTP";
		case 35:
			return "Private printer server protocol";
		case 37:
			return "Time protocol";
		case 39:
			return "Resource location protocol";
		case 43:
			return "WHOIS";
		case 53:
			return "DNS";
		case 80:
			return "HTTP web server";
		case 101:
			return "NIC host name";
		case 107:
			return "Telnet";
		case 110:
			return "POP3";
		case 115:
			return "SFTP";
		case 118:
			return "SQL services";
		case 137:
			return "NetBIOS name service";
		case 138:
			return "NetBIOS datagram service";
		case 139:
			return "NetBIOS session service";
		case 143:
			return "IMAP";
		case 156:
			return "SQL service";
		case 220:
			return "IMAP v3";
		case 331:
			return "Mac OS X server admin";
		case 389:
			return "LDAP";
		case 401:
			return "UPS uninterruptible power supply";
		case 443:
			return "HTTPS web server";
		case 445:
			return "Microsoft-DS active directory (windows shares, smb file sharing)";
		case 465:
			return "SMTP over SSL";
		case 514:
			return "Remote shell";
		case 515:
			return "Print service";
		case 530:
			return "RPC";
		case 547:
			return "DHCPv6 server";
		case 548:
			return "Apple filing protocol over TCP";
		case 587:
			return "SMTP";
		case 660:
			return "Mac OS X server administrator";
		case 691:
			return "MS exchange routing";
		case 783:
			return "SpamAssassin spamd daemon";
		case 843:
			return "Adobe Flash";
		case 901:
			return "Samba web administration tool (SWAT)";
		case 902:
			return "WMware server console";
		case 904:
			return "WMware server alternate";
		case 953:
			return "DNS";
		case 991:
			return "NAS";
		case 992:
			return "Telnet over SSL";
		case 993:
			return "IMAPS";
		case 995:
			return "POP3S";
		case 1194:
			return "OpenVPN";
		case 1241:
			return "Nessus security scanner";
		case 1433:
			return "MSSQL server";
		case 1434:
			return "MSSQL monitor";
		case 1521:
			return "Oracle database listener";
		case 1720:
			return "H.323";
		case 1723:
			return "Microsoft PPTP";
		case 2082:
			return "CPanel default";
		case 2083:
			return "CPanel default SSL";
		case 2095:
			return "CPanel default web mail";
		case 2096:
			return "CPanel default SSL web mail";
		case 2121:
			return "FTP Proxy";
		case 2483:
			return "Oracle database listening";
		case 2484:
			return "Oracle database listening SSL";
		case 2638:
			return "SQL anywhere database server";
		case 3000:
			return "Ruby on rails";
		case 3128:
			return "Web caches/Squid";
		case 3283:
			return "Apple remote desktop";
		case 3306:
			return "MySql database system";
		case 3389:
			return "Microsoft RDP";
		case 3493:
			return "NUT";
		case 3541:
			return "Voispeed web interface";
		case 3790:
			return "Metasploit web interface";
		case 3872:
			return "Oracle management remote agent";
		case 4000:
			return "Diablo 2";
		case 4125:
			return "Microsoft remote web workplace administration";
		case 4444:
			return "Oracle WebSenter content server / HTTP/s proxy";
		case 4445:
			return "HTTP/s proxy";
		case 4486:
			return "ICMS";
		case 4711:
			return "eMule web interface";
		case 4993:
			return "Home FTP Server web interface";
		case 5037:
			return "Android ADB server";
		case 5108:
			return "VPOP3 mail server webmail";
		case 5353:
			return "mDNS";
		case 5432:
			return "PostgreSQL database system";
		case 5800:
			return "VNC remote desktop protocol over HTTP";
		case 5900:
			return "VNC/RFB";
		case 5938:
			return "TeamViewer remote desktop protocol";
		case 5984:
			return "CouchDB database server";
		case 6000:
			return "X11";
		case 6086:
			return "PDTP";
		case 6112:
			return "dtspcd";
		case 6200:
			return "Oracle webcenter content portable";
		case 6201:
			return "Oracle webcenter content portable";
		case 6225:
			return "Oracle webcenter content portable";
		case 6227:
			return "Oracle webcenter content portable";
		case 6240:
			return "Oracle webcenter content portable";
		case 6244:
			return "Oracle webcenter content portable";
		case 6255:
			return "Oracle webcenter content portable";
		case 6660:
			return "IRC";
		case 6661:
			return "IRC";
		case 6662:
			return "IRC";
		case 6663:
			return "IRC";
		case 6664:
			return "IRC";
		case 6665:
			return "IRC";
		case 6666:
			return "IRC";
		case 6667:
			return "IRC";
		case 6668:
			return "IRC";
		case 6669:
			return "IRC";
		case 6679:
			return "IRC SSL";	
		case 6697:
			return "IRC SSL";
		case 6699:
			return "WinMX";	
		case 8008:
			return "HTTP alternate / IBM HTTP server administration";
		case 8080:
			return "HTTP alternate";
		case 8081:
			return "HTTP alternate";
		case 8222:
			return "VMware server management user interface";
		case 8333:
			return "VMware server management user interface";
		case 8834:
			return "Nessus";
		case 8887:
			return "HyperVM HTTP";
		case 8888:
			return "HyperVM HTTPS / Freenet HTTP";
		case 9001:
			return "HSQLDB server";
		default:
			return "Unknow";

		}

	}

}
