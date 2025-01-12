package com.silverquest.se17;

import java.text.Normalizer;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // **冒頭メッセージ**
            System.out.println("新しい問題を追加します。以下の指示に従って入力してください。");

            // 章番号の入力
            System.out.print("章番号を入力してください: ");
            int chapter = getIntInput(scanner, "章番号は整数で入力してください。");

            // 問題番号の入力
            System.out.print("問題番号を入力してください: ");
            int questionNumber = getIntInput(scanner, "問題番号は整数で入力してください。");

            // 問題文の入力（複数行）
            System.out.println("問題文を入力してください（改行で入力を続け、ENDのみの行で終了）:");
            String questionText = getMultilineInput(scanner);

            // 選択肢の入力（複数行）
            System.out.println("選択肢を入力してください（改行で入力を続け、ENDのみの行で終了）:");
            String choices = getMultilineInput(scanner);

            // 正解の入力
            System.out.print("正解を入力してください: ");
            String correctAnswer = scanner.nextLine().trim();

            // 解説文の入力（複数行）
            System.out.println("解説を入力してください（改行で入力を続け、ENDのみの行で終了）:");
            String explanation = getMultilineInput(scanner);

            // INSERTクエリを実行
            String insertQuery = "INSERT INTO Questions (chapter, question_text, correct_answer, explanation) VALUES (?, ?, ?, ?)";
            try {
                DataBaseUtil.executePreparedUpdate(
                        insertQuery,
                        chapter,
                        questionText + "\n" + choices, // 問題文＋選択肢をまとめて格納
                        correctAnswer,
                        explanation
                );
                System.out.println("問題が追加されました！");
            } catch (RuntimeException e) {
                System.out.println("問題の追加に失敗しました。原因: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("エラーが発生しました: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    /**
     * 整数入力を取得するメソッド
     * - 全角数字も受け付けたいので正規化を行う
     * - 変換できなければエラーメッセージを出して再入力
     */
    private static int getIntInput(Scanner scanner, String errorMsg) {
        while (true) {
            String input = scanner.nextLine().trim();
            // 全角 -> 半角への正規化 (NFKC)
            String normalized = Normalizer.normalize(input, Normalizer.Form.NFKC);
            try {
                return Integer.parseInt(normalized);
            } catch (NumberFormatException e) {
                System.out.println(errorMsg);
                System.out.print("もう一度入力してください: ");
            }
        }
    }

    /**
     * 複数行の入力を取得するメソッド
     * - "END" のみが入力されたら入力終了
     * - 最後は trim() で余計な改行を除去
     */
    private static String getMultilineInput(Scanner scanner) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = scanner.nextLine();
            if ("END".equalsIgnoreCase(line.trim())) {
                break;
            }
            sb.append(line).append("\n");
        }
        return sb.toString().trim();
    }
}
