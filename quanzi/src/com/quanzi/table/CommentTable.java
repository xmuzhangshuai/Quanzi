package com.quanzi.table;

public class CommentTable {

	public static final String C_ID = "commentid";//自增

	//确定一条帖子或活动
	public static final String PA_ID = "id";
	public static final String PA_USERID = "userid";
	public static final String COMMENT_TYPE = "type";

	public static final String C_USER_ID = "comment_userid";//主动评论
	public static final String C_USER_AVATAR = "Aavatar";
	public static final String C_USER_NICKNAME = "Anickname";
	public static final String C_USER_GENDER = "Agender";

	public static final String TO_USER_NICKNAME = "Bnickname";
	public static final String TO_USER_ID = "to_userid";//被动评论

	public static final String C_CONTENT = "content";
	public static final String C_TIME = "comment_time";

}
