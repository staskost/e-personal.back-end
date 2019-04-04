package com.msg.msg.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class DatabaseHelper {

	public DatabaseHelper() {
	}

	public static Connection getConnection() {
		Connection conn = null;

		try {
			Properties connectionProps = new Properties();
			connectionProps.put("user", "root");
			connectionProps.put("password", "konnos1987");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/tseam_six_3?zeroDateTimeBehavior=convertToNull&characterEncoding=utf-8&autoReconnect=true",
					connectionProps);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}
	
	public static int getSentMsgCount(int id) {
	try (Connection conn = getConnection();
			PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM tseam_six_3.message WHERE fk_sender_id=?");) {
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		int count  = 0;
		if(rs.next()) {
			count = rs.getInt("COUNT(*)");
		}
		return count;
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage(), e);
	}
}
	
	public static int getInboxMsgCount(int id) {
	try (Connection conn = getConnection();
			PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM tseam_six_3.message WHERE fk_receiver_id=?");) {
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		int count  = 0;
		if(rs.next()) {
			count = rs.getInt("COUNT(*)");
		}
		return count;
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage(), e);
	}
}
	
	public static int getUsersMsgCount(int senderId, int receiverId) {
	try (Connection conn = getConnection();
			PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM tseam_six_3.message "
							+ "WHERE fk_receiver_id=? AND fk_sender_id=? OR fk_receiver_id=? AND fk_sender_id=?");) {
		ps.setInt(1, receiverId);
		ps.setInt(2, senderId);
		ps.setInt(3, senderId);
		ps.setInt(4, receiverId);
		ResultSet rs = ps.executeQuery();
		int count  = 0;
		if(rs.next()) {
			count = rs.getInt("COUNT(*)");
		}
		return count;
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage(), e);
	}
}
	
	public static int getTrainersReviews(int id) {
	try (Connection conn = getConnection();
			PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM review,training_session,user "
							+ "WHERE review.fk_session_id = idtraining_session AND iduser = ?");) {
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		int count  = 0;
		if(rs.next()) {
			count = rs.getInt("COUNT(*)");
		}
		return count;
	} catch (Exception e) {
		throw new RuntimeException(e.getMessage(), e);
	}
}

//	public static Token createToken(User user) {
//		try (Connection conn = getConnection();
//				PreparedStatement ps = conn
//						.prepareStatement("insert into token (alphanumeric, iduser) values (?,?)");) {
//			String alphanumeric = UUID.randomUUID().toString();
//			ps.setString(1, alphanumeric);
//			ps.setInt(2, user.getId());
//			Token newToken = new Token(alphanumeric, user);
//			ps.executeUpdate();
//			return newToken;
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}

//	public static int getUserIDFromTokenAlphaNumeric(String alphanumeric) {
//		try (Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement("select iduser from token where alphanumeric=?");) {
//			ps.setString(1, alphanumeric);
//			ResultSet rs = ps.executeQuery();
//			int userId = 0;
//			while (rs.next()) {
//				userId = rs.getInt("iduser");
//			}
//			return userId;
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}

//	public static void logOutUser(String alphanumeric) {
//		try (Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement("delete from token where alphanumeric=?");) {
//			ps.setString(1, alphanumeric);
//			ps.executeUpdate();
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}

//	public static void trainerArea(int trainerId, int areaId) {
//		try (Connection conn = getConnection();
//				PreparedStatement ps = conn
//						.prepareStatement("insert into trainer_area (fk_trainer_id, fk_area_id) values(?,?)");) {
//			ps.setInt(1, trainerId);
//			ps.setInt(2, areaId);
//			ps.executeUpdate();
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}
//
//	public static void trainerSpecialization(int trainerId, int typeId) {
//		try (Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement(
//						"insert into trainer_specialization (fk_trainer_id, fk_training_type) values(?,?)");) {
//			ps.setInt(1, trainerId);
//			ps.setInt(2, typeId);
//			ps.executeUpdate();
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}

//	public static void cancelSession(int sessionId) {
//		try (Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement(
//						"delete from training_session where idtraining_session = ?");) {
//			ps.setInt(1, sessionId);
//			ps.executeUpdate();
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}
	
//	public static void removeArea(int trainerId, int areaId) {
//		try (Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement(
//						"delete from trainer_area where fk_trainer_id = ? and fk_area_id = ?");) {
//			ps.setInt(1, trainerId);
//			ps.setInt(2, areaId);
//			ps.executeUpdate();
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}
//	
//	public static void removeType(int trainerId, int typeId) {
//		try (Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement(
//						"delete from trainer_specialization where fk_trainer_id = ? and fk_training_type = ?");) {
//			ps.setInt(1, trainerId);
//			ps.setInt(2, typeId);
//			ps.executeUpdate();
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//	}
	
}
