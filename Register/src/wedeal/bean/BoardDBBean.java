package wedeal.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import board.boardDTO;

public class BoardDBBean {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	private static BoardDBBean instance = new BoardDBBean();
	
	public static BoardDBBean getinstance() {
		return instance;
	}
	
	private BoardDBBean() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/se?autoReconnect=true&useSSL=false";
			String dbID = "jy";
			String dbPW = "1365";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPW);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//현재 시간을 서버에 넣어준다.
	public String getDate() {
		String SQL="SELECT NOW()";//현재 시간을 돌려준다.
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			rs=pstmt.executeQuery();
			if(rs.next())
				return rs.getString(1);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//작성될 글 번호 구하기
	public int getNext() {
		String SQL="SELECT board_num FROM board ORDER BY board_num DESC";
		
		try {
			pstmt=conn.prepareStatement(SQL);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1)+1;
			}
			return 1;//현재가 첫번째 게시글인 경우
		}catch(Exception e) {
			e.printStackTrace();	
			}
		return -1;
	}
	
	//DB에 입력 글쓰기
	public int write(BoardDataBean board) {
		String SQL="INSERT INTO board VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, board.getCate_num());
			pstmt.setInt(2, getNext());
			pstmt.setString(3, board.getBoard_title());
			pstmt.setInt(4, board.getBoard_price());
			pstmt.setString(5, board.getUser_id());
			pstmt.setString(6, getDate());
			pstmt.setString(7, board.getBoard_content());
			pstmt.setString(8, board.getBoard_image());
			pstmt.setString(9, board.getBoard_path());
			pstmt.setInt(10, 0); //조회수
			pstmt.setInt(11, 1); //삭제확인
			pstmt.setInt(12, 0); //좋아요 수
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}	
	
	
	//전체 게시글 list로 출력
	public ArrayList<BoardDataBean> getList(int pageNumber){
		String SQL="SELECT * FROM board WHERE board_num < ? AND board_available = 1 ORDER BY board_num DESC LIMIT 8";
		ArrayList<BoardDataBean> list = new ArrayList<BoardDataBean>();
		
		try {
				PreparedStatement pstmt=conn.prepareStatement(SQL);
				pstmt.setInt(1, getNext()-(pageNumber-1)*8);
				rs=pstmt.executeQuery();

			while(rs.next()) {
				BoardDataBean board = new BoardDataBean();
				board.setCate_num(rs.getInt(1));
				board.setBoard_num(rs.getInt(2));
				board.setBoard_title(rs.getString(3));
				board.setBoard_price(rs.getInt(4));
				board.setUser_id(rs.getString(5));
				board.setBoard_date(rs.getString(6));
				board.setBoard_content(rs.getString(7));
				board.setBoard_image(rs.getString(8));
				board.setBoard_path(rs.getString(9));
				board.setBoard_hit(rs.getInt(10));
				board.setBoard_available(rs.getInt(11));
				board.setBoard_like(rs.getInt(12));
				list.add(board);
			}
			return list;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//페이징 처리를 위한 nextpage함수
	public boolean nextPage(int pageNumber) {
		String SQL="SELECT * FROM board WHERE board_num < ? AND board_available = 1 ORDER BY board_num DESC LIMIT 8";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext()-(pageNumber-1)*8);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//조회수 늘리기
	public int count(int board_num,int board_hit) {
		String SQL="UPDATE board SET board_hit = ? WHERE board_num = ?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, board_hit);
			pstmt.setInt(2, board_num);
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public BoardDataBean getBoard(int board_num) {
		String SQL="SELECT * FROM board WHERE board_num = ? AND board_available = 1";
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, board_num);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				BoardDataBean board = new BoardDataBean();
				board.setCate_num(rs.getInt(1));
				board.setBoard_num(rs.getInt(2));
				board.setBoard_title(rs.getString(3));
				board.setBoard_price(rs.getInt(4));
				board.setUser_id(rs.getString(5));
				board.setBoard_date(rs.getString(6));
				board.setBoard_content(rs.getString(7));
				board.setBoard_image(rs.getString(8));
				board.setBoard_path(rs.getString(9));
				board.setBoard_hit(rs.getInt(10));
				board.setBoard_available(rs.getInt(11));
				board.setBoard_like(rs.getInt(12));
				count(board.getBoard_num(),board.getBoard_hit()+1);
				return board;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//현재 게시글의 총 개수를 세준다.
	public int allCount(int cate_num) {
		String SQL1 = "SELECT COUNT(*) FROM board WHERE board_available = 1";
		String SQL2 = "SELECT COUNT(*) FROM board WHERE cate_num = ? AND board_available = 1";
		try {
			if(cate_num == 0) {
				PreparedStatement pstmt=conn.prepareStatement(SQL1);
				rs=pstmt.executeQuery();
			}
			else {
				PreparedStatement pstmt=conn.prepareStatement(SQL2);
				pstmt.setInt(1, cate_num);
				rs=pstmt.executeQuery();
			}
			if(rs.next()) {
				return rs.getInt(1);
			}
			else 
				return 0; //없을 경우
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;//오류
	}
	
	//수정시 사용
	public int update(BoardDataBean board) {
		String SQL="UPDATE board SET board_title = ?, board_price = ?, board_content = ?, board_image = ?, board_path = ? WHERE board_num = ?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setString(1, board.getBoard_title());
			pstmt.setInt(2, board.getBoard_price());
			pstmt.setString(3, board.getBoard_content());
			pstmt.setString(4, board.getBoard_image());
			pstmt.setString(5, board.getBoard_path());
			pstmt.setInt(6, board.getBoard_num());
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	//삭제시 사용
	public int delete(int board_num) {
		String SQL="UPDATE board SET board_available = ? WHERE board_num = ?";
		
		try {
			PreparedStatement pstmt=conn.prepareStatement(SQL);
			pstmt.setInt(1, 0);
			pstmt.setInt(2, board_num);
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	//삭제된 board list를 불러온다
		public ArrayList<BoardDataBean> delete_getList(){
			String SQL="SELECT * FROM board WHERE board_available = 0 ORDER BY board_num ";
			ArrayList<BoardDataBean> list = new ArrayList<BoardDataBean>();
			
			try {
					PreparedStatement pstmt=conn.prepareStatement(SQL);
					rs=pstmt.executeQuery();

				while(rs.next()) {
					BoardDataBean board = new BoardDataBean();
					board.setCate_num(rs.getInt(1));
					board.setBoard_num(rs.getInt(2));
					board.setBoard_title(rs.getString(3));
					board.setBoard_price(rs.getInt(4));
					board.setUser_id(rs.getString(5));
					board.setBoard_date(rs.getString(6));
					board.setBoard_content(rs.getString(7));
					board.setBoard_image(rs.getString(8));
					board.setBoard_path(rs.getString(9));
					board.setBoard_hit(rs.getInt(10));
					board.setBoard_available(rs.getInt(11));
					board.setBoard_like(rs.getInt(12));
					list.add(board);
				}
				return list;
			}catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}
}