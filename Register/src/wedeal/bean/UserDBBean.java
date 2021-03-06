package wedeal.bean;
/**
 * 코드설명
 * 작성자:이재윤
 * 수정자:
 * 최종수정일: 17.11.15
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.tomcat.jdbc.pool.DataSource;

import wedeal.bean.UserDataBean;


public class UserDBBean {
	private Connection conn = null;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private static UserDBBean instance = new UserDBBean();

	public static UserDBBean getinstance() {
		return instance;
	}

	private UserDBBean() {
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

	/*private Connection getConnection() throws Exception{
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp:/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/se");
		return ds.getConnection();
	}*/

	//���� �ð��� ������ �־��ش�.
	public String getDate() {
		String SQL="SELECT NOW()";//���� �ð��� �����ش�.
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

	//id/pw�� �˻��Ҷ� ����ϴ� �޼ҵ� (�α���)
	public int login(String user_id,String user_pw) {
		String SQL = "SELECT user_pw FROM USER WHERE user_id = ?";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(user_pw)) 
					return 1; //로그인
				else
					return 0; //비번이 틀림
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return -1; //아이디 없음
	}

	//check id(join) �ߺ�Ȯ�ο� ���
	public int registerCheck(String user_id) {
		String SQL="SELECT * FROM USER WHERE user_id = ?";

		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();
			//no
			if(rs.next() || user_id.equals("")) {
				return 0;
			}
			//ok
			else{
				return 1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs !=null) rs.close();
				if(pstmt !=null) pstmt.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1;//error
	}

	//add user ȸ�����Կ� ���
	public int register(UserDataBean member) {
		String SQL="INSERT INTO USER VALUES (?, ?, ?, ?, ?, ?, ?)";

		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, member.getUser_name());
			pstmt.setInt(2, member.getUser_age());
			pstmt.setString(3, member.getUser_phone());
			pstmt.setString(4, member.getUser_id());
			pstmt.setString(5, member.getUser_pw());
			pstmt.setString(6, member.getUser_hope());
			pstmt.setString(7, getDate());
			//int result = new likeDAO().create(member.getUser_id());
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs !=null) rs.close();
				if(pstmt !=null) pstmt.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1;//error
	}

	//get user info
	public UserDataBean getUser(String user_id) {
		String sql = "select * from user where user_id=?";
		UserDataBean user = new UserDataBean();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();

			rs.next();
			user.setUser_name(rs.getString("user_name"));
			user.setUser_age(rs.getInt("user_age"));
			user.setUser_phone(rs.getString("user_phone"));
			user.setUser_id(rs.getString("user_id"));
			user.setUser_pw(rs.getString("user_pw"));
			user.setUser_hope(rs.getString("user_hope"));

		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs !=null) rs.close();
				if(pstmt !=null) pstmt.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	//user_phone modify
	public boolean modifyPhone(String user_id, String user_phone) {
		String sql = "update user set user_phone=? where user_id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_phone);
			pstmt.setString(2, user_id);
			pstmt.executeUpdate();

		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	//user_pw modify
	public boolean modifyPasswd(String user_id, String user_pw) {
		String sql = "update user set user_pw=? where user_id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_pw);
			pstmt.setString(2, user_id);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	//user delete
	public boolean deleteUser(String user_id) {
		String sql = "delete from user where user_id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
			pstmt.executeQuery();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	/**
	 * 
	 * @param user_id
	 * @return UserDataBean 관리자가 사용자의 id를 검색하거나 metadata 사용시 이용
	 */
	public UserDataBean getUserByID(String user_id) {
		UserDataBean user = new UserDataBean();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = conn.prepareStatement("select * from user where user_id=?");
			preparedStatement.setString(1, user_id);
			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				user.setUser_name(rs.getString("user_age"));
				user.setUser_age(rs.getInt("user_age"));
				user.setUser_phone(rs.getString("user_phone"));
				user.setUser_id(rs.getString("user_id"));
				user.setUser_pw(rs.getString("user_pw"));
				user.setUser_hope(rs.getString("user_hope_1"));
				user.setUser_date(rs.getString("user_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	/**
	 * 
	 * @param userName
	 * @return User 관리자가 회원을 이름으로 조회하는 메소드
	 */
	public UserDataBean getUserByName(String userName) {
		UserDataBean user = new UserDataBean();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = conn.prepareStatement("select * from user where user_name=?");
			preparedStatement.setString(1, userName);
			rs = preparedStatement.executeQuery();

			if (rs.next()) {
				user.setUser_name(rs.getString("user_name"));
				user.setUser_age(rs.getInt("user_age"));
				user.setUser_phone(rs.getString("user_phone"));
				user.setUser_id(rs.getString("user_id"));
				user.setUser_pw(rs.getString("user_pw"));
				user.setUser_hope(rs.getString("user_hope"));
				user.setUser_date(rs.getString("user_date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}
	/**
	 * 모든 유저의 리스트를 반환하는 메소드
	 * @return
	 */

	public ArrayList<UserDataBean> getAllUser() {
		ArrayList<UserDataBean> list = new ArrayList<UserDataBean>();
		try {
			String sql = "select * from user";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UserDataBean user = new UserDataBean();
				user.setUser_name(rs.getString("name"));
				user.setUser_age(rs.getInt("age"));
				user.setUser_phone(rs.getString("user_phone"));
				user.setUser_id(rs.getString("id"));
				user.setUser_pw(rs.getString("user_pw"));
				user.setUser_hope(rs.getString("hope"));
				user.setUser_date(rs.getString("date"));
				list.add(user);
			}
		} catch (Exception e) {
			System.out.println("getAllUser err : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
			}
		}
		return list;
	}
	/**
	 * 한 회원을 활동 정지 시키는 메소드
	 * @param user_id
	 */
	public void stopUser(String user_id) {
		try {
			String sql = "update user set user_stop = true where user_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
		}catch(Exception e) {
			System.out.println("stopUser err : " + e.getMessage());
		}
		finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 한 회원의 활동 정지 상태를 해지시키는 메소드
	 * @param user_id
	 */
	public void startUser(String user_id) {
		try {
			String sql = "update user set user_stop = false where user_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user_id);
		}catch(Exception e) {
			System.out.println("stopUser err : " + e.getMessage());
		}
		finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	/**
	 * 강제 탈퇴된 회원들의 리스트를 반환하는 메소드
	 * 강제 탈퇴 유저 디비를 만들어야 할 것 같음
	 * 임시로 여기에 둠
	 * @return
	 */
	public ArrayList<UserDataBean> getDeletedUser() {
		ArrayList<UserDataBean> list = new ArrayList<UserDataBean>();
		try {
			String sql = "select * from deleteduser";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UserDataBean user = new UserDataBean();
				user.setUser_name(rs.getString("name"));
				user.setUser_age(rs.getInt("age"));
				user.setUser_phone(rs.getString("user_phone"));
				user.setUser_id(rs.getString("id"));
				user.setUser_pw(rs.getString("user_pw"));
				user.setUser_hope(rs.getString("hope"));
				user.setUser_date(rs.getString("date"));
				list.add(user);
			}
		} catch (Exception e) {
			System.out.println("getAllUser err : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
			}
		}
		return list;
	}
	/**
	 * 활동 정지된 회원들의 리스트를 반환하는 메소드
	 * @return
	 */
	public ArrayList<UserDataBean> getStoppedUser() {
		ArrayList<UserDataBean> list = new ArrayList<UserDataBean>();
		try {
			String sql = "select * from user where user_stop = true";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UserDataBean user = new UserDataBean();
				user.setUser_name(rs.getString("name"));
				user.setUser_age(rs.getInt("age"));
				user.setUser_phone(rs.getString("user_phone"));
				user.setUser_id(rs.getString("id"));
				user.setUser_pw(rs.getString("user_pw"));
				user.setUser_hope(rs.getString("hope"));
				user.setUser_date(rs.getString("date"));
				list.add(user);
			}
		} catch (Exception e) {
			System.out.println("getAllUser err : " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
			}
		}
		return list;
	}
}
