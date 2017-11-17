package wedeal.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CategorySearchAction")
public class CategorySearchAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchName = null;  //검색하려는 키워드
		String searchCategory = null;
		BoardDBBean searchService = BoardDBBean.getInstance();
		ArrayList<BoardDataBean> list = null;  //검색 결과를 가져올 list
		try {  //해당 parameter가 없을 경우
			searchName = request.getParameter("keyword");
			searchCategory = request.getParameter("searchCategory");  //어떤 카테고리를 검사할지
		} catch(NullPointerException e) {  //키워드의 내용이 없을 경우
			e.printStackTrace();
			request.setAttribute("error_message", e.getMessage() +"값을 입력해주세요");  //에러 메시지 저장
			return "error";
		}
		try {
			list = searchService.selectProductByCategory(searchName, searchCategory);  //물품의 이름으로 검색하는 메소드 실행
			request.setAttribute("searchResultList", list);  //검색 결과 리스트 attribute에 저장
		} catch(SQLException e) {
			e.printStackTrace();
			request.setAttribute("error_message",  e.getMessage());
			return "error";
		}
		return "success"; //반환형이 string형이기 때문에 일단 해놈. 상의 ㄱㄱ
	}
	}

}
