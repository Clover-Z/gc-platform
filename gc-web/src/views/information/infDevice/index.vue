<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <label class="el-form-item-label">设备号</label>
        <el-input v-model="query.id" clearable placeholder="设备号" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" />
        <label class="el-form-item-label">设备状态</label>
        <el-select v-model="query.status" clearable placeholder="设备状态" style="width: 185px;" class="filter-item" @keyup.enter.native="crud.toQuery" >
          <el-option label="0-未启用" value="0" />
          <el-option label="1-已启用" value="1" />
        </el-select>
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="600px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
          <el-form-item label="设备号" prop="id">
            <el-input v-model="form.id" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="设备描述" prop="remark">
            <el-input v-model="form.remark" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="经度" prop="longitude">
            <el-input v-model="form.longitude" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="纬度" prop="latitude">
            <el-input v-model="form.latitude" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="设备详细地址" prop="address">
            <el-input v-model="form.address" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="设备状态" prop="status">
            <el-select v-model="form.status" style="width: 370px;"  filterable placeholder="请选择">
              <el-option label="0-未启用" value="0" />
              <el-option label="1-已启用" value="1" />
            </el-select>
          </el-form-item>
          <el-form-item label="设备负责人">
            <el-input v-model="form.person" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="负责人联系方式">
            <el-input v-model="form.phone" style="width: 370px;" />
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
        <el-table-column prop="id" label="设备号" />
        <el-table-column prop="remark" label="设备描述" />
        <el-table-column prop="longitude" label="经度" />
        <el-table-column prop="latitude" label="纬度" />
        <el-table-column prop="address" label="设备详细地址" />
        <el-table-column prop="status" label="设备状态">
          <template slot-scope="scope">
            {{ dict.label.设备状态[scope.row.status] }}
          </template>
        </el-table-column>
        <el-table-column prop="person" label="设备负责人" />
        <el-table-column prop="phone" label="负责人联系方式" />
        <el-table-column prop="forDept" label="设备归属部门" />
        <el-table-column v-permission="['admin','infDevice:edit','infDevice:del']" label="操作" width="150px" align="center">
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
import crudInfDevice from '@/api/information/infDevice'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, remark: null, longitude: null, latitude: null, address: null, status: null, person: null, phone: null, forDept: null }
export default {
  name: 'InfDevice',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['设备状态'],
  cruds() {
    return CRUD({ title: '设备信息维护', url: 'api/infDevice', idField: 'id', sort: 'id,desc', crudMethod: { ...crudInfDevice }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'infDevice:add'],
        edit: ['admin', 'infDevice:edit'],
        del: ['admin', 'infDevice:del']
      },
      rules: {
        id: [
          { required: true, message: '设备号不能为空', trigger: 'blur' }
        ],
        remark: [
          { required: true, message: '设备描述不能为空', trigger: 'blur' }
        ],
        longitude: [
          { required: true, message: '经度不能为空', trigger: 'blur' }
        ],
        latitude: [
          { required: true, message: '纬度不能为空', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '设备详细地址不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '设备状态不能为空', trigger: 'blur' }
        ]
      },
      queryTypeOptions: [
        { key: 'id', display_name: '设备号' },
        { key: 'remark', display_name: '设备描述' },
        { key: 'longitude', display_name: '经度' },
        { key: 'latitude', display_name: '纬度' },
        { key: 'address', display_name: '设备详细地址' },
        { key: 'status', display_name: '设备状态' },
        { key: 'person', display_name: '设备负责人' },
        { key: 'phone', display_name: '负责人联系方式' }
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
