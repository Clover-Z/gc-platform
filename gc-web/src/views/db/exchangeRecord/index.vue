<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">交易编号</label>
        <el-input v-model="query.id" clearable placeholder="交易编号" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">交易卡号</label>
        <el-input v-model="query.txnCard" clearable placeholder="交易卡号" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">兑换商品编号</label>
        <el-input v-model="query.goodsNo" clearable placeholder="兑换商品编号" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">交易日期</label>
        <el-date-picker v-model="query.txnDate" clearable placeholder="交易日期" style="width: 185px;" class="filter-item" />
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="600px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
          <el-form-item label="交易编号" prop="id">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="交易日期" prop="txnDate">
            <el-date-picker v-model="form.txnDate" type="date" style="width: 370px;" value-format="yyyyMMdd" />
          </el-form-item>
          <el-form-item label="交易卡号" prop="txnCard">
            <el-input v-model="form.txnCard" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="兑换商品编号" prop="goodsNo">
            <el-input v-model="form.goodsNo" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商品兑换数量" prop="goodsCount">
            <el-input v-model="form.goodsCount" style="width: 370px;" />
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
        <el-table-column prop="id" label="交易编号" />
        <el-table-column prop="txnDate" label="交易日期" />
        <el-table-column prop="txnIntegral" label="交易花费积分" />
        <el-table-column prop="txnCard" label="交易卡号" />
        <el-table-column prop="goodsNo" label="兑换商品编号" />
        <el-table-column prop="goodsCount" label="商品兑换数量" />
        <el-table-column prop="goodsType" label="商品兑换分类" />
        <el-table-column prop="createTime" label="创建时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','dbExchangeRecord:edit','dbExchangeRecord:del']" label="操作" width="150px" align="center">
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
import crudDbExchangeRecord from '@/api/db/dbExchangeRecord'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, txnDate: null, txnIntegral: null, txnCard: null, goodsNo: null, goodsCount: null, goodsType: null, createTime: null }
export default {
  name: 'DbExchangeRecord',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: '积分兑换记录维护', url: 'api/dbExchangeRecord', idField: 'id', sort: 'id,desc', crudMethod: { ...crudDbExchangeRecord }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'dbExchangeRecord:add'],
        edit: ['admin', 'dbExchangeRecord:edit'],
        del: ['admin', 'dbExchangeRecord:del']
      },
      rules: {
        id: [
          { required: true, message: '交易编号不能为空', trigger: 'blur' }
        ],
        txnDate: [
          { required: true, message: '交易日期不能为空', trigger: 'blur' }
        ],
        txnIntegral: [
          { required: true, message: '交易花费积分不能为空', trigger: 'blur' }
        ],
        txnCard: [
          { required: true, message: '交易卡号不能为空', trigger: 'blur' }
        ],
        goodsNo: [
          { required: true, message: '兑换商品编号不能为空', trigger: 'blur' }
        ],
        goodsCount: [
          { required: true, message: '商品兑换数量不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'id', display_name: '交易编号' },
        { key: 'txnIntegral', display_name: '交易花费积分' },
        { key: 'txnCard', display_name: '交易卡号' },
        { key: 'goodsNo', display_name: '兑换商品编号' },
        { key: 'goodsCount', display_name: '商品兑换数量' },
        { key: 'goodsType', display_name: '商品兑换分类' }
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
