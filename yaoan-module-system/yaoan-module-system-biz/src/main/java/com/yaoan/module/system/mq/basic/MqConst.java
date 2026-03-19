package com.yaoan.module.system.mq.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author doujiale
 * @program gpmall-basic
 * @description 消息队列常量
 */
public interface MqConst {

    /**
     * rabbit mq 的配置 <em color = "white"> rmq队列名 由合同方指定，为保证统一性，以product为前缀 </em>
     */
    class RabbitMq {

        /**
         * 供应商队列
         */
        @Getter
        @AllArgsConstructor
        public enum SupplierQueue {

            /**
             * 供应商注册同步
             */
            REGISTER(SupplierQueueConst.REGISTER_CONST, "供应商注册同步"),

            /**
             * 供应商删除
             */
            REMOVE(SupplierQueueConst.REMOVE_CONST, "供应商删除"),

            /**
             * 供应商修改
             */
            UPDATE(SupplierQueueConst.UPDATE_CONST, "供应商修改"),

            /**
             * 添加用户
             */
            USER_REGISTER(SupplierQueueConst.USER_REGISTER_CONST, "添加用户"),

            /**
             * 用户修改
             */
            USER_UPDATE(SupplierQueueConst.USER_UPDATE_CONST, "用户修改"),

            /**
             * 用户删除
             */
            USER_REMOVE(SupplierQueueConst.USER_REMOVE_CONST, "用户删除"),

            /**
             * ？？？
             */
            SHORT_LIST(SupplierQueueConst.SHORT_LIST_CONST, "???"),
            ;

            private final String code;
            private final String value;
        }

        /**
         * 供应商 队列 常量
         */
        public abstract static class SupplierQueueConst {

            /**
             * 供应商注册同步
             */
            public static final String REGISTER_CONST = "contract.productagency.basic.gpbs.supplier.register";

            /**
             * 供应商删除
             */
            public static final String REMOVE_CONST = "contract.productagency.basic.gpbs.supplier.remove";

            /**
             * 供应商修改
             */
            public static final String UPDATE_CONST = "contract.productagency.basic.gpbs.supplier.update";

            /**
             * 添加用户
             */
            public static final String USER_REGISTER_CONST = "contract.productagency.basic.gpbs.supplier.user.new";

            /**
             * 用户修改
             */
            public static final String USER_UPDATE_CONST = "contract.productagency.basic.gpbs.supplier.user.update";

            /**
             * 用户删除
             */
            public static final String USER_REMOVE_CONST = "contract.productagency.basic.gpbs.supplier.user.remove";

            /**
             * ？？？
             */
            public static final String SHORT_LIST_CONST = "contract.productagency.basic.gpbs.supplier.short.list";
        }

        /**
         * 采购人 队列
         */
        @Getter
        @AllArgsConstructor
        public enum BuyManQueue {

            /**
             * 采购人注册同步
             */
            REGISTER(BuyManQueueConst.REGISTER_CONST, "采购人注册同步"),

            /**
             * 采购人删除
             */
            REMOVE(BuyManQueueConst.REMOVE_CONST, "采购人删除"),

            /**
             * 采购人修改
             */
            UPDATE(BuyManQueueConst.UPDATE_CONST, "采购人修改"),

            /**
             * 添加用户
             */
            USER_REGISTER(BuyManQueueConst.USER_REGISTER_CONST, "添加用户"),

            /**
             * 用户修改
             */
            USER_UPDATE(BuyManQueueConst.USER_UPDATE_CONST, "用户修改"),

            /**
             * 用户删除
             */
            USER_REMOVE(BuyManQueueConst.USER_REMOVE_CONST, "用户删除"),

            ;


            private final String code;
            private final String value;
        }

        /**
         * 采购人 队列 常量
         */
        public abstract static class BuyManQueueConst {
            /**
             * 采购人注册同步
             */
            public static final String REGISTER_CONST = "contract.productagency.basic.gpbp.buyman.register";

            /**
             * 采购人删除
             */
            public static final String REMOVE_CONST = "contract.productagency.basic.gpbp.buyman.remove";

            /**
             * 采购人修改
             */
            public static final String UPDATE_CONST = "contract.productagency.basic.gpbp.buyman.update";

            /**
             * 添加用户
             */
            public static final String USER_REGISTER_CONST = "contract.productagency.basic.gpbp.buyman.user.new";

            /**
             * 用户修改
             */
            public static final String USER_UPDATE_CONST = "contract.productagency.basic.gpbp.buyman.user.update";

            /**
             * 用户删除
             */
            public static final String USER_REMOVE_CONST = "contract.productagency.basic.gpbp.buyman.user.remove";
        }

        /**
         * manager 队列
         */
        @Getter
        @AllArgsConstructor
        public enum ManagerQueue {

            /**
             * manager 注册同步
             */
            REGISTER(ManagerQueueConst.REGISTER_CONST, "manager 注册同步"),

            /**
             * 管理员用户更新
             */
            UPDATE(ManagerQueueConst.UPDATE_CONST, "管理员用户更新"),

            ;


            private final String code;
            private final String value;
        }

        /**
         * 实施人员队列 常量
         */
        public abstract static class ManagerQueueConst {

            /**
             * manager 注册同步
             */
            public static final String REGISTER_CONST = "contract.productagency.basic.menu.implementer.insert";

            /**
             * 管理员用户更新
             */
            public static final String UPDATE_CONST = "contract.productagency.basic.menu.implementer.update";

        }
    }

    interface SupervisionQueue {
        /**
         * 监管注册同步
         */
        String registerQueue = "contract.productagency.basic.gpbs.Supervision.register";

        /**
         * 监管删除
         */
        String removeQueue = "contract.productagency.basic.gpbs.Supervision.remove";

        /**
         * 监管修改
         */
        String updateQueue = "contract.productagency.basic.gpbs.Supervision.update";


        /**
         * 添加用户
         */
        String userRegisterQueue = "contract.productagency.basic.gpbs.Supervision.user.new";

        /**
         * 用户修改
         */
        String userUpdateQueue = "contract.productagency.basic.gpbs.Supervision.user.update";

        /**
         * 用户删除
         */
        String userDeleteQueue = "contract.productagency.basic.gpbs.Supervision.user.remove";

        /**
         * 用户删除
         */
        String shortlist = "contract.productagency.basic.gpbs.Supervision.short.list";
    }

}