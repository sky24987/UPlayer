package com.alphanetworks.uplayer.upnp.dms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.util.Log;

public class HttpServer extends NanoHTTPD {

	private static final String LOG_TAG = HttpServer.class.getSimpleName();
	private File mRootDir = null;

	/**
	 * Hashtable mapping (String)FILENAME_EXTENSION -> (String)MIME_TYPE
	 */
	@SuppressWarnings("serial")
	private static final HashMap<String, String> MIME_TYPES = new HashMap<String, String>() {
		{
			put("css", "text/css");
			put("htm", "text/html");
			put("html", "text/html");
			put("xml", "text/xml");
			put("java", "text/x-java-source, text/java");
			put("txt", "text/plain");
			put("asc", "text/plain");
			put("gif", "image/gif");
			put("jpg", "image/jpeg");
			put("jpeg", "image/jpeg");
			put("png", "image/png");
			put("mp3", "audio/mpeg");
			put("m3u", "audio/mpeg-url");
			put("mp4", "video/mp4");
			put("ogv", "video/ogg");
			put("flv", "video/x-flv");
			put("mov", "video/quicktime");
			put("swf", "application/x-shockwave-flash");
			put("js", "application/javascript");
			put("pdf", "application/pdf");
			put("doc", "application/msword");
			put("ogg", "application/x-ogg");
			put("zip", "application/octet-stream");
			put("exe", "application/octet-stream");
			put("class", "application/octet-stream");
		}
	};

	public HttpServer(int port) {
		super(port);
		mRootDir = new File("/");
		// TODO Auto-generated constructor stub
	}

	public HttpServer(String hostname, int port) {
		super(hostname, port);
		mRootDir = new File("/");
		// TODO Auto-generated constructor stub

	}

	@Override
	public Response serve(String uri, Method method,
			Map<String, String> header, Map<String, String> parms,
			Map<String, String> files) {
		// TODO Auto-generated method stub

		// Map uri to acture file in ContentTree
		Log.i(LOG_TAG, "uri:" + uri + "method:" + method);
		for (String key : header.keySet())
			Log.i(LOG_TAG, "HEADER,key:" + key + "value:" + header.get(key));
		for (String key : parms.keySet())
			Log.i(LOG_TAG, "PARMS:key:" + key + "value:" + parms.get(key));
		for (String key : files.keySet())
			Log.i(LOG_TAG, "FILES,key:" + key + "value:" + files.get(key));

		try {
			String itemId = uri.replaceFirst("/", "");
			itemId = URLDecoder.decode(itemId, "UTF-8");
			String newUri = null;

			if (ContentTree.hasContentNode(itemId, true)) {
				ContentNode node = ContentTree.getContentNode(itemId, true);
				if (node.isItem()) {
					newUri = node.getPath();
				}
			}

			if (newUri != null)
				uri = newUri;
			return serveFile(uri, header, mRootDir);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	Response serveFile(String uri, Map<String, String> header, File homeDir) {
		Response res = null;

		// Make sure we won't die of an exception later
		if (!homeDir.isDirectory()) {
			res = new Response(Response.Status.INTERNAL_ERROR,
					NanoHTTPD.MIME_PLAINTEXT,
					"INTERNAL ERRROR: serveFile(): given homeDir is not a directory.");
		}

		if (res == null) {
			// Remove URL arguments
			uri = uri.trim().replace(File.separatorChar, '/');
			if (uri.indexOf('?') >= 0)
				uri = uri.substring(0, uri.indexOf('?'));

			// Prohibit getting out of current directory
			if (uri.startsWith("src/main") || uri.endsWith("src/main")
					|| uri.contains("../"))
				res = new Response(Response.Status.FORBIDDEN,
						NanoHTTPD.MIME_PLAINTEXT,
						"FORBIDDEN: Won't serve ../ for security reasons.");
		}

		File f = new File(homeDir, uri);
		if (res == null && !f.exists()) {
			res = new Response(Response.Status.NOT_FOUND,
					NanoHTTPD.MIME_PLAINTEXT, "Error 404, file not found.");
		}

		// List the directory, if necessary
		if (res == null && f.isDirectory()) {
			// Browsers get confused without '/' after the
			// directory, send a redirect.
			if (!uri.endsWith("/")) {
				uri += "/";
				res = new Response(Response.Status.REDIRECT,
						NanoHTTPD.MIME_HTML,
						"<html><body>Redirected: <a href=\"" + uri + "\">"
								+ uri + "</a></body></html>");
				res.addHeader("Location", uri);
			}

			if (res == null) {
				// First try index.html and index.htm
				if (new File(f, "index.html").exists()) {
					f = new File(homeDir, uri + "/index.html");
				} else if (new File(f, "index.htm").exists()) {
					f = new File(homeDir, uri + "/index.htm");
				} else if (f.canRead()) {
					// No index file, list the directory if it is readable
					res = new Response(listDirectory(uri, f));
				} else {
					res = new Response(Response.Status.FORBIDDEN,
							NanoHTTPD.MIME_PLAINTEXT,
							"FORBIDDEN: No directory listing.");
				}
			}
		}

		try {
			if (res == null) {
				// Get MIME type from file name extension, if possible
				String mime = null;
				int dot = f.getCanonicalPath().lastIndexOf('.');
				if (dot >= 0)
					mime = MIME_TYPES.get(f.getCanonicalPath()
							.substring(dot + 1).toLowerCase());
				if (mime == null)
					mime = NanoHTTPD.MIME_DEFAULT_BINARY;

				// Calculate etag
				String etag = Integer.toHexString((f.getAbsolutePath()
						+ f.lastModified() + "" + f.length()).hashCode());

				// Support (simple) skipping:
				long startFrom = 0;
				long endAt = -1;
				String range = header.get("range");
				if (range != null) {
					if (range.startsWith("bytes=")) {
						range = range.substring("bytes=".length());
						int minus = range.indexOf('-');
						try {
							if (minus > 0) {
								startFrom = Long.parseLong(range.substring(0,
										minus));
								endAt = Long.parseLong(range
										.substring(minus + 1));
							}
						} catch (NumberFormatException ignored) {
						}
					}
				}

				// Change return code and add Content-Range header when skipping
				// is requested
				long fileLen = f.length();
				if (range != null && startFrom >= 0) {
					if (startFrom >= fileLen) {
						res = new Response(
								Response.Status.RANGE_NOT_SATISFIABLE,
								NanoHTTPD.MIME_PLAINTEXT, "");
						res.addHeader("Content-Range", "bytes 0-0/" + fileLen);
						res.addHeader("ETag", etag);
					} else {
						if (endAt < 0)
							endAt = fileLen - 1;
						long newLen = endAt - startFrom + 1;
						if (newLen < 0)
							newLen = 0;

						final long dataLen = newLen;
						FileInputStream fis = new FileInputStream(f) {
							@Override
							public int available() throws IOException {
								return (int) dataLen;
							}
						};
						fis.skip(startFrom);

						res = new Response(Response.Status.PARTIAL_CONTENT,
								mime, fis);
						res.addHeader("Content-Length", "" + dataLen);
						res.addHeader("Content-Range", "bytes " + startFrom
								+ "-" + endAt + "/" + fileLen);
						res.addHeader("ETag", etag);
					}
				} else {
					if (etag.equals(header.get("if-none-match")))
						res = new Response(Response.Status.NOT_MODIFIED, mime,
								"");
					else {
						res = new Response(Response.Status.OK, mime,
								new FileInputStream(f));
						res.addHeader("Content-Length", "" + fileLen);
						res.addHeader("ETag", etag);
					}
				}
			}
		} catch (IOException ioe) {
			res = new Response(Response.Status.FORBIDDEN,
					NanoHTTPD.MIME_PLAINTEXT, "FORBIDDEN: Reading file failed.");
		}

		res.addHeader("Accept-Ranges", "bytes"); // Announce that the file
													// server accepts partial
													// content requestes
		return res;
	}

	private String listDirectory(String uri, File f) {
		String heading = "Directory " + uri;
		String msg = "<html><head><title>" + heading + "</title><style><!--\n"
				+ "span.dirname { font-weight: bold; }\n"
				+ "span.filesize { font-size: 75%; }\n" + "// -->\n"
				+ "</style>" + "</head><body><h1>" + heading + "</h1>";

		String up = null;
		if (uri.length() > 1) {
			String u = uri.substring(0, uri.length() - 1);
			int slash = u.lastIndexOf('/');
			if (slash >= 0 && slash < u.length()) {
				up = uri.substring(0, slash + 1);
			}
		}

		List<String> files = Arrays.asList(f.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isFile();
			}
		}));
		Collections.sort(files);
		List<String> directories = Arrays.asList(f.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		}));
		Collections.sort(directories);
		if (up != null || directories.size() + files.size() > 0) {
			msg += "<ul>";
			if (up != null || directories.size() > 0) {
				msg += "<section class=\"directories\">";
				if (up != null) {
					msg += "<li><a rel=\"directory\" href=\""
							+ up
							+ "\"><span class=\"dirname\">..</span></a></b></li>";
				}
				for (int i = 0; i < directories.size(); i++) {
					String dir = directories.get(i) + "/";
					msg += "<li><a rel=\"directory\" href=\""
							+ encodeUri(uri + dir)
							+ "\"><span class=\"dirname\">" + dir
							+ "</span></a></b></li>";
				}
				msg += "</section>";
			}
			if (files.size() > 0) {
				msg += "<section class=\"files\">";
				for (int i = 0; i < files.size(); i++) {
					String file = files.get(i);

					msg += "<li><a href=\"" + encodeUri(uri + file)
							+ "\"><span class=\"filename\">" + file
							+ "</span></a>";
					File curFile = new File(f, file);
					long len = curFile.length();
					msg += "&nbsp;<span class=\"filesize\">(";
					if (len < 1024)
						msg += len + " bytes";
					else if (len < 1024 * 1024)
						msg += len / 1024 + "." + (len % 1024 / 10 % 100)
								+ " KB";
					else
						msg += len / (1024 * 1024) + "." + len % (1024 * 1024)
								/ 10 % 100 + " MB";
					msg += ")</span></li>";
				}
				msg += "</section>";
			}
			msg += "</ul>";
		}
		msg += "</body></html>";
		return msg;
	}

	/**
	 * URL-encodes everything between "/"-characters. Encodes spaces as '%20'
	 * instead of '+'.
	 */
	private String encodeUri(String uri) {
		String newUri = "";
		StringTokenizer st = new StringTokenizer(uri, "/ ", true);
		while (st.hasMoreTokens()) {
			String tok = st.nextToken();
			if (tok.equals("/"))
				newUri += "/";
			else if (tok.equals(" "))
				newUri += "%20";
			else {
				try {
					newUri += URLEncoder.encode(tok, "UTF-8");
				} catch (UnsupportedEncodingException ignored) {
				}
			}
		}
		return newUri;
	}
}