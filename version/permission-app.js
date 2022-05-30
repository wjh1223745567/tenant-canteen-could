const APP_MRCP = {
    name: '每日菜谱',
    permission: 'APP_MRCP',
    permissionType: 0,
    ignore: false,
    children: [
        {
            name: '每日菜谱列表',
            permission: 'APP_MRCP_LB'
        },
        {
            name: '点评',
            permission: 'APP_MRCP_DP'
        }
    ]
}

const APP_CTSK = {
    name: '餐厅实况',
    permission: 'APP_CTSK',
    permissionType: 0,
    ignore: false,
    children: []
}

const APP_CPBP = {
    name: '菜谱编排',
    permission: 'APP_CPBP',
    permissionType: 0,
    ignore: false,
    children: []
}

const APP_HCGL = {
    name: '后厨管理',
    permission: 'APP_HCGL',
    permissionType: 0,
    ignore: false,
    children: [
        {
            name: "关怀提醒",
            permission: 'APP_HCGL_GHTX'
        },
        {
            name: "花名册",
            permission: 'APP_HCGL_HMC'
        },
        {
            name: "员工关怀",
            permission: 'APP_HCGL_YGGH'
        },
        {
            name: "员工考核",
            permission: 'APP_HCGL_YGKH'
        },
        {
            name: "员工考勤",
            permission: 'APP_HCGL_YGKQ',
            children: [
                {
                    name: '员工考勤-请假申请',
                    permission: 'APP_HCGL_YGKQ_QJ'
                }
            ]
        },
        {
            name: "规章制度",
            permission: 'APP_HCGL_GZZD'
        },
        {
            name: "晨检管理",
            permission: 'APP_HCGL_CJGL',
            children: [
                {
                    name: '添加/编辑/删除晨检记录',
                    permission: 'APP_HCGL_CJGL_AED'
                }
            ]
        },
        {
            name: "消毒管理",
            permission: 'APP_HCGL_XDGL',
            children: [
                {
                    name: '添加/编辑/删除消毒记录',
                    permission: 'APP_HCGL_XDGL_AED'
                }
            ]
        },
        {
            name: "明厨亮灶",
            permission: 'APP_HCGL_MCLZ'
        },
        {
            name: "设备设施管理",
            permission: 'APP_HCGL_SBSS',
            children: [
                {
                    name: '添加/编辑/删除设备检查记录',
                    permission: 'APP_HCGL_SBSS_AED'
                }
            ]
        },
        {
            name: "留样管理",
            permission: 'APP_HCGL_LYGL',
            children: [
                {
                    name: '添加/编辑/删除留样检查记录',
                    permission: 'APP_HCGL_LYGL_AED'
                }
            ]
        },
        {
            name: "清洗管理",
            permission: 'APP_HCGL_QXGL',
            children: [
                {
                    name: '添加/编辑/删除清洗检查记录',
                    permission: 'APP_HCGL_QXGL_AED'
                }
            ]
        },
        {
            name: "切配管理",
            permission: 'APP_HCGL_QPGL',
            children: [
                {
                    name: '添加/编辑/删除切配检查记录',
                    permission: 'APP_HCGL_QPGL_AED'
                }
            ]
        },
        {
            name: "烹饪加工管理",
            permission: 'APP_HCGL_PRJG',
            children: [
                {
                    name: '添加/编辑/删除烹饪加工检查记录',
                    permission: 'APP_HCGL_PRJG_AED'
                }
            ]
        },
        {
            name: "添加剂管理",
            permission: 'APP_HCGL_TJJ',
            children: [
                {
                    name: '添加/编辑/删除添加剂检查记录',
                    permission: 'APP_HCGL_TJJ_AED'
                }
            ]
        },
        {
            name: "厨余垃圾管理",
            permission: 'APP_HCGL_CYLJ',
            children: [
                {
                    name: '添加/编辑/删除厨余垃圾检查记录',
                    permission: 'APP_HCGL_CYLJ_AED'
                }
            ]
        },
        {
            name: "环境卫生",
            permission: 'APP_HCGL_HJWS',
            children: [
                {
                    name: '添加/编辑/删除环境卫生检查记录',
                    permission: 'APP_HCGL_HJWS_AED'
                }
            ]
        },
        {
            name: "消防安全",
            permission: 'APP_HCGL_XFAQ',
            children: [
                {
                    name: '添加/编辑/删除消防安全检查记录',
                    permission: 'APP_HCGL_XFAQ_AED'
                }
            ]
        }
    ]
}

const APP_CKQB = {
    name: '餐卡钱包',
    permission: 'APP_CKQB',
    permissionType: 0,
    ignore: false,
    children: [
        {
            name: '充值',
            permission: 'APP_CKQB_CZ'
        }
    ]
}

const APP_WSSC = {
    name: '网上商城',
    permission: 'APP_WSSC',
    permissionType: 0,
    ignore: false,
    children: []
}

const APP_SCFH = {
    name: '商城发货',
    permission: 'APP_SCFH',
    permissionType: 0,
    ignore: false,
    children: []
}

const APP_JXC = {
    name: '进销存管理',
    permission: 'APP_JXC',
    permissionType: 0,
    ignore: false,
    children: [
        {
            name: '采购入库',
            permission: 'APP_JXC_CGRK',
            children: [
                {
                    name: '采购入库申请',
                    permission: 'APP_JXC_CGRK_SQ'
                }
            ]
        },
        {
            name: '入库明细',
            permission: 'APP_JXC_RK_MX'
        },
        {
            name: '采购退货',
            permission: 'APP_JXC_CGTH',
            children: [
                {
                    name: '采购退货申请',
                    permission: 'APP_JXC_CGTH_SQ'
                }
            ]
        },
        {
            name: '退货明细',
            permission: 'APP_JXC_CGTH_MX'
        },
        {
            name: '领用出库',
            permission: 'APP_JXC_LYCK',
            children: [
                {
                    name: '领用出库申请',
                    permission: 'APP_JXC_LYCK_SQ'
                }
            ]
        },
        {
            name: '出库明细',
            permission: 'APP_JXC_CKMX'
        },
        {
            name: '领用退库',
            permission: 'APP_JXC_LYTK',
            children: [
                {
                    name: '领用退库申请',
                    permission: 'APP_JXC_LYTK_SQ'
                }
            ]
        },
        {
            name: '退库明细',
            permission: 'APP_JXC_TKMX'
        },
        {
            name: '库存查询',
            permission: 'APP_JXC_KCCX'
        },
        {
            name: '库存盘点',
            permission: 'APP_JXC_KCPD',
            children: [
                {
                    name: '库存盘点申请',
                    permission: 'APP_JXC_KCPD_SQ'
                }
            ]
        },
        {
            name: '库存预警',
            permission: 'APP_JXC_KCYJ'
        },
        {
            name: '商品',
            permission: 'APP_JXC_SP'
        },
        {
            name: '供应商',
            permission: 'APP_JXC_GYS'
        },
        {
            name: '仓库',
            permission: 'APP_JXC_CK'
        }
    ]
}

const APP_SPBK = {
    name: '食谱百科',
    permission: 'APP_SPBK',
    permissionType: 0,
    ignore: false,
    children: []
}

const APP_YYDA = {
    name: '营养档案',
    permission: 'APP_YYDA',
    permissionType: 0,
    ignore: false,
    children: []
}

const APP_ROOM_RESERVE = {
    name: '订房订餐',
    permission: 'APP_ROOM_RESERVE',
    permissionType: 0,
    ignore: false,
    children: [
        {
            name: '预定审核权限',
            permission: 'APP_TO_EXAMINE'
        },
        {
            name: '预定取消权限',
            permission: 'APP_CANCEL'
        }
    ]
}

const APP_QUESTIONNAIRE_SURVEY = {
    name: '问卷调查',
    permission: 'APP_QUESTIONNAIRE_SURVEY',
    permissionType: 0,
    ignore: false,
}

const APP_VOTING_ACTIVITY = {
    name: '投票活动',
    permission: 'APP_VOTING_ACTIVITY',
    permissionType: 0,
    ignore: false,
}

const APP_LIVE_TELECAST = {
    name: '互动直播',
    permission: 'APP_LIVE_TELECAST',
    permissionType: 0,
    ignore: false,
}

const APP_HAIRCUT = {
    name: '理发预约',
    permission: 'APP_HAIRCUT',
    permissionType: 0,
    ignore: false,
    children: [
        {
            name: '理发师权限',
            permission: 'APP_HAIRCUT_MASTER'
        },
        {
            name: '用户权限',
            permission: 'APP_HAIRCUT_USER'
        }
    ]
}


const APP_PERMISSIONS = [
    APP_MRCP,
    APP_CTSK,
    APP_HCGL,
    APP_CKQB,
    APP_WSSC,
    APP_SCFH,
    APP_JXC,
    APP_SPBK,
    APP_YYDA,
    APP_CPBP,
    APP_ROOM_RESERVE,
    APP_QUESTIONNAIRE_SURVEY,
    APP_VOTING_ACTIVITY,
    APP_LIVE_TELECAST,
    APP_HAIRCUT
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
                 values (${id}, '${name}', '${permission}', 0, ${pid}, ${id}, now());`
    return sql
}
let id = 138
const sqls = []
let sql

let build = (item, pid) => {
    item.children && item.children.forEach(item2 => {
        if (item2.ignore) {
            return
        }
        sql = buildSql(item2, ++id, pid)
        sqls.push(sql)

        if (item.children && item.children.length > 0) {
            build(item2, pid)
        }
    })
}

APP_PERMISSIONS.forEach(item => {
    if (item.ignore) { // 可以给权限配置一个ignore属性，当该属性为true时，会跳过不输出
        return
    }
    sql = buildSql(item, ++id, null)
    sqls.push(sql)


    build(item, id)
})
console.log(sqls.join('\n'))