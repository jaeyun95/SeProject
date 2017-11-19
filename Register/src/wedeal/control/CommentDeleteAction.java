package wedeal.control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/CommentDeleteAction")
public class CommentDeleteAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CommentDeleteAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		int comment_num=Integer.parseInt(request.getParameter("comment_num"));
		int board_num = Integer.parseInt(request.getParameter("board_num"));
		int result = new commentDAO().delete(comment_num);
		
		if(result == -1) {
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "댓글 삭제를 실패했습니다.");
			response.sendRedirect("view.jsp?board_num="+board_num);
			return;
		}
		else {
			request.getSession().setAttribute("messageType", "성공 메시지");
			request.getSession().setAttribute("messageContent", "댓글 삭제를 성공했습니다.");
			response.sendRedirect("view.jsp?board_num="+board_num);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
