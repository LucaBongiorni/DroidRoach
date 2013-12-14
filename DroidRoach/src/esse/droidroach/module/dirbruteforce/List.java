/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.dirbruteforce;

public class List {
	private static String directories[] = new String[] { ".bzr", ".git", ".hg", ".svn", "2011", "2012", "2013", "404", "a", "about", "adm", "admin", "admin2", "adminarea", "administrator", "apache", "async", "author", "authors", "autorize",
			"autorized", "b", "back", "backdoor", "backs", "backup", "backups", "bak", "baks", "bill", "bills", "bin", "binaries", "binary", "block", "blog", "body", "bookmark", "bookmarks", "box", "boxes", "bug", "bugs", "bzr", "cache", "caches",
			"camel", "captcha", "cat", "categories", "category", "cgi", "cgi-bin", "cgi_bin", "chrome", "class", "classes", "cli", "comment", "comments", "common", "components", "config", "config2", "config3", "configs", "config_old", "connect",
			"connect1", "connect2", "connect3", "connect_old", "content", "core", "credential", "credentials", "credits", "cron", "css", "customer", "customization", "customize", "customized", "dash", "dashboard", "dashboards", "db", "db1", "db2",
			"db3", "dbs", "db_old", "dead", "deads", "death", "delete", "deleted", "demo", "demo1", "demo2", "demo2_upload", "demo3", "deploy", "deployed", "deployment", "deprecated", "desktop", "docs", "document", "documents", "download",
			"download2", "downloads", "e", "edit", "en", "enclosure", "error", "errorlog", "errorlogs", "errors", "example", "example1", "example2", "example3", "example4", "exit", "export", "exporter", "f", "faq", "feed", "field", "file", "files",
			"filezilla", "firefox", "font", "fonts", "foo", "foobar", "form", "forms", "forum", "ftp", "full", "gallery", "gallery_full", "gallery_preview", "gb", "gedit", "gedit3", "git", "god", "grave", "guest", "h", "head", "header", "heads",
			"hg", "home", "home2", "home3", "home_old", "hot", "htaccess", "htpasswd", "http", "https", "i", "ie", "image", "image-cache", "images", "image_cache", "img", "imgs", "imgz", "import", "importer", "inc", "include", "includes", "index",
			"index2", "index3", "index_old", "info", "install", "install2", "installation", "installer", "intranet", "item", "items", "itmanager", "javascript", "js", "k", "l", "language", "languages", "license", "link", "links", "linux", "lisp",
			"list", "lists", "locale", "locator", "log", "login", "login2", "login3", "logout", "logout2", "logout3", "logs", "love", "m", "mac", "machintosh", "macro", "mail", "main", "media", "media-upload", "medias", "media_gallery", "menu",
			"menus", "microsoft", "minimal", "misc", "miscs", "mkportal", "mnt", "moderation", "module", "modules", "ms", "mssql", "multiple", "multiple-file-upload-with-php", "multiple-upload", "multiple.upload", "multipleupload",
			"multiple_upload", "multiple_upload_ac", "mydb", "mydb1", "mydb2", "mysql", "n", "nbproject", "net", "network", "new", "news", "node", "o", "option", "options", "p", "parse", "parser", "parsing", "password", "passwords", "perl", "perse",
			"photo", "php", "phpBB3", "phpfileuploader", "phpinfo", "phpmyadmin", "phpuploader", "pictures", "plugin", "plugins", "post", "posting", "pref", "preferences", "preview", "private", "project", "projects", "protected", "public", "pure",
			"pwd", "pwds", "python", "q", "r", "rar", "rating", "readme", "registry", "research", "revision", "revisions", "rss", "rss2", "s", "savefiles", "schema", "schemas", "screen", "screens", "screenshot", "screenshots", "script", "scripts",
			"search", "secret", "select-multiple-files-upload", "setup", "shell", "shellcode", "show", "site", "sites", "source", "space", "sql", "sqlback", "sqlbackup", "sqlite", "src", "standard", "store", "stream", "streams", "style", "sub",
			"suggest", "super", "supersecret", "svn", "sync", "syslog", "t", "table", "tables", "tag", "tags", "temp", "template", "test", "test2", "test3", "testing", "testing2", "text", "theme", "themes", "thumb", "tmp", "token", "tokens", "tomb",
			"tombstone", "topic", "topsecret", "u", "ui", "uk", "unix", "up", "update", "upload", "upload2", "uploads", "upload_old", "upz", "us", "user", "user2", "utility", "v", "view", "vuln", "vulnerabilities", "vulnerability", "w", "web",
			"web.config", "webmail", "weeveley", "window", "windows", "wordpress", "wordpress.old", "wordpress_old", "words", "world", "worlds", "wp", "wp-admin", "wp-content", "wp-includes", "x", "xml", "xmlrpc", "xmls", "y", "z", "zip" };

	private static String extensions[] = new String[] { "doc", "docx", "pdf", "xlsx", "sh", "swf", "xml", "php", "aspx", "asp", "html", "htm", "txt", "php.bak", "php_bak", "php_old", "old", "sql", "zip", "tar.gz", "log", "logs" };

	public static String[] getDirList() {
		return directories;
	}

	public static String[] getExtList() {
		return extensions;
	}

}
