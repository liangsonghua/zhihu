package com.liangsonghua.dao;

import com.liangsonghua.model.Comment;
import org.apache.ibatis.annotations.*;



import java.util.List;


/**
 * Created by liangsonghua on 16-7-24.
 */
@Mapper
public interface CommentDAO {
        // 注意空格
         String TABLE_NAME =" comment ";
        String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
        String SELECT_FIELDS = " id, " + INSERT_FIELDS;
        @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
                ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
        int addComment(Comment comment);

        @Update({"update ", TABLE_NAME, " set status=#{status} where entityId=#{entityId} and entityType=#{entityType}"})
        void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);

        @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
                " where entityId=#{entityId} and entityType=#{entityType} order by id desc"})
        List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

        @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
                " where id=#{id} "})
        Comment getCommentId(@Param("id") int id);

        @Select({"select count(id) from ", TABLE_NAME, " where entityId=#{entityId} and entityType=#{entityType} "})
        int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

        @Select({"select count(id) from ",TABLE_NAME,"where userId=#{userId}"})
        int getUserCommentCount(@Param("userId") int userId);
}
