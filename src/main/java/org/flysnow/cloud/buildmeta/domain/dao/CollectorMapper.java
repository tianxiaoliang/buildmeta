package org.flysnow.cloud.buildmeta.domain.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.flysnow.cloud.buildmeta.domain.model.Collector;
import org.flysnow.cloud.buildmeta.domain.model.CollectorExample;
import org.flysnow.cloud.buildmeta.domain.model.CollectorWithBLOBs;

public interface CollectorMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int countByExample(CollectorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int deleteByExample(CollectorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int insert(CollectorWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int insertSelective(CollectorWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    List<CollectorWithBLOBs> selectByExampleWithBLOBs(CollectorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    List<Collector> selectByExample(CollectorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    CollectorWithBLOBs selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int updateByExampleSelective(@Param("record") CollectorWithBLOBs record, @Param("example") CollectorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int updateByExampleWithBLOBs(@Param("record") CollectorWithBLOBs record, @Param("example") CollectorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int updateByExample(@Param("record") Collector record, @Param("example") CollectorExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int updateByPrimaryKeySelective(CollectorWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int updateByPrimaryKeyWithBLOBs(CollectorWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collector
     *
     * @mbggenerated Mon Nov 17 14:28:53 CST 2014
     */
    int updateByPrimaryKey(Collector record);
}