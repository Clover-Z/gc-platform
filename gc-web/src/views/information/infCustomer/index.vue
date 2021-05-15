<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">持卡人姓名</label>
        <el-input v-model="query.name" clearable placeholder="持卡人姓名" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">卡号</label>
        <el-input v-model="query.cardNo" clearable placeholder="卡号" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">用户状态</label>
        <el-select v-model="query.status" clearable placeholder="用户状态" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" >
          <el-option v-for="item in userStatus" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="600px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
<!--          <el-form-item label="id" prop="id">-->
<!--            <el-input v-model="form.id" style="width: 370px;" />-->
<!--          </el-form-item>-->
          <el-form-item label="持卡人姓名" prop="name">
            <el-input v-model="form.name" style="width: 370px;" :disabled="crud.status.edit == 1" />
          </el-form-item>
          <el-form-item label="卡号" prop="cardNo">
            <el-input v-model="form.cardNo" style="width: 370px;" :disabled="crud.status.edit == 1" />
          </el-form-item>
          <el-form-item label="地址" prop="address">
            <el-input v-model="form.address" style="width: 370px;" />
          </el-form-item>
<!--          <el-form-item label="积分" prop="integral">-->
<!--            <el-input v-model="form.integral" style="width: 370px;" />-->
<!--          </el-form-item>-->
          <el-form-item label="用户状态" prop="status">
            <el-select v-model="form.status" default-first-option >
              <el-option v-for="item in userStatus" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
<!--          <el-form-item label="注册时间" prop="createTime">-->
<!--            <el-date-picker v-model="form.createTime" type="datetime" style="width: 370px;" />-->
<!--          </el-form-item>-->
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="持卡人姓名" />
        <el-table-column prop="cardNo" label="卡号" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="integral" label="积分" />
        <el-table-column prop="status" label="用户状态" :formatter="this.userStatusFormatter" />
        <el-table-column prop="createTime" label="注册时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','infCustomer:edit','infCustomer:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudInfCustomer from '@/api/information/infCustomer'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, name: null, cardNo: null, address: null, integral: null, status: null, createTime: null }
export default {
  name: 'InfCustomer',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: '客户信息维护', url: 'api/infCustomer', idField: 'id', sort: 'createTime,desc', crudMethod: { ...crudInfCustomer }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'infCustomer:add'],
        edit: ['admin', 'infCustomer:edit'],
        del: ['admin', 'infCustomer:del']
      },

      userStatus: [
        { value: '1', label: '1-正常' },
        { value: '2', label: '2-封禁' },
        { value: '3', label: '3-已删除' }
      ],

      rules: {
        name: [
          { required: true, message: '持卡人姓名不能为空', trigger: 'blur' }
        ],
        cardNo: [
          { required: true, message: '卡号不能为空', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '地址不能为空', trigger: 'blur' }
        ],
        integral: [
          { required: true, message: '积分不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '用户状态不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '注册时间不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'name', display_name: '持卡人姓名' },
        { key: 'cardNo', display_name: '卡号' },
        { key: 'status', display_name: '用户状态' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },

    userStatusFormatter(row) {
      for (let i = 0; i < this.userStatus.length; i++) {
        // eslint-disable-next-line eqeqeq
        if (this.userStatus[i].value == row.status) {
          return this.userStatus[i].label
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
