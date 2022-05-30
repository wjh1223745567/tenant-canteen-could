// 组织权限
const orgPermission = {
    name: '人员管理',
    permissionType: 1,
    permission: 'ORG_ALL',
    children: [
        {
            name: '人员列表',
            permission: 'ORG_EMP'
        },
        {
            name: '新增人员',
            permission: 'ORG_EMP_ADD'
        },
        {
            name: '修改人员',
            permission: 'ORG_EMP_EDIT'
        },
        {
            name: '删除人员',
            permission: 'ORG_EMP_DELETE'
        },
        {
            name: '新增组织',
            permission: 'ORG_ADD'
        },
        {
            name: '修改组织',
            permission: 'ORG_EDIT'
        },
        {
            name: '删除组织',
            permission: 'ORG_DELETE'
        }
    ]
}

const menuPermission = {
    name: '菜谱管理',
    permission: 'MENU_ALL',
    permissionType: 1,
    ignore: false,
    children: [
        {
            name: '菜品管理',
            permission: 'MENU_PRODUCT'
        },
        {
            name: '新增/编辑菜品',
            permission: 'MENU_PRODUCT_EDIT'
        },
        {
            name: '删除菜品',
            permission: 'MENU_PRODUCT_DELETE'
        },
        {
            name: '菜品上架/下架',
            permission: 'MENU_PRODUCT_TOGGLE'
        },
        {
            name: '菜谱管理',
            permission: 'MENU'
        },
        {
            name: '本周菜谱',
            permission: 'MENU_EDIT'
        },
        {
            name: '原料管理',
            permission: 'MATERIAL'
        },
        {
            name: '原料添加/编辑/删除',
            permission: 'MATERIAL_EDIT'
        }
        ,
        {
            name: '食谱百科',
            permission: 'DISH_ALL'
        },
        {
            name: '食谱添加/编辑/删除',
            permission: 'DISH_EDIT'
        }
    ]
}

const canteenPermission = {
    name: '食堂管理',
    permission: 'CANTEEN_ALL',
    permissionType: 1,
    ignore: false,
    children: [
        {
            name: '食堂设置',
            permission: 'CANTEEN_SETTING'
        },
        {
            name: '食堂实况',
            permission: 'CANTEEN_LIVE'
        },
        {
            name: '子食堂',
            permission: 'CANTEEN_SUB'
        },
        {
            name: '新建/编辑/删除子食堂',
            permission: 'CANTEEN_SUB_EDIT'
        }
    ]
}

const equipmentPermission = {
    name: '设备管理',
    permission: 'CANTEEN_DEVICE',
    permissionType: 1,
    ignore: false,
    children: [
        {
            name: '通行设备',
            permission: 'DEVICE_PASSAGE'
        },
        {
            name: '新增/编辑/删除通行设备',
            permission: 'DEVICE_PASSAGE_EDIT'
        },
        {
            name: '摄像头',
            permission: 'DEVICE_CAMERA'
        }, {
            name: '新增/编辑/删除摄像头',
            permission: 'DEVICE_CAMERA_EDIT'
        },
        {
            name: '物联感知设备',
            permission: 'DEVICE_LORA_SENSOR'
        },
        {
            name: '新增/编辑/删除物联感知设备',
            permission: 'DEVICE_LORA_SENSOR_EDIT'
        },
        {
            name: '考勤设备',
            permission: 'DEVICE_ATTENDANCE'
        },
        {
            name: '新增/编辑/删除考勤设备',
            permission: 'DEVICE_ATTENDANCE_EDIT'
        },
        {
            name: 'AIBox设备',
            permission: 'DEVICE_AIBOX'
        },
        {
            name: '新增/编辑/删除AIBox设备',
            permission: 'DEVICE_AIBOX_EDIT'
        },
        {
            name: 'TCP终端',
            permission: 'TCP_CLIENT'
        },
        {
            name: '新增/编辑/删除TCP终端',
            permission: 'TCP_CLIENT_EDIT'
        }
    ]
}

const reservedRoom = {
    name: '订房订餐',
    permission: 'ROOM_RESERVATION_ALL',
    permissionType: 1,
    ignore: false,
    children: [
        {
            name: '包间管理',
            permission: 'ROOM_RESERVATION_ROOM_ALL'
        },
        {
            name: '包间预定审核',
            permission: 'ROOM_RESERVATION_APPLY_ALL'
        }
    ]
}

const kitchenPermission = {
    name: '后厨管理',
    permission: 'KITCHEN_ALL',
    permissionType: 1,
    ignore: false,
    children: [
        {
            name: '花名册',
            permission: 'KITCHEN_EMP',
        },
        {
            name: '修改花名册',
            permission: 'KITCHEN_EMP_EDIT'
        },
        {
            name: '员工关怀',
            permission: 'KITCHEN_EMP_CARE'
        },
        {
            name: '规章制度',
            permission: 'KITCHEN_RULE'
        },
        {
            name: '考核管理',
            permission: 'KITCHEN_ASSESS'
        },
        {
            name: '考勤记录',
            permission: 'KITCHEN_ATTENDANCE'
        },
        {
            name: '考勤设置',
            permission: 'KITCHEN_ATTENDANCE_SETTINGS'
        },
        {
            name: '加班申请',
            permission: 'WORK_OVERTIME_APPLY_ADE'
        },
        {
            name: '加班审核',
            permission: 'WORK_OVERTIME_APPLY_APPLY'
        },
        {
            name: '加班配置',
            permission: 'WORK_OVERTIME_CONFIG_ADE'
        },
        {
            name: '加班记录',
            permission: 'WORK_OVERTIME_RECORD_ADE'
        },
        {
            name: '请假管理',
            permission: 'KITCHEN_VACATE'
        },
        {
            name: '请假审核',
            permission: 'KITCHEN_VACATE_APPLY'
        },
        {
            name: '晨检管理',
            permission: 'KITCHEN_MORNING_INSPECT'
        },
        {
            name: '消毒管理',
            permission: 'KITCHEN_DISINFECT'
        },
        {
            name: '留样管理',
            permission: 'KITCHEN_SAMPLE'
        },
        {
            name: '厨品管理',
            permission: 'KITCHEN_OPERATION'
        },
        {
            name: '消防安全',
            permission: 'KITCHEN_SAFETY_INSPECT'
        },
        {
            name: '设备设施管理',
            permission: 'KITCHEN_FACILITY'
        },
        {
            name: '餐厨垃圾管理',
            permission: 'KITCHEN_GARBAGE'
        },
        {
            name: '环境卫生',
            permission: 'KITCHEN_ENV'
        },
        {
            name: '食品添加剂管理',
            permission: 'KITCHEN_FOOD_ADDITIVE'
        },
        {
            name: '烹饪加工管理',
            permission: 'KITCHEN_COOK'
        }
    ]
}

const financePermission = {
    name: '消费管理',
    permission: 'FIN_ALL',
    permissionType: 1,
    ignore: false,
    children: [
        {
            name: '消费管理',
            permission: 'FIN_CONSUME'
        }
    ]
}

const feedbackPermission = {
    name: '意见反馈',
    permission: 'FEEDBACK_ALL',
    permissionType: 1,
    ignore: false,
    children: [
        {
            name: '反馈列表',
            permission: 'FEEDBACK'
        },
        {
            name: '处理反馈',
            permission: 'FEEDBACK_EDIT'
        },
        {
            name: '删除反馈',
            permission: 'FEEDBACK_DELETE'
        },
        {
            name: '反馈类型',
            permission: 'FEEDBACK_TYPE'
        },
        {
            name: '新增/修改/删除类型',
            permission: 'FEEDBACK_TYPE_EDIT'
        }
    ]
}

const takeoutPermission = {
    name: '网上商城',
    permission: 'TAKE_OUT_ALL',
    permissionType: 1,
    ignore: false,
    children: [
        {
            name: '订单管理', permission: 'TAKE_OUT_ORDER_ALL'
        },
        {
            name: '商品管理', permission: 'TAKEOUT_PRODUCT_LIST'
        },
        {
            name: '新增/编辑/删除商品',
            permission: 'TAKEOUT_PRODUCT_EDIT'
        },
        {
            name: '新增/编辑/删除商品类别',
            permission: 'TAKEOUT_PRODUCT_TYPE_EDIT'
        }
    ]
}

const backSourcesPermission = {
    name: '溯源管理',
    permission: 'BACK_SOURCE_ALL',
    permissionType: 1,
    ignore: false,
    children: [
        {
            name: '食材溯源', permission: 'BACK_SOURCE_LIST'
        },
    ]
}

const systemPermission = {
    name: '系统设置',
    permissionType: 1,
    permission: 'SYS_ALL',
    children: [
        {
            name: '角色管理',
            permission: 'SYS_ROLE'
        },
        {
            name: '角色增/删/改',
            permission: 'SYS_ROLE_EDIT'
        },
        {
            name: '查看用户角色',
            permission: 'SYS_ROLE_USER'
        },
        {
            name: '配置用户角色',
            permission: 'SYS_ROLE_USER_EDIT'
        },
        {
            name: '查看权限',
            permission: 'SYS_ROLE_PERMISSION'
        },
        {
            name: '配置权限',
            permission: 'SYS_ROLE_PERMISSION_EDIT'
        }
    ]
}

const stockPermission = {
    name: '进销存管理',
    permissionType: 1,
    permission: 'STOCK_ALL',
    ignore: false,
    children: [
        // {
        //     name: '入库管理',
        //     permission: 'STOCK_IN_ALL'
        // },
        {
            name: '采购入库',
            permission: 'STOCK_IN'
        },
        {
            name: '采购入库申请',
            permission: 'STOCK_IN_APPLY'
        },
        {
            name: '入库明细',
            permission: 'STOCK_IN_DETAIL'
        },
        {
            name: '采购退货',
            permission: 'STOCK_IN_BACK'
        },
        {
            name: '采购退货申请',
            permission: 'STOCK_IN_BACK_APPLY'
        },
        {
            name: '退货明细',
            permission: 'STOCK_IN_BACK_DETAIL'
        },
        // {
        //     name: '出库管理',
        //     permission: 'STOCK_OUT_ALL'
        // },
        {
            name: '领用出库',
            permission: 'STOCK_OUT'
        },
        {
            name: '领用出库申请',
            permission: 'STOCK_OUT_APPLY'
        },
        {
            name: '出库明细',
            permission: 'STOCK_OUT_DETAIL'
        },
        {
            name: '领用退库',
            permission: 'STOCK_OUT_BACK'
        },
        {
            name: '领用退库申请',
            permission: 'STOCK_OUT_BACK_APPLY'
        },
        {
            name: '退库明细',
            permission: 'STOCK_OUT_BACK_DETAIL'
        },
        // {
        //     name: '库存设置',
        //     permission: 'STOCK_CONFIG_ALL'
        // },
        {
            name: '商品管理',
            permission: 'STOCK_GOODS_MANAGE'
        },
        {
            name: '新增/修改/删除商品类别',
            permission: 'STOCK_GOODS_TYPE_EDIT'
        },
        {
            name: '新增/修改/删除商品',
            permission: 'STOCK_GOODS_EDIT'
        },
        {
            name: '仓库类型',
            permission: 'STOCK_WAREHOUSE_TYPE_MANAGE'
        },
        {
            name: '新增/修改/删除仓库类型',
            permission: 'STOCK_WAREHOUSE_TYPE_EDIT'
        },
        {
            name: '仓库管理',
            permission: 'STOCK_WAREHOUSE_MANAGE'
        },
        {
            name: '新增/修改/删除仓库',
            permission: 'STOCK_WAREHOUSE_EDIT'
        },
        {
            name: '供应商类型',
            permission: 'STOCK_SUPPLIER_TYPE'
        },
        {
            name: '新增/修改/删除供应商类型',
            permission: 'STOCK_SUPPLIER_TYPE_EDIT'
        },
        {
            name: '供应商管理',
            permission: 'STOCK_SUPPLIER_MANAGE'
        },
        {
            name: '新增/修改/删除供应商',
            permission: 'STOCK_SUPPLIER_EDIT'
        },
        // {
        //     name: '流程管理',
        //     permission: 'STOCK_FLW_MANAGE'
        // },
        {
            name: '流程设置',
            permission: 'STOCK_FLW_MANAGE'
        },
        // {
        //     name: '库存管理',
        //     permission: 'STOCK_MANAGE_ALL'
        // },
        {
            name: '库存查询',
            permission: 'STOCK_SEARCH'
        },
        {
            name: '库存盘点',
            permission: 'STOCK_INVENTORY'
        },
        {
            name: '盘点申请',
            permission: 'STOCK_INVENTORY_APPLY'
        },
        {
            name: '库存预警',
            permission: 'STOCK_WARNING'
        },
        {
            name: '库存汇总表',
            permission: 'STOCK_STAT_LIST'
        },
        {
            name: '待办列表',
            permission: 'STOCK_TODO'
        },
        {
            name: '智慧采购',
            permission: 'PROCUREMENT_PLAN_LIST'
        }
    ]
}

const systemLog = {
    name: '系统日志',
    permissionType: 1,
    permission: 'SYS_LOG',
    children: [
        {
            name: '日志列表',
            permission: 'SYS_LOG_LIST'
        }
    ]
}

const interactivePermission = {
    name: '互动管理',
    permission: 'INTERACTIVE_MANAGEMENT',
    permissionType: 1,
    ignore: false,
    children: [
        {
            name: '资讯类型', permission: 'INFORMATION_TYPE'
        },
        {
            name: '新增/编辑/删除资讯类型', permission: 'INFORMATION_TYPE_EDIT'
        },
        {
            name: '资讯列表', permission: 'INFORMATION_LIST'
        }, {
            name: '新增/编辑/删除资讯资讯', permission: 'INFORMATION_EDIT'
        },
        {
            name: '问卷调查', permission: 'QS_MANAGEMENT'
        }, {
            name: '新增/编辑/删除问卷调查', permission: 'QS_EDIT'
        },
        {
            name: '投票活动', permission: 'VOTE_MANAGEMENT'
        },
        {
            name: '新增/编辑/删除投票活动', permission: 'VOTE_EDIT'
        },
    ]
}

const liveTelecastPermission = {
    name: '直播管理',
    permissionType: 1,
    permission: 'LIVE_TELECAST_ALL',
    children: [
        {
            name: '直播列表',
            permission: 'LIVE_TELECAST_LIST'
        },
        {
            name: '直播成员',
            permission: 'LIVE_TELECAST_MEMBER'
        }
    ]
}

const haircutPermission = {
    name: '理发预约',
    permissionType: 1,
    permission: 'HAIRCUT_ALL'
}

const emptyPlatePermission = {
    name: '光盘行动',
    permissionType: 1,
    permission: 'EMPTY_PLATE_ALL',
    children: [
        {
            name: '浪费列表',
            permission: 'EMPTY_PLATE'
        },
        {
            name: '浪费统计',
            permission: 'EMPTY_PLATE_STAT'
        },
        {
            name: '推送配置列表',
            permission: 'EMPTY_PLATE_SET_LIST'
        },
        {
            name: '推送配置',
            permission: 'EMPTY_PLATE_SET'
        }
    ]
}

const permissions = [
    orgPermission,
    menuPermission,
    canteenPermission,
    kitchenPermission,
    financePermission,
    backSourcesPermission,
    takeoutPermission,
    feedbackPermission,
    systemPermission,
    stockPermission,
    systemLog,
    reservedRoom,
    interactivePermission,
    liveTelecastPermission,
    haircutPermission,
    equipmentPermission,
    emptyPlatePermission
]

const map = {}
const buildSql = (item, id, pid) => {
    const {
        name, permission, permissionType
    } = item
    if (map[permission
        ]) {
        throw new Error(`duplicate permission:${permission
        }`)
    }
    map[permission] = true
    const sql = `insert into sys_permission(id, name, permission, permission_type, pid, sort, create_time)
                 values (${id}, '${name}', '${permission}', 1, ${pid}, ${id}, now());`
    return sql
}
let id = 0
const sqls = []
permissions.forEach(item => {
    if (item.ignore) { // 可以给权限配置一个ignore属性，当该属性为true时，会跳过不输出
        return
    }
    let sql = buildSql(item, ++id, null)
    sqls.push(sql)
    let pid = id
    item.children && item.children.forEach(item2 => {
        if (item2.ignore) {
            return
        }
        sql = buildSql(item2, ++id, pid)
        sqls.push(sql)
    })
})
console.log(sqls.join('\n'))