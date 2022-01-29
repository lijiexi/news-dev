package com.ljx.enums;

/**
 * @Desc: 文章审核状态 枚举
 * 文章状态：
 *      1：审核中（用户已提交），
 *      2：机审结束，等待人工审核，
 *      3：审核通过（已发布），
 *      4：审核未通过
 *      5：文章撤回
 *
 *      1/2 可以统一归类为 [正在审核]状态
 */
public enum ArticleReviewStatus {
    REVIEWING(1, "审核中（用户已提交）"),
    WAITING_MANUAL(2, "机审结束，等待人工审核"),
    SUCCESS(3, "审核通过（已发布）"),
    FAILED(4, "审核未通过"),
    WITHDRAW(5, "文章撤回");

    public final Integer type;
    public final String value;

    ArticleReviewStatus(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * 判断传入的审核状态是不是有效的值
     * @param tempStatus
     * @return
     */
    public static boolean isArticleStatusValid(Integer tempStatus) {
        if (tempStatus != null) {
            if (tempStatus == REVIEWING.type
                    || tempStatus == WAITING_MANUAL.type
                    || tempStatus == SUCCESS.type
                    || tempStatus == FAILED.type
                    || tempStatus == WITHDRAW.type) {
                return true;
            }
        }
        return false;
    }
}
