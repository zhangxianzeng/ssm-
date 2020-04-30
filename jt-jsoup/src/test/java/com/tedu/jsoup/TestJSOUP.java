package com.tedu.jsoup;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tedu.jsoup.pojo.StudentBook;
import com.tedu.jsoup.service.StudentBookService;
import com.tedu.jsoup.service.StudentBookServiceImpl;

public class TestJSOUP {
	
	private static ObjectMapper objectMapper = 
			new ObjectMapper();
	
	
	/**
	 * 1.爬取静态页面数据 获取页面标题
	 * 2.定位目标网站的页面
	 * 3.定位页面的元素内容
	 * 4.获取具体的数据
	 * 5.数据处理
	 * 6.入库保存
	 * @throws IOException 
	 */
	
	@Test
	public void test01() throws IOException{
		String url = "http://www.tmooc.cn/web/index_new.html?tedu";
		
		//通过JSOUP进行数据爬取 
		Document document = Jsoup.connect(url).get();
		
		//通过jsoup提供的选择器快速定位目标元素
		Element element = document.select(".b_search h2").get(0);
		
		//获取h2的具体的文本内容
		String msg = element.text();
		
		System.out.println("爬取标题数据:"+msg);
	}
	
	
	//获取页面中学生人数
	@Test
	public void test02() throws IOException{
		String url = "http://www.tmooc.cn/web/index_new.html?tedu";
		
		Element element = 
				Jsoup.connect(url).get().select("#user_num").get(0);
		
		String msg = element.text();
		
		System.out.println("获取学生人数:"+msg);
		
	}
	
	
	//获取动态的页面数据  与之前的完全不同了
	@Test
	public void test03() throws IOException{
		String url = "http://www.tmooc.cn/commonData/getCommonNum";
		
		Response response = 
				Jsoup.connect(url).ignoreContentType(true).execute();
		
		//将获取到的结果转化为字符串
		String resultJSON = response.body();
		
		System.out.println("获取响应数据:!!!!!"+resultJSON);
		//{"result":"1","obj":{"userNum":397607,"bookNum":999,"dirNum":17}}
		//获取学生的人数
		JsonNode jsonNode = objectMapper.readTree(resultJSON);
		
		//获取obj的属性值
		String userNum =
				jsonNode.get("obj").get("userNum").toString();
		System.out.println("获取学生的人数:"+userNum);
	}
	
	
	
	
	
	
	private StudentBookService studentBookService;
	
	//通过spring容器动态获取spring中的对象
	@Before
	public void init(){
		ApplicationContext  context = 
		new ClassPathXmlApplicationContext("/spring/application*.xml");
		
		studentBookService = 
				(StudentBookService) context.getBean("studentBookServiceImpl");
		
	}
	
	
	//获取热门课程
	@Test
	public void test04() throws IOException{
		String url = "http://tmooc.cn/book_test/getHotBook";
		
		studentBookService.insertBook(url);
		
		
		
		
		/*String resultJSON = 
		Jsoup.connect(url).ignoreContentType(true).execute().body();
		//"{list:[xxxxxx],result:1}"
		String listJSON =  
		objectMapper.readTree(resultJSON).get("list").toString();
		
		//将StringList集合转化为List<StudentBook>
		StudentBook[] books = 
		objectMapper.readValue(listJSON, StudentBook[].class);
		
		List<StudentBook> bookList = 
				Arrays.asList(books);
		
		
		System.out.println(bookList);*/
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
