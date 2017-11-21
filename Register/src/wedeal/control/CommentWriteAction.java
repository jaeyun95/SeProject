package wedeal.control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wedeal.bean.CommentDBBean;
import wedeal.bean.CommentDataBean;


@WebServlet("/CommentWriteAction")
public class CommentWriteAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public CommentWriteAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		CommentDBBean comment = CommentDBBean.getinstance();
		CommentDataBean commentdt = new CommentDataBean();
		
		commentdt.setCate_num(Integer.parseInt(request.getParameter("cate_num")));
		commentdt.setBoard_num(Integer.parseInt(request.getParameter("board_num")));
		commentdt.setComment_content(request.getParameter("comment_content"));
		commentdt.setUser_id(request.getParameter("user_id"));

		
		if(commentdt.getComment_content() == null || commentdt.getComment_content().equals("")){
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "모든 내용을 입력하세요.");
			response.sendRedirect("view.jsp");
			return;
		}
		
		else {
		int result = comment.write(commentdt);
		
		if(result == -1) {
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "댓글 작성 실패");
			response.sendRedirect("view.jsp?board_num="+commentdt.getBoard_num());
			return;
		}
		else
			response.sendRedirect("view.jsp?board_num="+commentdt.getBoard_num());
		}
	}
}
