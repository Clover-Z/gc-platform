<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">交易编号</label>
        <el-input v-model="query.txn" clearable placeholder="交易编号" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">交易卡号</label>
        <el-input v-model="query.txnCard" clearable placeholder="交易卡号" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">交易设备号</label>
        <el-input v-model="query.txnDevice" clearable placeholder="交易设备号" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="600px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
          <el-form-item label="交易卡号" prop="txnCard">
            <el-input v-model="form.txnCard" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="交易设备号" prop="txnDevice">
            <el-input v-model="form.txnDevice" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="垃圾类别" prop="gcType">
            <el-select v-model="form.gcType" style="width: 370px;">
              <el-option label="废铁" value="废铁" />
              <el-option label="木材" value="木材" />
              <el-option label="衣物" value="衣物" />
            </el-select>
          </el-form-item>
          <el-form-item label="垃圾重量" prop="gcWeight">
            <el-input v-model="form.gcWeight" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="获得积分额" prop="txnIntegral">
            <el-input v-model="form.txnIntegral" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="txn" label="交易编号" />
        <el-table-column prop="txnCard" label="交易卡号" />
        <el-table-column prop="txnDevice" label="交易设备号" />
        <el-table-column prop="txnDate" label="交易日期" />
        <el-table-column prop="gcType" label="垃圾类别" />
        <el-table-column prop="gcWeight" label="垃圾重量" />
        <el-table-column prop="txnIntegral" label="获得积分额" />
        <el-table-column prop="createTime" label="创建时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','dbCollectionRecord:edit','dbCollectionRecord:del']" label="操作" width="150px" align="center">
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
import crudDbCollectionRecord from '@/api/db/dbCollectionRecord'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { txn: null, txnCard: null, txnDate: null, gcType: null, gcWeight: null, txnIntegral: null, txnDevice: null, createTime: null }
export default {
  name: 'DbCollectionRecord',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: '投放记录维护', url: 'api/dbCollectionRecord', idField: 'txn', sort: 'txn,desc', crudMethod: { ...crudDbCollectionRecord }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'dbCollectionRecord:add'],
        edit: ['admin', 'dbCollectionRecord:edit'],
        del: ['admin', 'dbCollectionRecord:del']
      },
      rules: {
        txnCard: [
          { required: true, message: '交易卡号不能为空', trigger: 'blur' }
        ],
        gcType: [
          { required: true, message: '垃圾类别不能为空', trigger: 'blur' }
        ],
        gcWeight: [
          { required: true, message: '垃圾重量不能为空', trigger: 'blur' }
        ],
        txnIntegral: [
          { required: true, message: '获得积分额不能为空', trigger: 'blur' }
        ],
        txnDevice: [
          { required: true, message: '交易设备号不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'txn', display_name: '交易编号' },
        { key: 'txnCard', display_name: '交易卡号' },
        { key: 'txnDevice', display_name: '交易设备号' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
