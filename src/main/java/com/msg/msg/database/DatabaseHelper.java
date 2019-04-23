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

	public static int getUsersCount() {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tseam_six_3.user");) {
			int count = 0;
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				count = rs.getInt("COUNT(*)");
			}
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static int getSentMsgCount(int id) {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn
						.prepareStatement("SELECT COUNT(*) FROM tseam_six_3.message WHERE fk_sender_id=?");) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
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
			int count = 0;
			if (rs.next()) {
				count = rs.getInt("COUNT(*)");
			}
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static int getUsersMsgCount(int senderId, int receiverId) {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM tseam_six_3.message "
						+ "WHERE fk_receiver_id=? AND fk_sender_id=? OR fk_receiver_id=? AND fk_sender_id=?");) {
			ps.setInt(1, receiverId);
			ps.setInt(2, senderId);
			ps.setInt(3, senderId);
			ps.setInt(4, receiverId);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt("COUNT(*)");
			}
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static int getUnreadMsgCount(int receiverId) {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"SELECT COUNT(*) FROM tseam_six_3.message WHERE fk_receiver_id=? AND is_read=0");) {
			ps.setInt(1, receiverId);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt("COUNT(*)");
			}
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static int getTrainersReviewsCount(int id) {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM review,training_session,user "
						+ "WHERE review.fk_session_id = idtraining_session AND training_session.fk_trainer_id = user.iduser AND iduser = ?");) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt("COUNT(*)");
			}
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static int getTrainersCountByTypeAndArea(int typeId, int areaId) {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT COUNT(iduser)\r\n"
						+ " FROM user, trainer_area, trainer_specialization, area ,training_type\r\n"
						+ "			WHERE user.iduser=trainer_specialization.fk_trainer_id AND user.iduser=trainer_area.fk_trainer_id AND \r\n"
						+ "			training_type.idtraining_type=trainer_specialization.fk_training_type AND area.idarea=trainer_area.fk_area_id\r\n"
						+ "			AND training_type.idtraining_type =? AND idarea =?;");) {
			ps.setInt(1, typeId);
			ps.setInt(2, areaId);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("COUNT(iduser)");
			}
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static int getTrainersCountByArea(int areaId) {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT COUNT(iduser)\r\n"
						+ " FROM user,trainer_area,area WHERE iduser=fk_trainer_id AND fk_area_id=idarea AND idarea=?");) {
			ps.setInt(1, areaId);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("COUNT(iduser)");
			}
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	public static int getTrainersCountByType(int typeId) {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT COUNT(iduser)\r\n"
						+ " FROM user,trainer_specialization,training_type "
						+ "WHERE iduser=fk_trainer_id AND fk_training_type=idtraining_type AND idtraining_type =?");) {
			ps.setInt(1, typeId);
			ResultSet rs = ps.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("COUNT(iduser)");
			}
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	public static int getTrainersCount() {
		try (Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM user WHERE fk_role_id = 2");) {
			ResultSet rs = ps.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt("COUNT(*)");
			}
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
