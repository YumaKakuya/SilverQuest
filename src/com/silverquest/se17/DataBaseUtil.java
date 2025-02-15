package com.silverquest.se17;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseUtil {

	// データベース接続情報
	private static final String URL = "jdbc:postgresql://localhost:5432/SilverQuest";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgrestest";

	/**
	 * データベース接続メソッド
	 *
	 * @return Connectionオブジェクト
	 * @throws SQLException SQLエラーが発生した場合
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	/**
	 * SELECT専用メソッド
	 *
	 * @param query   実行するSQLクエリ
	 * @param handler 結果セットの処理ロジック
	 */
	public static void executeQuery(String query, QueryHandler handler) {
		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			handler.handle(rs);

		} catch (Exception e) {
			System.out.println("エラー: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 更新系クエリ (INSERT, UPDATE, DELETE)
	 *
	 * @param query 実行するSQLクエリ
	 */
	public static void executeUpdate(String query) {
		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement()) {

			int affectedRows = stmt.executeUpdate(query);
			System.out.println("影響を受けた行数: " + affectedRows);

		} catch (SQLException e) {
			System.out.println("エラー: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * PreparedStatementを使用した更新系クエリ (INSERT, UPDATE, DELETE)
	 *
	 * @param query  実行するSQLクエリ
	 * @param params プレースホルダにバインドするパラメータ
	 */
	public static void executePreparedUpdate(String query, Object... params) {
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)) {

			// パラメータを設定
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}

			int affectedRows = pstmt.executeUpdate();
			System.out.println("影響を受けた行数: " + affectedRows);

		} catch (SQLException e) {
			System.out.println("エラー: データベース操作中に問題が発生しました。");
			e.printStackTrace(); // デバッグ用（本番環境では適切にログを残す）
			throw new RuntimeException("データベース操作に失敗しました。"); // 明示的に例外を再スロー
		}
	}


}
