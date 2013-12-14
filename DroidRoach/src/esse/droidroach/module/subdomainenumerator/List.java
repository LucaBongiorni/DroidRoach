/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.subdomainenumerator;

public class List {
	private static String[] subdomains = { "0", "00", "000", "0000", "01", "1", "123", "2", "3", "4", "5", "6", "666", "7", "8", "9", "a", "ab", "abs", "acid",
			"activity", "admin", "admin1", "admin2", "admin3", "admin4", "admin666", "adminarea", "administracion", "administrateur", "administrator", "administrator1",
			"administrator2", "administrator3", "adress", "ag", "agile", "alt", "alt1", "alt2", "alt3", "alt4", "alt5", "alt6", "alt666", "ambra", "amc",
			"amministratore", "anon", "anonymous", "apache", "app", "apps", "archive", "area", "arg", "asm", "awesome", "back", "backup", "backups", "bad", "badass",
			"balls", "be", "beta", "bild", "black", "block", "blocks", "blog", "blogs", "bo", "board", "body", "build", "c", "ca", "cache", "card", "ch", "cisco",
			"classifica", "client", "clients", "clone", "co", "code", "com", "community", "correo", "courrier", "customer", "cz", "d", "database", "db", "de", "debug",
			"den", "descargar", "dev", "dev0", "dev1", "dev2", "dev3", "dev4", "dev5", "dev6", "dev666", "devarea", "developer", "developers", "devsite", "dispatch",
			"div", "dl", "do", "doc", "docs", "documenta", "documentation", "documentazione", "documentos", "dokument", "dokumente", "dokumenter", "dokumenty",
			"download", "drop", "e", "email", "en", "error", "es", "esp", "essai", "ext", "external", "f", "fame", "faq", "faqs", "fi", "fin", "fire", "firewall",
			"flash", "foo", "for", "fr", "fra", "frame", "game", "gameserver", "gap", "git", "group", "hack", "hacked", "hall", "hard", "hell", "help", "hidden", "hl",
			"ho", "hol", "horny", "host", "hot", "html", "i", "icy", "image", "images", "img", "img1", "img2", "img3", "img4", "img5", "img6", "img7", "img8", "img9",
			"imgs", "imgs1", "imgs2", "internal", "internalwiki", "internet", "intranet", "intro", "it", "itmanager", "itpedia", "jap", "joke", "ju", "jumbo",
			"jumbomail", "kick", "kik", "ko", "kor", "l", "lag", "like", "link", "log", "login", "loginserver", "logout", "lol", "lolz", "lul", "lulz", "m", "mac",
			"machine", "mail", "main", "man", "manage", "management", "manager", "member", "memberarea", "members", "menu", "metops", "mi", "mnt", "mobi", "mobile",
			"moderator", "mssql", "music", "musica", "mx", "mx0", "mx1", "mx10", "mx2", "mx3", "mx4", "mx5", "mx6", "mx7", "mx8", "mx9", "my", "mysql", "name", "net",
			"new", "news", "nl", "ns", "ns1", "ns10", "ns2", "ns3", "ns4", "ns5", "ns6", "ns7", "ns8", "ns9", "ny", "nyc", "o", "ol", "old", "other", "others", "p",
			"pa", "password", "pause", "penis", "phone", "photo", "photos", "player", "po", "poison", "pol", "pool", "portfolio", "posta", "postmaster", "privacy",
			"privata", "private", "privatewiki", "privato", "project", "projects", "prova", "proxy", "prueba", "pussy", "rad", "random", "rar", "rdp", "registar",
			"register", "registra", "registrazione", "repo", "repository", "reputation", "research", "result", "results", "ro", "route", "ru", "rx", "rx0", "rx1", "rx2",
			"rx3", "rx4", "rx5", "rx6", "rx7", "rx8", "rx9", "sale", "sales", "salvataggi", "sample", "samples", "satan", "saves", "se", "search", "secret", "secrets",
			"secretwiki", "secure", "security", "server", "sex", "sicuro", "sites", "siti", "slash", "smart", "smartphone", "soft", "space", "spf", "spice", "splash",
			"spot", "sql", "static", "sub", "subdomain", "supertest", "svn", "sw", "swi", "t", "tablet", "tap", "tarball", "telecharger", "test", "test123", "test2",
			"testas", "teste", "testet", "testi", "testing", "testo", "testone", "testtwo", "testwo", "teszt", "time", "to", "toest", "toets", "tr", "tw", "tx", "tx1",
			"tx2", "tx3", "tx4", "tx5", "tx6", "tx7", "tx8", "tx9", "u", "uji", "uk", "underpool", "update", "updates", "upload", "user", "v", "venom", "video",
			"videos", "w", "w1", "w2", "w3", "w3c", "w4", "wap", "war", "watch", "wc", "wd", "web", "webmail", "wiki", "wikipedia", "wildcard", "ww0", "ww1", "ww2",
			"ww3", "www", "www2", "www3", "x", "xxx", "y", "you", "young", "your", "z", "zip", };

	public static String[] getSubdomainsList() {
		return subdomains;
	}

}
