package wedeal.control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wedeal.bean.LikeDBBean;
import wedeal.bean.LikeDataBean;


@WebServlet("/LikeAction")
public class LikeAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public LikeAction() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		LikeDBBean like = LikeDBBean.getinstance();
		LikeDataBean likedt = new LikeDataBean();
		
		likedt.setBoard_num(Integer.parseInt(request.getParameter("board_num")));
		likedt.setUser_id(request.getParameter("user_id"));
		
		if(like.check_id(likedt.getUser_id()) == 1 ) {
			request.getSession().setAttribute("messageType", "알림");
			request.getSession().setAttribute("messageContent", "좋아요 취소");
			response.sendRedirect("board.jsp");
		}
		
		else {
			like.add(likedt);
			request.getSession().setAttribute("messageType", "알림");
			request.getSession().setAttribute("messageContent", "좋아요 누름");
			response.sendRedirect("board.jsp");
		}
			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
