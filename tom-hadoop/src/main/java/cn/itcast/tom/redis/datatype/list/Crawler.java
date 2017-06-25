package cn.itcast.tom.redis.datatype.list;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import redis.clients.jedis.Jedis;

/**
 * 
 * <p>Titile:Crawler</p>
 * <p>Description: </p>
 * @author TOM
 * @date 2017年6月22日 下午4:53:54
 */
public class Crawler {
	// 定义需要爬取的URL的Key
	private static final String redisUriWillKey = "crawler:urls:will";
	// 如果需要去重的话,可以使用set保存已经爬过的url

	public static void main(String[] args) throws Exception {
		// 准备Url
		String startUrl = "http://www.huxiu.com/collections";
		String domain = "http://www.huxiu.com/";
		// 获取文章Url
		getUrls(startUrl, domain);
		// 处理url，下载文章的内容并打印
		parseUrl();
	}

	private static void parseUrl() {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		// 从右边弹出一个url
		while (true) {
			String url = jedis.rpop(redisUriWillKey);
			try {
				Article article = parser(url);
				System.out.println(article);
			} catch (Exception e) {
				// jedis.lpush(redisUrlsWillKey, url);
			}
		}
	}

	private static Article parser(String url) throws Exception {
		Document document = Jsoup.connect(url).get();
		Article article = new Article();
		// 封装作者信息
		Elements author = document.getElementsByClass("author-name");
		article.setAuthor(StringUtil.isBlank(author.text()) ? "jingzhongyue" : author.text());
		// 抽取文章日期
		Elements date = document.getElementsByClass("article-time");
		article.setDate(StringUtil.isBlank(date.text()) ? new Date() : DateUtil.getDate(date.text()));
		// 抽取文章标题
		Elements title = document.getElementsByTag("title");
		article.setTitle(title.text());
		// 抽取文章编号
		String id = url.substring(29);
		int index = id.indexOf("/");
		id = id.substring(0, index);
		article.setId(id);
		// 抽取文章正文
		StringBuffer buffer = new StringBuffer();
		Elements contents = document.getElementsByAttribute(id);
		for (Element element : contents) {
			String idTag = element.attr("id");
			if ("article_content".equals(idTag)) {
				Elements childs = element.children();
				Elements pElements = childs.tagName("p");
				for (Element element2 : pElements) {
					buffer.append(element2.text());
				}
			}
		}
		return article;
	}

	private static void getUrls(String startUrl, String domain) throws Exception {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		Document document = Jsoup.connect(startUrl).get();
		Elements elements = document.getElementsByAttribute("href");
		for (Element element : elements) {
			String endUrl = element.attr("href");
			if (endUrl.contains("article")) {
				String url = domain + endUrl;
				System.out.println(url);
				jedis.lpush(redisUriWillKey, url);
			}
		}
	}
}
