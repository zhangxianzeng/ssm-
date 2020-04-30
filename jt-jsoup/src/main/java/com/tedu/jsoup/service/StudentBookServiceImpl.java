package com.tedu.jsoup.service;

import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tedu.jsoup.mapper.StudentBookMapper;
import com.tedu.jsoup.mapper.StudentSectionMapper;
import com.tedu.jsoup.pojo.StudentBook;
import com.tedu.jsoup.pojo.StudentSection;
import com.tedu.jsoup.vo.BookList;

@Service
public class StudentBookServiceImpl implements StudentBookService {
	
	@Autowired
	private StudentBookMapper bookMapper;
	
	@Autowired
	private StudentSectionMapper sectionMapper;
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public void insertBook(String url) {
		try {
			//通过url实现JSOUP查询
			String resultJSON = 
			Jsoup.connect(url).ignoreContentType(true).execute().body();
			
			BookList bookList = 
			objectMapper.readValue(resultJSON,BookList.class);
			
			//循环遍历
			for (StudentBook book : bookList.getList()) {
				
				if(book.getSections() !=null){
					//获取章节list集合
					List<StudentSection> sList = 
							book.getSections();
			
					for(StudentSection section :sList){
						//进行主外键对象关联
						section.setBookId(book.getId());
						sectionMapper.insert(section);	
					}
					
				}

				//表示已经将章节信息入库  需要把课程信息入库
				bookMapper.insert(book);
				System.out.println("恭喜你爬虫数据已经成功入库,快乐的过节吧!!!!!");
			}
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	
	
	
	
	
	
	
	

}
