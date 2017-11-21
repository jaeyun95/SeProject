/**
 * 게시글 일반 검색(by이름) 서블릿
 * 수정일 : 
 * 작성자 : 정은진
 */
package wedeal.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wedeal.bean.BoardDBBean;
import wedeal.bean.BoardDataBean;

@WebServlet("/SearchAction")
public class SearchAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchName = null;  //검색하려는 키워드
		BoardDBBean searchService = BoardDBBean.getinstance();
		ArrayList<BoardDataBean> list = null;  //검색 결과를 가져올 list
		try {  //해당 parameter가 없을 경우
			searchName = request.getParameter("keyword");
		} catch(NullPointerException e) {  //키워드의 내용이 없을 경우
			e.printStackTrace();
			request.setAttribute("error_message", e.getMessage() +"값을 입력해주세요");  //에러 메시지 저장
		}
		try {
			list = searchService.selectProductByName(searchName);  //물품의 이름으로 검색하는 메소드 실행
			request.setAttribute("searchResultList", list);  //검색 결과 리스트 attribute에 저장
		} catch(SQLException e) {
			e.printStackTrace();
			request.setAttribute("error_message",  e.getMessage());
		}
	}
}
