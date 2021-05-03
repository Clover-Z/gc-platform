<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">商品id</label>
        <el-input v-model="query.id" clearable placeholder="商品id" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">商品名称</label>
        <el-input v-model="query.name" clearable placeholder="商品名称" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">商品分类</label>
        <el-select v-model="query.type" clearable placeholder="商品分类" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery">
          <el-option v-for="item in goodsType" :key="item" :value="item" :label="item"></el-option>
        </el-select>
        <label class="el-form-item-label">商品状态</label>
        <el-select v-model="query.status" clearable placeholder="商品状态" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" >
<!--          <el-option v-for="item in goodsStatus" :key="item.value" :label="item.label" :value="item.value" />-->
          <el-option label="0-待上架" value="0" />
          <el-option label="1-已上架" value="1" />
          <el-option label="2-已下架" value="2" />
        </el-select>
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="600px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
          <el-form-item label="商品id" prop="id">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商品名称" prop="name">
            <el-input v-model="form.name" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="商品分类" prop="type">
            <el-select v-model="form.type" style="width: 370px;">
              <el-option v-for="item in goodsType" :key="item" :value="item" :label="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商品数量" prop="stock">
            <el-input v-model="form.stock" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="兑换所需积分" prop="cost">
            <el-input v-model="form.cost" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" style="width: 370px;">
<!--              <el-option v-for="item in goodsStatus" :key="item.value" :label="item.label" :value="item.value" />-->
              <el-option label="0-待上架" value="0" />
              <el-option label="1-已上架" value="1" />
              <el-option label="2-已下架" value="2" />
            </el-select>
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
        <el-table-column prop="id" label="商品id" />
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="type" label="商品分类" />
        <el-table-column prop="stock" label="商品数量" />
        <el-table-column prop="cost" label="兑换所需积分" />
        <el-table-column prop="status" label="状态" :formatter="statusFormatter" />
        <el-table-column prop="createTime" label="创建时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-permission="['admin','infGoods:edit','infGoods:del']" label="操作" width="150px" align="center">
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
import crudInfGoods from '@/api/information/infGoods'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, name: null, type: null, stock: null, cost: null, status: null, createTime: null }
export default {
  name: 'InfGoods',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: '兑换商品信息维护', url: 'api/infGoods', idField: 'id', sort: 'id,desc', crudMethod: { ...crudInfGoods }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'infGoods:add'],
        edit: ['admin', 'infGoods:edit'],
        del: ['admin', 'infGoods:del']
      },

      goodsType: [
        '服饰', '数码', '家具', '百货', '食品', '虚拟'
      ],
      // goodsStatus: [
      //   { label: '0-待上架', value: '0' },
      //   { label: '1-已上架', value: '1' },
      //   { label: '2-已下架', value: '2' }
      // ],

      rules: {
        id: [
          { required: true, message: '商品id不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '商品名称不能为空', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '商品分类不能为空', trigger: 'blur' }
        ],
        stock: [
          { required: true, message: '商品数量不能为空', trigger: 'blur' }
        ],
        cost: [
          { required: true, message: '兑换所需积分不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'id', display_name: '商品id' },
        { key: 'name', display_name: '商品名称' },
        { key: 'type', display_name: '商品分类' },
        { key: 'stock', display_name: '商品数量' },
        { key: 'cost', display_name: '兑换所需积分' },
        { key: 'status', display_name: '状态' }
      ]
    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },

    statusFormatter(row) {
      let t = row.status;
      switch (t) {
        case 0: t += '-待上架'; break;
        case 1: t += '-已上架'; break;
        case 2: t += '-已下架'; break;
      }
      return t
    }
  }
}
</script>

<style scoped>

</style>
