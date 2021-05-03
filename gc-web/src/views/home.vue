<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">

      <panel-group @handleSetLineChartData="handleSetLineChartData" />

      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <line-chart :chart-data="lineChartData" />
      </el-row>
      <el-row :gutter="32">
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <radar-chart />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <pie-chart />
          </div>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <bar-chart />
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RadarChart from '@/components/Echarts/RadarChart'
import PieChart from '@/components/Echarts/PieChart'
import BarChart from '@/components/Echarts/BarChart'
import { getTotalLineChart } from '@/api/report'


const funcMap = new Map([
  [1, '平台累计用户数'],
  [2, '平台累计设备数'],
  [3, '累计积分发放额'],
  [4, '累计商品兑换数']
]);

export default {
  name: 'Dashboard',
  components: {
    PanelGroup,
    LineChart,
    RadarChart,
    PieChart,
    BarChart
  },
  data() {
    return {
      lineChartData: {
        actualData: [],
        title: '',
        xNames: []
      },
    }
  },
  methods: {
    handleSetLineChartData(type) {
      getTotalLineChart(type).then(value => {
        this.lineChartData = value;
        this.lineChartData.title = funcMap.get(type);
      })
    }
  },
  created() {
    this.handleSetLineChartData(1);
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .github-corner {
      position: absolute;
      top: 0;
      border: 0;
      right: 0;
    }

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>
