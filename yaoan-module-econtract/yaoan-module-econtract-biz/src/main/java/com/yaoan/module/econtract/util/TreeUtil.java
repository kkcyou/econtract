package com.yaoan.module.econtract.util;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewPointsTypeRespVO;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {


    /**
     * 搜索树形结构数据
     * 1. 如果当前搜索值存在，那么不管子集存不存在，当前数据都需要查出来
     * 2. 如果当前搜索值不存在，同时他的子集存在，那么当前包含子集的数据都需要查出来
     * 3. 如果当前搜索值不存在，并且子集也不存在，那么当前数据需要移除掉
     */
    public static List<ReviewPointsTypeRespVO> childSearchName(List<ReviewPointsTypeRespVO> list, String search) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 默认当前搜索含有该值
        for (int i = 0; i < list.size(); i++) {
            ReviewPointsTypeRespVO item = list.get(i);
            if (item.getName().contains(search)) {
                // 当前搜索含有该值
                childSearch(item.getTypeList(), search, true);
            } else {
                // 当前搜索不含有该值
                boolean flag = childSearch(item.getTypeList(), search, false);
                // 下级没有搜索到
                if (!flag) {
                    // 移除
                    list.remove(i);
                    i--;
                }
            }
        }
        return list;
    }

    /**
     * 递归搜索值操作
     */
    private static boolean childSearch(List<ReviewPointsTypeRespVO> list, String search, boolean flag) {
        if (flag) {
            // 如果父级存在，子集不需要判断了
            return true;
        }
        if (CollectionUtils.isEmpty(list)) {
            return flag;
        }
        // 本次搜索默认不存在
        boolean newFlag = false;
        for (int i = 0; i < list.size(); i++) {
            // 当前搜索值是否存在
            boolean thisSearchFlag;
            ReviewPointsTypeRespVO item = list.get(i);
            if (item.getName().contains(search)) {
                // 当前搜索值存在
                thisSearchFlag = childSearch(item.getTypeList(), search, true);
            } else {
                // 当前搜索值不存在
                thisSearchFlag = childSearch(item.getTypeList(), search, flag);
                // 下级没有搜索到
                if (!thisSearchFlag) {
                    // 移除
                    list.remove(i);
                    i--;
                }
            }
            // 如果上一级不存在则跟随当前搜索结果
            if (thisSearchFlag) {
                newFlag = true;
            }
        }
        // 如果上次搜索不存在则用当前搜索结果
        if (!flag) {
            flag = newFlag;
        }
        return flag;
    }

    /**
     * 获取子集
     */
    public static List<ReviewPointsTypeRespVO> getChild(String parentId, List<ReviewPointsTypeRespVO> list) {
        if (CollectionUtils.isEmpty(list) || parentId == null) {
            return null;
        }
        // 组装新的树形结构数据
        List<ReviewPointsTypeRespVO> newList = new ArrayList<>();
        for (ReviewPointsTypeRespVO child : list) {
            // 如果父级内码是当前内码，就继续
            if (parentId.equals(child.getParentId())) {
                // 寻找下级
                child.setTypeList(getChild(child.getId(), list));
                newList.add(child);
            }
        }
        return newList;
    }
}
