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
            <el-input v-model="form.id" style="width: 370px;" :disabled="crud.status.edit == 1"/>
          </el-form-item>
          <el-form-item label="设备描述" prop="remark">
            <el-input v-model="form.remark" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="设备详细地址" prop="address">
            <el-input v-model="form.address" style="width: 370px;" />

            <div style="font-size: 1.5em">
              请使用地图选择经纬度
              <svg-icon icon-class="location" class-name="card-panel-icon" @click="openMap" />
            </div>
          </el-form-item>
          <el-form-item label="经度" prop="longitude" >
            <el-input v-model="form.longitude" style="width: 370px;" disabled />
          </el-form-item>
          <el-form-item label="纬度" prop="latitude" >
            <el-input v-model="form.latitude" style="width: 370px;" disabled />
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


    <el-dialog :visible.sync="mapV" style="width: 100%;" title="选择设备地址">
      <div class="containerMap">
        <div class="search-box">
          <input
            v-model="searchKey"
            type="search"
            id="search">
          <el-button @click="searchByHand" size="mini" type="primary">搜索</el-button>
          <el-button @click="submitAddress" size="mini" type="success">确认</el-button>
          <div class="tip-box" id="searchTip"></div>
        </div>
        <el-amap class="amap-box"
                 :amap-manager="amapManager"
                 :vid="'amap-vue'"
                 :zoom="zoom"
                 :plugin="plugin"
                 :center="center"
                 :events="events"
        >
          <!-- 标记 -->
          <el-amap-marker v-for="(marker, index) in markers" :position="marker" :key="index"></el-amap-marker>
        </el-amap>
      </div>

    </el-dialog>

  </div>
</template>

<script>
import crudInfDevice from '@/api/information/infDevice'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import {AMapManager, lazyAMapApiLoaderInstance} from 'vue-amap'
let amapManager = new AMapManager()

const defaultForm = { id: null, remark: null, longitude: null, latitude: null, address: null, status: null, person: null, phone: null, forDept: null }
export default {
  name: 'InfDevice',
  components: { pagination, crudOperation, rrOperation, udOperation},
  mixins: [presenter(), header(), form(defaultForm), crud()],
  dicts: ['设备状态'],
  cruds() {
    return CRUD({ title: '设备信息维护', url: 'api/infDevice', idField: 'id', sort: 'createTime,desc', crudMethod: { ...crudInfDevice }})
  },
  data() {
    let self = this

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
      ],


      //map
      mapV: false,

      searchKey: '',
      amapManager,
      markers: [],
      searchOption: {
        city: '全国',
        citylimit: true
      },
      center: [121.329402, 31.228667],
      zoom: 17,
      lng: 0,
      lat: 0,
      loaded: false,
      markerEvent:{
        click(e){
          console.log('点击',e);
        }
      },
      events: {
        init() {
          lazyAMapApiLoaderInstance.load().then(() => {
            self.initSearch()
          })
        },
        // 点击获取地址的数据
        // lng精度
        // lat维度
        click(e) {
          self.markers = []
          let {lng, lat} = e.lnglat
          self.lng = lng
          self.lat = lat
          console.log(e)
          self.center = [lng, lat]
          self.markers.push([lng, lat])
          // 这里通过高德 SDK 完成。
          let geocoder = new AMap.Geocoder({
            radius: 1000,
            extensions: 'all'
          })
          geocoder.getAddress([lng, lat], function (status, result) {
            if (status === 'complete' && result.info === 'OK') {
              if (result && result.regeocode) {
                console.log(result.regeocode.formattedAddress)
                self.address = result.regeocode.formattedAddress
                self.searchKey = result.regeocode.formattedAddress
                self.$nextTick()
              }
            }
          })
        }
      },
      // 一些工具插件
      plugin: [
        {
          // 定位
          pName: 'Geolocation',
          events: {
            init(o) {
              // o是高德地图定位插件实例
              o.getCurrentPosition((status, result) => {
                if (result && result.position) {
                  // 设置经度
                  self.lng = result.position.lng
                  // 设置维度
                  self.lat = result.position.lat
                  // 设置坐标
                  self.center = [self.lng, self.lat]
                  self.markers.push([self.lng, self.lat])
                  // load
                  self.loaded = true
                  // 页面渲染好后
                  self.$nextTick()
                }
              })
            },
            click(e){
              console.log(e);
            }
          }
        },
        {
          // 工具栏
          pName: 'ToolBar',
          events: {
            init(instance) {
              console.log(instance);
            }
          }
        },
        {
          // 鹰眼
          pName: 'OverView',
          events: {
            init(instance) {
              console.log(instance);
            }
          }
        },
        {
          // 地图类型
          pName: 'MapType',
          defaultType: 0,
          events: {
            init(instance) {
              console.log(instance);
            }
          }
        },
        {
          // 搜索
          pName: 'PlaceSearch',
          events: {
            init(instance) {
              console.log(instance)
            }
          }
        }
      ]
    }
  },

  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    },

    openMap() {
      this.mapV = true;
    },

    submitAddress(){
      console.log('经纬度',this.center)
      console.log('地址',this.address)
      this.form.longitude = this.center[0];
      this.form.latitude = this.center[1];
      this.form.address = this.address;
      this.mapV = false;
    },
    initSearch() {
      let vm = this
      let map = this.amapManager.getMap()
      AMapUI.loadUI(['misc/PoiPicker'], function (PoiPicker) {
        var poiPicker = new PoiPicker({
          input: 'search',
          placeSearchOptions: {
            map: map,
            pageSize: 10
          },
          suggestContainer: 'searchTip',
          searchResultsContainer: 'searchTip'
        })
        vm.poiPicker = poiPicker
        // 监听poi选中信息
        poiPicker.on('poiPicked', function (poiResult) {
          // console.log(poiResult)
          let source = poiResult.source
          let poi = poiResult.item
          if (source !== 'search') {
            poiPicker.searchByKeyword(poi.name)
          } else {
            poiPicker.clearSearchResults()
            vm.markers = []
            let lng = poi.location.lng
            let lat = poi.location.lat
            let address = poi.cityname + poi.adname + poi.name
            vm.center = [lng, lat]
            console.log(vm.center)
            vm.markers.push([lng, lat])
            vm.lng = lng
            vm.lat = lat
            vm.address = address
            vm.searchKey = address
          }
        })
      })
    },
    searchByHand() {
      if (this.searchKey !== '') {
        this.poiPicker.searchByKeyword(this.searchKey)
      }
    }
  }
}
</script>

<style scoped>
.containerMap {
  width:100%;
  height: 500px;
  position: relative;
  border: 1px solid #999;
}

.search-box {
  position: absolute;
  z-index: 5;
  width: 70%;
  left: 13%;
  top: 10px;
  height: 30px;
}

.search-box input {
  float: left;
  width: 59%;
  height: 100%;
  font-size:13px;
  border: 2px solid #30ccc1;
  padding: 0 8px;
  outline: none;
}

.search-box button {
  float: left;
  margin-left:5px;
  /* width: 20%;
  height: 100%;
  background: #30ccc1;
  border: 1px solid #30ccc1;
  color: #fff;
  outline: none; */
}

.tip-box {
  width: 100%;
  max-height: 260px;
  position: absolute;
  top: 30px;
  overflow-y: auto;
  background-color: #fff;
}
</style>
